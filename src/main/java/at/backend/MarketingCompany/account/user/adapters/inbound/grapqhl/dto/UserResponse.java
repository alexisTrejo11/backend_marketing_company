package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
    String id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    Set<Role> roles,
    UserStatus status,
    LocalDateTime lastLoginAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}


