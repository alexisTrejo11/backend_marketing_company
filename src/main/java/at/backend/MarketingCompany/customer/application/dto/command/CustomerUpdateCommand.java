package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import lombok.Builder;

@Builder
public record CustomerUpdateCommand(
    CustomerId id,
    PersonName personalInfo,
    BusinessProfile businessProfile,
    ContactDetails contactDetails) {

  public CustomerUpdateCommand {
    if (id == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }
  }
}
