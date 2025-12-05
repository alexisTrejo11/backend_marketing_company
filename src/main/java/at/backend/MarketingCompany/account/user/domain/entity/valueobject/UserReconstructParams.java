package at.backend.MarketingCompany.account.user.domain.entity.valueobject;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserReconstructParams(
    UserId id,
    Email email,
    PhoneNumber phoneNumber,
    HashedPassword hashedPassword,
    PersonName name,
    Set<Role> roles,
    UserStatus status,
    LocalDateTime lastLoginAt,
    LocalDateTime passwordChangedAt,
    Integer version,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}
