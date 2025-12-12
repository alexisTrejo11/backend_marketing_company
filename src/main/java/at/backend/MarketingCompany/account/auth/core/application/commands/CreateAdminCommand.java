package at.backend.MarketingCompany.account.auth.core.application.commands;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;

public record CreateAdminCommand(
    Email email,
    PlainPassword password,
    PersonName name,
    PhoneNumber phoneNumber) {
}
