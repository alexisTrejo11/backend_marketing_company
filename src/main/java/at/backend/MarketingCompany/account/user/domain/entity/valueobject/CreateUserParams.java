package at.backend.MarketingCompany.account.user.domain.entity.valueobject;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.HashedPassword;
import lombok.Builder;

@Builder
public record CreateUserParams(
    Email email,
    PhoneNumber phoneNumber,
    HashedPassword hashedPassword,
    PersonName name
) {}