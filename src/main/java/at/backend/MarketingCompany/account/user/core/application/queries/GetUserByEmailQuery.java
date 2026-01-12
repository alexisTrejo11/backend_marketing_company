package at.backend.MarketingCompany.account.user.core.application.queries;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;

public record GetUserByEmailQuery(Email email) {
    public static GetUserByEmailQuery from(String email) {
        return new GetUserByEmailQuery(
            new Email(email)
        );
    }
}
