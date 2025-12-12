package at.backend.MarketingCompany.account.auth.application;

import at.backend.MarketingCompany.account.auth.application.commands.*;
import at.backend.MarketingCompany.account.auth.application.query.GetUserSessionsQuery;
import at.backend.MarketingCompany.account.auth.application.query.ValidateTokenQuery;
import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthResult;
import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.auth.domain.exceptions.AuthValidationException;
import at.backend.MarketingCompany.account.auth.domain.exceptions.InvalidCredentialsException;
import at.backend.MarketingCompany.account.auth.domain.exceptions.InvalidTokenException;
import at.backend.MarketingCompany.account.auth.domain.exceptions.SessionNotFoundException;
import at.backend.MarketingCompany.account.auth.domain.repository.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.CreateUserParams;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.domain.exceptions.UserNotFoundException;
import at.backend.MarketingCompany.account.user.domain.exceptions.UserValidationException;
import at.backend.MarketingCompany.account.user.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final AuthSessionRepository authSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${app.auth.jwt.access.expiration-minutes:15}")
    private int accessTokenExpiryMinutes;

    @Value("${app.auth.jwt.refresh.expiration-days:30}")
    private int refreshTokenExpiryDays;

    @Value("${app.auth.max-sessions-per-user:5}")
    private int maxSessionsPerUser;

    @Transactional
    public AuthResult handle(SignUpCommand command) {
        log.info("Signing up new user with email: {}", command.email().value());

        validateEmailNotExists(command.email());

        var hashedPassword = new HashedPassword(passwordEncoder.encode(command.password().value()));
        log.info("Password hashed for email: {}", command.email().value());

        var createParams = CreateUserParams.builder()
                .email(command.email())
                .phoneNumber(command.phoneNumber())
                .hashedPassword(hashedPassword)
                .name(command.name())
                .build();

        User newUser = User.createUser(createParams);
        User savedUser = userRepository.save(newUser);

        log.info("User signed up successfully with ID: {}", savedUser.getId().value());

        return performLogin(savedUser, command.userAgent(), command.ipAddress());
    }

    @Transactional
    public AuthResult handle(LoginCommand command) {
        log.info("Login attempt for email: {}", command.email().value());

        User user = findUserByEmail(command.email());
        validateUserCanLogin(user);
        validatePassword(command.password(), user.getHashedPassword());

        user.recordLogin();
        User updatedUser = userRepository.save(user);

        enforceSessionLimit(user.getId());

        AuthResult authResult = performLogin(updatedUser, command.userAgent(), command.ipAddress());
        log.info("User logged in successfully: {}", user.getId().value());

        return authResult;
    }

    @Transactional
    public AuthResult handle(RefreshSessionCommand command) {
        log.info("Refreshing token");

        try {
            Claims refreshClaims = tokenProvider.validateRefreshToken(command.refreshToken());
            String userId = refreshClaims.getSubject();

            AuthSession session = authSessionRepository.findByRefreshToken(command.refreshToken())
                    .filter(AuthSession::isValid)
                    .orElseThrow(() -> new InvalidTokenException("Session not found or invalid"));

            if (!session.getUserId().value().equals(userId)) {
                throw new InvalidTokenException("User mismatch in refresh token");
            }

            User user = findUserById(UserId.from(userId));
            validateUserCanLogin(user);

            session.updateAccess();
            authSessionRepository.save(session);

            String newAccessToken = tokenProvider.generateAccessToken(user.getId().value(), user.getEmail().value(), user.getRoles());
            String currentRefreshToken = command.refreshToken();
            LocalDateTime accessTokenExpiresAt = LocalDateTime.now().plusMinutes(accessTokenExpiryMinutes);

            Date refreshExpiryDate = refreshClaims.getExpiration();
            LocalDateTime refreshTokenExpiresAt = refreshExpiryDate
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            log.info("Token refreshed successfully for user: {}", user.getId().value());
            return new AuthResult(user.getId(), newAccessToken, currentRefreshToken, accessTokenExpiresAt, refreshTokenExpiresAt, user);
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage(), e);
            if (e instanceof InvalidTokenException) {
                throw e;
            }
            throw new InvalidTokenException("Invalid refresh token: " + e.getMessage());
        }
    }

    @Transactional
    public void handle(LogoutCommand command) {
        log.info("Logging out session: {}", command.sessionId());

        AuthSession session = findSessionById(command.sessionId());
        session.revoke();
        authSessionRepository.save(session);

        log.info("Session logged out successfully: {}", command.sessionId());
    }

    @Transactional
    public void handle(LogoutAllCommand command) {
        log.info("Logging out all sessions for user: {}", command.userId());

        authSessionRepository.deleteAllByUserId(command.userId());
        log.info("All sessions logged out for user: {}", command.userId());
    }

    @Transactional
    public void handle(ChangePasswordCommand command) {
        log.info("Changing password for user: {}", command.userId());

        User user = findUserById(command.userId());
        validatePassword(command.currentPassword(), user.getHashedPassword());

        var passwordHashedStr = passwordEncoder.encode(command.newPassword().value());
        user.changePassword(new HashedPassword(passwordHashedStr));

        userRepository.save(user);

        // Logout all sessions for security
        authSessionRepository.deleteAllByUserId(user.getId());

        log.info("Password changed successfully for user: {}", command.userId());
    }

    @Transactional
    public User handle(CreateAdminCommand command) {
        log.info("Creating admin user with email: {}", command.email().value());

        validateEmailNotExists(command.email());

        var hashedPassword = new HashedPassword(passwordEncoder.encode(command.password().value()));
        var createParams = CreateUserParams.builder()
                .email(command.email())
                .phoneNumber(command.phoneNumber())
                .hashedPassword(hashedPassword)
                .name(command.name())
                .build();

        User adminUser = User.createAdmin(createParams);
        User savedAdmin = userRepository.save(adminUser);
        log.info("Admin user created successfully with ID: {}", savedAdmin.getId().value());

        return savedAdmin;
    }

    private User findUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId.value()));
    }

    private User findUserByEmail(Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email.value()));
    }

    private AuthSession findSessionById(String refreshToken) {
        return authSessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionNotFoundException(refreshToken));
    }

    private void validateEmailNotExists(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserValidationException("User with this email already exists");
        }
    }

    private void validateUserCanLogin(User user) {
        if (!user.canLogin()) {
            throw new AuthValidationException("User account can't login cause it's not active");
        }
    }

    private void validatePassword(PlainPassword password, HashedPassword hashedPassword) {
        if (!passwordEncoder.matches(password.value(), hashedPassword.value())) {
            throw new InvalidCredentialsException();
        }
    }

    private void enforceSessionLimit(UserId userId) {
        long activeSessions = authSessionRepository.countActiveSessionsByUserId(userId);
        if (activeSessions >= maxSessionsPerUser) {
            // Remove oldest sessions
            List<AuthSession> sessions = authSessionRepository.findByUserId(userId);
            sessions.stream()
                    .sorted((s1, s2) -> s1.getLastAccessedAt().compareTo(s2.getLastAccessedAt()))
                    .limit(sessions.size() - maxSessionsPerUser + 1)
                    .forEach(authSessionRepository::delete);
        }
    }

    private AuthResult performLogin(User user, String userAgent, String ipAddress) {
        String refreshToken = tokenProvider.generateRefreshToken(user.getId().value());

        AuthSession session = AuthSession.create(
                user.getId(),
                refreshToken,
                refreshTokenExpiryDays,
                userAgent,
                ipAddress
        );

        AuthSession savedSession = authSessionRepository.save(session);

        String accessToken = tokenProvider.generateAccessToken(user.getId().value(), user.getEmail().value(), user.getRoles());

        LocalDateTime accessTokenExpiresAt = LocalDateTime.now().plusMinutes(accessTokenExpiryMinutes);
        LocalDateTime refreshTokenExpiresAt = savedSession.getExpiresAt();

        return new AuthResult(user.getId(), accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt, user);
    }

    @Transactional(readOnly = true)
    public List<AuthSession> handle(GetUserSessionsQuery query) {
        log.debug("Fetching sessions for user: {}", query.userId());

        return authSessionRepository.findByUserId(query.userId());
    }

    @Transactional(readOnly = true)
    public TokenValidationResult handle(ValidateTokenQuery query) {
        log.debug("Validating token");

        try {
            Claims claims = tokenProvider.validateAccessToken(query.accessToken());
            return new TokenValidationResult(true, "Token is valid", claims);
        } catch (Exception e) {
            return new TokenValidationResult(false, e.getMessage(), null);
        }
    }

    @Transactional(readOnly = true)
    public TokenValidationResult validateRefreshToken(String refreshToken) {
        log.debug("Validating refresh token");
        try {
            boolean sessionExists = authSessionRepository.findByRefreshToken(refreshToken)
                    .filter(AuthSession::isValid)
                    .isPresent();

            Claims claims = tokenProvider.validateRefreshToken(refreshToken);


            if (!sessionExists) {
                return new TokenValidationResult(false, "Session not found or invalid", null);
            }

            return new TokenValidationResult(true, "Refresh token is valid", claims);
        } catch (Exception e) {
            return new TokenValidationResult(false, e.getMessage(), null);
        }
    }

    @Transactional(readOnly = true)
    public Claims getAccessTokenClaims(String accessToken) {
        try {
            return tokenProvider.validateAccessToken(accessToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid access token: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Claims getRefreshTokenClaims(String refreshToken) {
        try {
            return tokenProvider.validateRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid refresh token: " + e.getMessage());
        }
    }

    public record TokenValidationResult(boolean valid, String message, Claims claims) {
    }
}