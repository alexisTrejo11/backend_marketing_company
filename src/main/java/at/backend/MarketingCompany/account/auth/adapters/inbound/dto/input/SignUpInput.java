package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.input;

import at.backend.MarketingCompany.account.auth.core.application.commands.SignUpCommand;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpInput(
    @NotBlank @Email String email,
    @NotBlank 
    String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String phoneNumber
) {

    public SignUpCommand toCommand(String userAgent, String ipAddress) {
        return new SignUpCommand(
            new at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email(this.email),
            new PlainPassword(this.password),
            new PersonName(this.firstName, this.lastName),
            this.phoneNumber != null ? new PhoneNumber(this.phoneNumber) : null,
            userAgent,
            ipAddress
        );
    }
}