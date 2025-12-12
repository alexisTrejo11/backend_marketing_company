package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output;

import at.backend.MarketingCompany.account.auth.application.commands.RefreshSessionCommand;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenInput(
    @NotBlank String refreshToken
) {
    public RefreshSessionCommand toCommand(String userAgent, String ipAddress) {
        return new RefreshSessionCommand(
            this.refreshToken,
            userAgent,
            ipAddress
        );
    }
}