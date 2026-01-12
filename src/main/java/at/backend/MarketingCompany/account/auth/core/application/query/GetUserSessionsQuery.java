package at.backend.MarketingCompany.account.auth.core.application.query;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record GetUserSessionsQuery(UserId userId) {
}
