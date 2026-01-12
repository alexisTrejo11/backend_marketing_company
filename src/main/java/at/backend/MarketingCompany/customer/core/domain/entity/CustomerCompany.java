package at.backend.MarketingCompany.customer.core.domain.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyContactPersonAddedEvent;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyContactPersonRemovedEvent;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyCreatedEvent;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyOpportunityAddedEvent;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyStatusChangedEvent;
import at.backend.MarketingCompany.customer.core.domain.events.CompanyUpgradedToEnterpriseEvent;
import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.AnnualRevenue;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyName;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyProfile;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.ContactPerson;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyReconstructParams;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // For Testing purposes only
public class CustomerCompany extends BaseDomainEntity<CustomerCompanyId> {
  private CompanyName companyName;
  private CompanyProfile companyProfile;
  private CompanyStatus status;
  private Set<ContactPerson> contactPersons;
  private Set<OpportunityId> opportunities;
  private Set<InteractionId> interactions;

  private CustomerCompany() {
    this.contactPersons = new HashSet<>();
    this.opportunities = new HashSet<>();
    this.interactions = new HashSet<>();
    this.status = CompanyStatus.PROSPECT;
    this.companyProfile = CompanyProfile.builder().build();
  }

  public CustomerCompany(CustomerCompanyId id) {
    super(id);
    this.contactPersons = new HashSet<>();
    this.opportunities = new HashSet<>();
    this.interactions = new HashSet<>();
    this.status = CompanyStatus.PROSPECT;
    this.companyProfile = CompanyProfile.builder().build();
  }

  public void validate() {
    if (companyName == null)
      throw new CustomerDomainException("Company name is required");
    if (contactPersons.isEmpty())
      throw new CustomerDomainException("At least one contact person is required");
    if (companyProfile == null)
      throw new CustomerDomainException("Company profile is required");

    validateCompanyProfile();
    validateContactPersons();
  }

  private void validateContactPersons() {
    Set<String> emails = new HashSet<>();
    for (ContactPerson contact : contactPersons) {
      String email = contact.email().value();
      if (emails.contains(email)) {
        throw new CustomerDomainException("Duplicate contact email: " + email);
      }
      emails.add(email);
    }
  }

  public void updateCompanyProfile(CompanyProfile newProfile) {
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot update profile of a blocked company");
    }
    this.companyProfile = newProfile;
  }

  public void updateCompanyName(CompanyName newName) {
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot update name of a blocked company");
    }
    this.companyName = newName;
  }

  public void deactivate(String reason) {
    if (this.status == CompanyStatus.BLOCKED) {
      throw new CustomerDomainException("Cannot deactivate a blocked company");
    }
    CompanyStatus oldStatus = this.status;
    this.status = CompanyStatus.INACTIVE;

    registerEvent(new CompanyStatusChangedEvent(
        this.id, oldStatus, this.status, reason));
  }

  public void activate() {
    if (this.status == CompanyStatus.BLOCKED) {
      throw new CustomerDomainException("Cannot activate a blocked company");
    }
    CompanyStatus oldStatus = this.status;
    this.status = CompanyStatus.ACTIVE;

    registerEvent(new CompanyStatusChangedEvent(
        this.id, oldStatus, this.status, "Manual activation"));
  }

  public void block(String reason) {
    CompanyStatus oldStatus = this.status;
    this.status = CompanyStatus.BLOCKED;

    registerEvent(new CompanyStatusChangedEvent(
        this.id, oldStatus, this.status, reason));
  }

  public static CustomerCompany create(CompanyName companyName, CompanyProfile companyProfile,
      Set<ContactPerson> contactPersons) {
    CustomerCompany company = new CustomerCompany(CustomerCompanyId.generate());
    company.companyName = companyName;
    company.companyProfile = companyProfile;
    company.status = CompanyStatus.PROSPECT;
    company.contactPersons = contactPersons;

    company.validate();

    company.registerEvent(new CompanyCreatedEvent(
        company.id,
        companyName,
        companyProfile.industry(),
        company.status));

    contactPersons.forEach(contact -> company.registerEvent(new CompanyContactPersonAddedEvent(
        company.id,
        contact.getFullName(),
        contact.position(),
        contact.email().value(),
        contact.isDecisionMaker())));
    return company;
  }

  public void upgradeToEnterprise(AnnualRevenue newRevenue) {
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot upgrade blocked company");
    }
    if (this.companyProfile == null) {
      throw new CustomerDomainException("Company profile is required");
    }

    CompanySize oldSize = this.companyProfile.size();
    CompanySize newSize = CompanySize.ENTERPRISE;

    this.companyProfile = CompanyProfile.builder()
        .industry(this.companyProfile.industry())
        .size(newSize)
        .revenue(newRevenue)
        .missionStatement(this.companyProfile.missionStatement())
        .targetMarket(this.companyProfile.targetMarket())
        .competitorUrls(this.companyProfile.competitorUrls())
        .socialMediaHandles(this.companyProfile.socialMediaHandles())
        .keyProducts(this.companyProfile.keyProducts())
        .foundingYear(this.companyProfile.foundingYear())
        .build();

    registerEvent(new CompanyUpgradedToEnterpriseEvent(
        this.id, oldSize, newSize, newRevenue));
  }

  public void addContactPerson(ContactPerson contactPerson) {
    if (contactPerson == null) {
      throw new CustomerDomainException("Contact person cannot be null");
    }
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot add contacts to a blocked company");
    }

    boolean alreadyExists = contactPersons.stream()
        .anyMatch(cp -> cp.email().equals(contactPerson.email()));

    if (alreadyExists) {
      throw new CustomerDomainException("Contact with email already exists: "
          + contactPerson.email().value());
    }

    this.contactPersons.add(contactPerson);

    registerEvent(new CompanyContactPersonAddedEvent(
        this.id,
        contactPerson.getFullName(),
        contactPerson.position(),
        contactPerson.email().value(),
        contactPerson.isDecisionMaker()));
  }

  public void addOpportunity(OpportunityId opportunityId, BigDecimal estimatedValue, String opportunityType) {
    if (opportunityId == null) {
      throw new CustomerDomainException("Opportunity ID cannot be null");
    }
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot add opportunities to a blocked company");
    }
    this.opportunities.add(opportunityId);
    registerEvent(new CompanyOpportunityAddedEvent(this.id, opportunityId, estimatedValue, opportunityType));
  }

  public boolean isHighValueClient() {
    return companyProfile != null
        && companyProfile.revenue() != null
        && companyProfile.revenue().isHighValue();
  }

  public boolean isStartup() {
    return companyProfile != null && companyProfile.isStartup();
  }

  public boolean isEnterprise() {
    return companyProfile != null
        && companyProfile.size() != null
        && companyProfile.size().isEnterprise();
  }

  public boolean isInIndustry(String industryCode) {
    return companyProfile != null
        && companyProfile.industry() != null
        && companyProfile.industry().code().equalsIgnoreCase(industryCode);
  }

  public boolean hasCompanyProfile() {
    return companyProfile != null;
  }

  public boolean removeContactPerson(Email email) {
    if (isBlocked()) {
      throw new CustomerDomainException("Cannot remove contacts from blocked company");
    }

    boolean removed = contactPersons.removeIf(cp -> cp.email().equals(email));

    if (removed && contactPersons.isEmpty()) {
      throw new CustomerDomainException("Company must have at least one contact person");
    }

    if (removed) {
      registerEvent(new CompanyContactPersonRemovedEvent(this.id, email.value()));
    }

    return removed;
  }

  private void validateCompanyProfile() {
    if (companyProfile.industry() == null) {
      throw new CustomerDomainException("Industry is required");
    }
  }

  public static CustomerCompany reconstruct(CustomerCompanyReconstructParams params) {
    if (params == null) {
      return null;
    }

    CustomerCompany company = new CustomerCompany();
    company.id = params.id();
    company.companyName = params.companyName();
    company.companyProfile = params.companyProfile();
    company.status = params.status();
    company.contactPersons.addAll(params.contactPersons() != null ? params.contactPersons() : new HashSet<>());
    company.opportunities.addAll(params.opportunities() != null ? params.opportunities() : new HashSet<>());
    company.interactions.addAll(params.interactions() != null ? params.interactions() : new HashSet<>());
    company.createdAt = params.createdAt();
    company.updatedAt = params.updatedAt();
    company.deletedAt = params.deletedAt();
    company.version = params.version();

    return company;
  }

  public boolean isBlocked() {
    return status == CompanyStatus.BLOCKED;
  }

  public boolean isActive() {
    return status == CompanyStatus.ACTIVE;
  }

  public boolean isProspect() {
    return status == CompanyStatus.PROSPECT;
  }

  public Set<Email> getDecisionMakersEmails() {
    return contactPersons.stream()
        .filter(ContactPerson::isDecisionMaker)
        .map(ContactPerson::email)
        .collect(java.util.stream.Collectors.toSet());
  }
}
