package at.backend.MarketingCompany.account.user.application.queries;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public record GetUserByIdQuery(UserId userId) {

    public static GetUserByIdQuery from(String userId) {
        return new GetUserByIdQuery(
            new UserId(userId)
        );
    }
}
