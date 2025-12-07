package at.backend.MarketingCompany.customer.domain.valueobject;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import lombok.Builder;

@Builder
public record CustomerCreateParams(
    PersonName personalInfo,
    ContactDetails contactDetails,
    BusinessProfile businessProfile,
    CustomerStatus status) {
}
