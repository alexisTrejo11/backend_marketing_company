package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserStatus;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Set;

@Builder
public record UserResponse(
    String id,
    String email,
    String phoneNumber,
    Set<Role> roles,
    UserStatus status,
    PersonalDataResponse personalData,
    OffsetDateTime lastLoginAt,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {

}
