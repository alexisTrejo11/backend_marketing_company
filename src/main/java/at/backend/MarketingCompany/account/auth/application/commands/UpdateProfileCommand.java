package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;

public record UpdateProfileCommand(
    String userId,
    PersonName name,
    PhoneNumber phoneNumber) {
}
