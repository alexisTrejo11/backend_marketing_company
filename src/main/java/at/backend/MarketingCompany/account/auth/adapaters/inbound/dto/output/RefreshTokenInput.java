package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output;

import at.backend.MarketingCompany.account.auth.application.commands.RefreshTokenCommand;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenInput(
    @NotBlank String refreshToken
) {
    public RefreshTokenCommand toCommand(String userAgent, String ipAddress) {
        return new RefreshTokenCommand(
            this.refreshToken,
            userAgent,
            ipAddress
        );
    }
}