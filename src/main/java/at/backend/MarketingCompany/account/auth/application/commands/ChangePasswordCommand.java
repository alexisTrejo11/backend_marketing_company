package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.PlainPassword;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record ChangePasswordCommand(
    UserId userId,
    PlainPassword currentPassword,
    PlainPassword newPassword) {
}
