package at.backend.MarketingCompany.account.user.core.application.queries;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

public record GetUserByIdQuery(UserId userId) {

    public static GetUserByIdQuery from(String userId) {
        return new GetUserByIdQuery(
						UserId.of(userId)
        );
    }
}
