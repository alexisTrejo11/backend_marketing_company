package at.backend.MarketingCompany.customer.domain;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.excpetions.CustomerDomainException;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCreateParams;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerReconstructParams;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerStatus;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Customer extends BaseDomainEntity<CustomerId> {
  private PersonName personalInfo;
  private ContactDetails contactDetails;
  private BusinessProfile businessProfile;
  private CustomerStatus status;
  private final Set<OpportunityId> opportunities = new HashSet<>();
  private final Set<InteractionId> interactions = new HashSet<>();

  private Customer() {
  }

  public void activate() {
    if (this.status == CustomerStatus.BLOCKED) {
      throw new CustomerDomainException("Cannot activate a blocked customer");
    }
    this.status = CustomerStatus.ACTIVE;
  }

  public void block() {
    this.status = CustomerStatus.BLOCKED;
  }

  public boolean isBlocked() {
    return this.status == CustomerStatus.BLOCKED;
  }

  public void updateContactDetails(ContactDetails newDetails) {
    validateContactDetails(newDetails);
    this.contactDetails = newDetails;
  }

  public void validate() {
    if (personalInfo == null || personalInfo.firstName() == null || personalInfo.firstName().isBlank()) {
      throw new CustomerDomainException("First name is required");
    }
    if (personalInfo.lastName().isBlank()) {
      throw new CustomerDomainException("Last name is required");
    }
    if (contactDetails != null) {
      if (contactDetails.email() == null && contactDetails.phone() == null) {
        throw new CustomerDomainException("At least one contact method (email or phone) is required");
      }
    }
    if (businessProfile != null) {
      if (businessProfile.company() == null || businessProfile.company().isBlank()) {
        throw new CustomerDomainException("Company name is required in business profile");
      }
      if (businessProfile.brandColors() != null) {
        validateBrandColors(businessProfile.brandColors());
      }
    }
  }

  public static Customer create(CustomerCreateParams params) {
    Customer customer = new Customer();
    customer.personalInfo = params.personalInfo();
    customer.contactDetails = params.contactDetails();
    customer.businessProfile = params.businessProfile();
    customer.status = params.status() != null ? params.status() : CustomerStatus.INACTIVE;
    customer.validate();
    return customer;
  }

  public static Customer reconstruct(CustomerReconstructParams params) {
    Customer customer = new Customer();
    if (params == null)
      return customer;

    customer.id = params.id();
    customer.personalInfo = params.personalInfo();
    customer.contactDetails = params.contactDetails();
    customer.businessProfile = params.businessProfile();
    customer.createdAt = params.createdAt();
    customer.updatedAt = params.updatedAt();
    customer.deletedAt = params.deletedAt();
    customer.version = params.version();
    return customer;
  }

  private void validateContactDetails(ContactDetails details) {
    if (details == null) {
      throw new CustomerDomainException("Contact details cannot be null");
    }
    if (details.email() == null && details.phone() == null) {
      throw new CustomerDomainException("At least one contact method (email or phone) is required");
    }
  }

  public void deactivate() {
    this.status = CustomerStatus.INACTIVE;
  }

  public boolean hasSocialMediaHandles() {
    if (this.businessProfile == null || this.businessProfile.socialMediaHandles() == null)
      return false;

    return this.businessProfile.socialMediaHandles().hasHandles();
  }

  public boolean hasCompetitors() {
    if (this.businessProfile == null)
      return false;

    return this.businessProfile.hasCompetitors();
  }

  public void updateBusinessProfile(BusinessProfile newProfile) {
    validateBusinessProfile(newProfile);
    this.businessProfile = newProfile;
  }

  private void validateBusinessProfile(BusinessProfile profile) {
    if (profile == null) {
      throw new CustomerDomainException("Business profile cannot be null");
    }
    if (profile.company() == null || profile.company().isBlank()) {
      throw new CustomerDomainException("Company name is required in business profile");
    }
    if (profile.brandColors() != null) {
      validateBrandColors(profile.brandColors());
    }
  }

  private void validateBrandColors(String brandColors) {
    String[] colors = brandColors.split(",");
    for (String color : colors) {
      if (!color.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
        throw new CustomerDomainException("Invalid brand color format: " + color);
      }
    }
  }

  public void updatePersonalInfo(PersonName newPersonalInfo) {
    if (newPersonalInfo == null) {
      throw new CustomerDomainException("Personal info cannot be null");
    }
    if (newPersonalInfo.firstName() == null || newPersonalInfo.firstName().isBlank()) {
      throw new CustomerDomainException("First name is required");
    }
    if (newPersonalInfo.lastName() == null || newPersonalInfo.lastName().isBlank()) {
      throw new CustomerDomainException("Last name is required");
    }
    this.personalInfo = newPersonalInfo;
  }

  public void addOpportunity(OpportunityId opportunityId) {
    if (opportunityId == null) {
      throw new CustomerDomainException("Opportunity ID cannot be null");
    }
    if (opportunities.contains(opportunityId)) {
      throw new CustomerDomainException("Opportunity already exists for this customer");
    }
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot add opportunities to a blocked customer");
    }
    this.opportunities.add(opportunityId);
  }

  public void addInteraction(InteractionId interactionId) {
    if (interactionId == null) {
      throw new CustomerDomainException("Interaction ID cannot be null");
    }
    if (interactions.contains(interactionId)) {
      throw new CustomerDomainException("Interaction already exists for this customer");
    }
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot add interactions to a blocked customer");
    }
    this.interactions.add(interactionId);
  }

  public boolean isHighValueCustomer() {
    return opportunities.size() > 5;
  }

  public boolean matchesTargetMarket(String targetMarket) {
    if (businessProfile == null || businessProfile.targetMarket() == null) {
      return false;
    }
    return businessProfile.targetMarket().equalsIgnoreCase(targetMarket);
  }
}
