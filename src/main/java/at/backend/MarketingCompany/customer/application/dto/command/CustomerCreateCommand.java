package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCreateParams;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerStatus;
import lombok.Builder;

@Builder
public record CustomerCreateCommand(
    PersonName personalInfo,
    ContactDetails contactDetails,
    BusinessProfile businessProfile,
    CustomerStatus status) {

  public CustomerCreateParams toParams() {
    return CustomerCreateParams.builder()
        .personalInfo(this.personalInfo)
        .contactDetails(this.contactDetails)
        .businessProfile(this.businessProfile)
        .status(this.status)
        .build();
  }
}
