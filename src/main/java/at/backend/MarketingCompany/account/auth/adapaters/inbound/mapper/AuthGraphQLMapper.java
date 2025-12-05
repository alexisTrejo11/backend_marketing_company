package at.backend.MarketingCompany.account.auth.adapaters.inbound.mapper;

import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.TokenValidationResponse;
import at.backend.MarketingCompany.account.auth.application.AuthApplicationService;
import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthResult;
import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.SessionResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthGraphQLMapper {
    
    public AuthResponse toAuthResponse(AuthResult authResult) {
        if (authResult == null) return null;
        
        return new AuthResponse(
            authResult.getAccessToken(),
            authResult.getRefreshToken(),
            authResult.getAccessTokenExpiresAt(),
            authResult.getRefreshTokenExpiresAt(),
            toUserResponse(authResult.getUser())
        );
    }
    
    public UserResponse toUserResponse(User user) {
        if (user == null) return null;
        
        return new UserResponse(
            user.getId().value(),
            user.getEmail().value(),
            user.getName().firstName(),
            user.getName().lastName(),
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
            session.getCreatedAt(),
            session.getExpiresAt(),
            session.getLastAccessedAt(),
            session.getUserAgent(),
            session.getIpAddress(),
            session.isValid()
        );
    }

    public TokenValidationResponse toTokenValidationResponse(
            AuthApplicationService.TokenValidationResult result) {
        return new TokenValidationResponse(result.valid(), result.message(), result.claims());
    }

}