package at.backend.MarketingCompany.account.auth.application.commands;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record LogoutAllCommand(
    UserId userId) {
}
