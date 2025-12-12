package at.backend.MarketingCompany.account.user.adapters.outbound.persistence;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.*;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public UserEntity toEntity(User userDomain) {
        if (userDomain == null)
            throw new IllegalArgumentException("User domain object cannot be null");

        UserEntity entity = new UserEntity();
        if (userDomain.getId() != null)
            entity.setId(userDomain.getId().value());
        entity.setEmail(userDomain.getEmail() != null ? userDomain.getEmail().value() : null);
        entity.setHashedPassword(userDomain.getHashedPassword() != null ? userDomain.getHashedPassword().value() : null);
        entity.setStatus(userDomain.getStatus());
        entity.setLastLoginAt(userDomain.getLastLoginAt());
        entity.setPasswordChangedAt(userDomain.getPasswordChangedAt());
        entity.setPhoneNumber(userDomain.getPhoneNumber() != null ? userDomain.getPhoneNumber().value() : null);

        if (userDomain.getPersonalData() != null) {
            entity.setDateOfBirth(userDomain.getPersonalData().dateOfBirth());
            entity.setGender(userDomain.getPersonalData().gender());

            if (userDomain.getPersonalData().name() != null) {
                entity.setFirstName(userDomain.getPersonalData().name().firstName());
                entity.setLastName(userDomain.getPersonalData().name().lastName());
            }
        }
        entity.setRoles(userDomain.getRoles());

        // Audit fields
        entity.setCreatedAt(userDomain.getCreatedAt());
        entity.setUpdatedAt(userDomain.getUpdatedAt());
        entity.setDeletedAt(userDomain.getDeletedAt());
        entity.setVersion(userDomain.getVersion());

        return entity;
    }


    public User toDomain(UserEntity userEntity) {
        if (userEntity == null)
            return null;

        var personalData = new PersonalData(
                PersonName.from(userEntity.getFirstName(), userEntity.getLastName()),
                userEntity.getDateOfBirth(),
                userEntity.getGender()
        );

        return User.reconstruct(UserReconstructParams.builder()
                .id(userEntity.getId() != null ? UserId.from(userEntity.getId()) : null)
                .email(userEntity.getEmail() != null ? Email.from(userEntity.getEmail()) : null)
                .personalData(personalData)
                .hashedPassword(userEntity.getHashedPassword() != null ? HashedPassword.from(userEntity.getHashedPassword()) : null)
                .lastLoginAt(userEntity.getLastLoginAt())
                .passwordChangedAt(userEntity.getPasswordChangedAt())
                .roles(userEntity.getRoles())
                .status(userEntity.getStatus())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .deletedAt(userEntity.getDeletedAt())
                .version(userEntity.getVersion())
                .build());

    }
}