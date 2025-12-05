package at.backend.MarketingCompany.account.user.application.queries;

import org.springframework.data.domain.Pageable;

public record GetActiveUsersQuery(Pageable pageable) {

}
