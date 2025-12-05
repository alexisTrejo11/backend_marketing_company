package at.backend.MarketingCompany.account.user.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;

public record GetUsersByRoleQuery(Role role, Pageable pageable) {
}
