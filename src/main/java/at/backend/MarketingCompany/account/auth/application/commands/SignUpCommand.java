package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;

public record SignUpCommand(
    Email email,
    PlainPassword password,
    PersonName name,
    PhoneNumber phoneNumber,
    String userAgent,
    String ipAddress) {
}
