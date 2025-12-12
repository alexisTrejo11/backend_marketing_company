package at.backend.MarketingCompany.account.user.core.application.queries;

import org.springframework.data.domain.Pageable;

public record SearchUsersQuery(Pageable pageable) {
}
