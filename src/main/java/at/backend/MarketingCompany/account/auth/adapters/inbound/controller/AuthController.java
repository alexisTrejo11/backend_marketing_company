package at.backend.MarketingCompany.account.auth.adapters.inbound.controller;

import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.input.LoginInput;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.input.SignUpInput;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.RefreshTokenInput;
import at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapters.inbound.mapper.AuthResponseMapper;
import at.backend.MarketingCompany.account.auth.core.application.commands.*;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthResult;
import at.backend.MarketingCompany.account.auth.core.port.input.AuthCommandService;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
  private final AuthCommandService authService;
  private final AuthResponseMapper authResponseMapper;

  @MutationMapping
  @GraphQLRateLimit("sensitive")
  public AuthResponse signUp(
      @Valid @Argument SignUpInput input,
      @ContextValue(name = "userAgent") String userAgent,
      @ContextValue(name = "clientIp") String clientIp) {
    log.info("Sign up request for email: {} from ip: {} ua: {}", input.email(), clientIp, userAgent);
    SignUpCommand signUpCommand = input.toCommand(
        userAgent != null ? userAgent : "GraphQL-Client",
        clientIp != null ? clientIp : "unknown");

    AuthResult authResult = authService.handleSignUp(signUpCommand);
    return authResponseMapper.toAuthResponse(authResult);
  }

  @MutationMapping
  @GraphQLRateLimit("sensitive")
  public AuthResponse login(
      @Valid @Argument LoginInput input,
      @ContextValue(name = "userAgent") String userAgent,
      @ContextValue(name = "clientIp") String clientIp) {
    log.info("Login request for email: {}", input.email());
    LoginCommand command = input.toCommand(userAgent, clientIp);

    AuthResult result = authService.handleLogin(command);
    return authResponseMapper.toAuthResponse(result);
  }

  @MutationMapping
  @GraphQLRateLimit
  public AuthResponse refreshToken(
      @Valid @Argument RefreshTokenInput input,
      @ContextValue(name = "userAgent") String userAgent,
      @ContextValue(name = "clientIp") String clientIp) {
    log.info("Refresh token request");
    RefreshSessionCommand command = input.toCommand(userAgent, clientIp);

    AuthResult result = authService.handleRefreshToken(command);
    return authResponseMapper.toAuthResponse(result);
  }

  @MutationMapping
  @GraphQLRateLimit
  public Boolean logout(@Argument String refreshToken) {
    log.info("Logout request for session: {}", refreshToken.substring(0, 6) + "...");
    LogoutCommand command = LogoutCommand.from(refreshToken);

    authService.handleLogout(command);
    return true;
  }

  @MutationMapping
  @GraphQLRateLimit
  public Boolean logoutAll(@ContextValue(name = "userId") String userId) {
    log.info("Logout all request for user: {}", userId);
    LogoutAllCommand command = LogoutAllCommand.from(userId);

    authService.handleLogoutAll(command);
    return true;
  }
}
