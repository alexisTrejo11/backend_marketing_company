package at.backend.MarketingCompany.account.auth.adapaters.inbound.controller;

import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.input.LoginInput;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.input.SignUpInput;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.RefreshTokenInput;
import at.backend.MarketingCompany.account.auth.application.AuthApplicationService;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.mapper.AuthGraphQLMapper;
import at.backend.MarketingCompany.account.auth.application.commands.*;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthApplicationService authService;
    private final AuthGraphQLMapper authGraphQLMapper;


    @MutationMapping
    public AuthResponse signUp(@Valid @Argument SignUpInput input, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String clientIp = resolveClientIp(request);

        log.info("Sign up request for email: {} from ip: {} ua: {}", input.email(), clientIp, userAgent);

        var signUpCommand = input.toCommand(userAgent != null ? userAgent : "GraphQL-Client", clientIp);
        var authResult = authService.handle(signUpCommand);

        return authGraphQLMapper.toAuthResponse(authResult);
    }


    @MutationMapping
    public AuthResponse login(@Valid @Argument LoginInput input, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String clientIp = resolveClientIp(request);

        log.info("Login request for email: {}", input.email());

        var loginCommand = input.toCommand(userAgent, clientIp);
        var result = authService.handle(loginCommand);

        return authGraphQLMapper.toAuthResponse(result);
    }

    @MutationMapping
    public AuthResponse refreshToken(@Valid @Argument RefreshTokenInput input, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String clientIp = resolveClientIp(request);

        log.info("Refresh token request");
        var refreshTokenCommand = input.toCommand(userAgent, clientIp);

        var result = authService.handle(refreshTokenCommand);
        return authGraphQLMapper.toAuthResponse(result);
    }

    @MutationMapping
    public Boolean logout(@Argument String sessionId) {
        log.info("Logout request for session: {}", sessionId);

        var logoutCommand = LogoutCommand.from(sessionId);
        authService.handle(logoutCommand);

        return true;
    }

    @MutationMapping
    public Boolean logoutAll() {
        // TODO: User ID should come from security context
        UserId userId = getCurrentUserId();
        log.info("Logout all request for user: {}", userId);

        var logoutAllCommand = new LogoutAllCommand(userId);
        authService.handle(logoutAllCommand);

        return true;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private UserId getCurrentUserId() {
        return UserId.generate(); // Placeholder implementation
    }
}