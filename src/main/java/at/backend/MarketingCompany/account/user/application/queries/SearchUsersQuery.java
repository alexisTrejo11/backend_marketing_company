package at.backend.MarketingCompany.account.user.application.queries;

import org.springframework.data.domain.Pageable;

public record SearchUsersQuery(Pageable pageable) {
}
