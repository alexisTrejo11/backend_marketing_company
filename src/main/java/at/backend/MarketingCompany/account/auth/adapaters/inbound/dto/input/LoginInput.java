package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.input;

import at.backend.MarketingCompany.account.auth.application.commands.LoginCommand;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.PlainPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginInput(
    @NotBlank @Email String email,
    @NotBlank String password
) {
    public LoginCommand toCommand(String userAgent, String ipAddress) {
        return new LoginCommand(
            new at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email(this.email),
            new PlainPassword(this.password),
            userAgent,
            ipAddress
        );
    }
}