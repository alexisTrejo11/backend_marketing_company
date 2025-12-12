package at.backend.MarketingCompany.account.auth.adapters.inbound.mapper;

import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.TokenValidationResponse;
import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandler;
import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandlerHandler;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthResult;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.SessionResponse;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class AuthResponseMapper {
    
    public AuthResponse toAuthResponse(AuthResult authResult) {
        if (authResult == null) return null;
        
        return new AuthResponse(
            authResult.getAccessToken(),
            authResult.getRefreshToken(),
            authResult.getAccessTokenExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
            authResult.getRefreshTokenExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
            toUserResponse(authResult.getUser())
        );
    }
    
    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        var firstName = user.getPersonalData().name() != null ? user.getPersonalData().name().firstName() : null;
        var lastName = user.getPersonalData().name() != null ? user.getPersonalData().name().lastName() : null;
        return new UserResponse(
            user.getId().value(),
            user.getEmail().value(),
            firstName,
            lastName,
            user.getPhoneNumber() != null ? user.getPhoneNumber().value() : null,
            user.getRoles(),
            user.getStatus(),
            user.getLastLoginAt(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
    
    public SessionResponse toSessionResponse(AuthSession session) {
        if (session == null) return null;
        
        return new SessionResponse(
            session.getSessionId().value(),
            session.getCreatedAt().atOffset(OffsetDateTime.now().getOffset()),
            session.getExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
            session.getLastAccessedAt().atOffset(OffsetDateTime.now().getOffset()),
            session.getUserAgent(),
            session.getIpAddress(),
            session.isValid()
        );
    }

    public TokenValidationResponse toTokenValidationResponse(
            AuthCommandHandler.TokenValidationResult result) {
        return new TokenValidationResponse(result.valid(), result.message(), result.claims());
    }

}