package at.backend.MarketingCompany.account.auth.adapters.inbound.mapper;

import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.TokenValidationResponse;
import at.backend.MarketingCompany.account.auth.core.application.AuthCommandHandler;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthResult;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper.UserResponseMapper;
import lombok.RequiredArgsConstructor;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.SessionResponse;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class AuthResponseMapper {
  private final UserResponseMapper userMapper;

  public AuthResponse toAuthResponse(AuthResult authResult) {
    if (authResult == null)
      return null;

    return new AuthResponse(
        authResult.getAccessToken(),
        authResult.getRefreshToken(),
        authResult.getAccessTokenExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
        authResult.getRefreshTokenExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
        userMapper.toUserResponse(authResult.getUser()));
  }

  public SessionResponse toSessionResponse(AuthSession session) {
    if (session == null)
      return null;

    return new SessionResponse(
        session.getSessionId().value(),
        session.getCreatedAt().atOffset(OffsetDateTime.now().getOffset()),
        session.getExpiresAt().atOffset(OffsetDateTime.now().getOffset()),
        session.getLastAccessedAt().atOffset(OffsetDateTime.now().getOffset()),
        session.getUserAgent(),
        session.getIpAddress(),
        session.isValid());
  }

  public TokenValidationResponse toTokenValidationResponse(
      AuthCommandHandler.TokenValidationResult result) {
    return new TokenValidationResponse(result.valid(), result.message(), result.claims());
  }

}
