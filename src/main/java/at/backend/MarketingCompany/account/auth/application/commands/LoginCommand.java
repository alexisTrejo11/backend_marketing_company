package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;

public record LoginCommand(
    Email email,
    PlainPassword password,
    String userAgent,
    String ipAddress) {

}
