package at.backend.MarketingCompany.account.user.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;

public record GetUsersByRoleQuery(Role role, Pageable pageable) {
}
