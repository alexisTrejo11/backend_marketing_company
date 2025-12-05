package at.backend.MarketingCompany.account.auth.application.query;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record GetUserSessionsQuery(UserId userId) {
}
