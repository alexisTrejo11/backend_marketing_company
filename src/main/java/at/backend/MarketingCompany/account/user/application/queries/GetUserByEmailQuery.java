package at.backend.MarketingCompany.account.user.application.queries;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;

public record GetUserByEmailQuery(Email email) {
    public static GetUserByEmailQuery from(String email) {
        return new GetUserByEmailQuery(
            new Email(email)
        );
    }
}
