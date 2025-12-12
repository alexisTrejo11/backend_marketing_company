package at.backend.MarketingCompany.account.user.core.domain.entity.valueobject;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.HashedPassword;
import lombok.Builder;

@Builder
public record CreateUserParams(
    Email email,
    PhoneNumber phoneNumber,
    HashedPassword hashedPassword,
    PersonalData personalData
) {}