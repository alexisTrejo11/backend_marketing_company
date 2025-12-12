package at.backend.MarketingCompany.account.auth.adapaters.inbound.controller;

import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.input.LoginInput;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.input.SignUpInput;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.RefreshTokenInput;
import at.backend.MarketingCompany.account.auth.application.AuthApplicationService;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output.AuthResponse;
import at.backend.MarketingCompany.account.auth.adapaters.inbound.mapper.AuthGraphQLMapper;
import at.backend.MarketingCompany.account.auth.application.commands.*;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;
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
    private final AuthApplicationService authService;
    private final AuthGraphQLMapper authGraphQLMapper;

    @MutationMapping
    public AuthResponse signUp(
            @Valid @Argument SignUpInput input,
            @ContextValue(name = "userAgent") String userAgent,
            @ContextValue(name = "clientIp") String clientIp
    ) {
        log.info("Sign up request for email: {} from ip: {} ua: {}",
                input.email(), clientIp, userAgent);

        var signUpCommand = input.toCommand(
                userAgent != null ? userAgent : "GraphQL-Client",
                clientIp != null ? clientIp : "unknown"
        );
        var authResult = authService.handle(signUpCommand);

        return authGraphQLMapper.toAuthResponse(authResult);
    }

    @MutationMapping
    public AuthResponse login(
            @Valid @Argument LoginInput input,
            @ContextValue(name = "userAgent") String userAgent,
            @ContextValue(name = "clientIp") String clientIp
    ) {
        log.info("Login request for email: {}", input.email());

        var loginCommand = input.toCommand(userAgent, clientIp);
        var result = authService.handle(loginCommand);

        return authGraphQLMapper.toAuthResponse(result);
    }

    @MutationMapping
    public AuthResponse refreshToken(
            @Valid @Argument RefreshTokenInput input,
            @ContextValue(name = "userAgent") String userAgent,
            @ContextValue(name = "clientIp") String clientIp
    ) {
        log.info("Refresh token request");
        var refreshTokenCommand = input.toCommand(userAgent, clientIp);

        var result = authService.handle(refreshTokenCommand);
        return authGraphQLMapper.toAuthResponse(result);
    }

    @MutationMapping
    public Boolean logout(@Argument String refreshToken) {
        log.info("Logout request for session: {}", refreshToken.substring(0, 6) + "...");

        var logoutCommand = LogoutCommand.from(refreshToken);
        authService.handle(logoutCommand);

        return true;
    }

    @MutationMapping
    public Boolean logoutAll(
            @ContextValue(name = "userId") String userIdStr
    ) {
        log.info("Logout all request for user: {}", userIdStr);

        UserId userId = UserId.from(userIdStr);
        var logoutAllCommand = new LogoutAllCommand(userId);
        authService.handle(logoutAllCommand);

        return true;
    }
}