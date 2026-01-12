package at.backend.MarketingCompany.account.auth.core.application.commands;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;

public record LoginCommand(
    Email email,
    PlainPassword password,
    String userAgent,
    String ipAddress) {

}
