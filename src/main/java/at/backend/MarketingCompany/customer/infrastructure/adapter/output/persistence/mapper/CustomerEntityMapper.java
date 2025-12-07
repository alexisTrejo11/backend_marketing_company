package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.Customer;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerReconstructParams;
import at.backend.MarketingCompany.customer.domain.valueobject.SocialMediaHandles;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerEntity;

@Component
public class CustomerEntityMapper {
  public CustomerEntity toEntity(Customer customer) {
    CustomerEntity entity = new CustomerEntity();
    entity.setId(customer.getId().value());
    entity.setFirstName(customer.getPersonalInfo().firstName());
    entity.setLastName(customer.getPersonalInfo().lastName());
    if (customer.getContactDetails() != null) {
      entity.setEmail(customer.getContactDetails().email().value());
      entity.setPhone(customer.getContactDetails().phone().value());
    }
    if (customer.getBusinessProfile() != null) {
      entity.setCompany(customer.getBusinessProfile().company());
      entity.setBrandColors(customer.getBusinessProfile().brandColors());
      entity.setTargetMarket(customer.getBusinessProfile().targetMarket());
      entity.setIndustry(customer.getBusinessProfile().industry());
      entity.setBrandVoice(customer.getBusinessProfile().brandVoice());
      entity.setSocialMediaHandles(customer.getBusinessProfile().socialMediaHandles().toString());
      if (customer.getBusinessProfile().hasCompetitors()) {
        entity.setCompetitorUrls(customer.getBusinessProfile().competitorUrls());
      }
    }
    entity.setCreatedAt(customer.getCreatedAt());
    entity.setUpdatedAt(customer.getUpdatedAt());
    entity.setDeletedAt(customer.getDeletedAt());
    entity.setVersion(customer.getVersion());
    return entity;
  }

  public Customer toDomain(CustomerEntity entity) {
    var params = CustomerReconstructParams.builder()
        .id(new CustomerId(entity.getId()))
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .deletedAt(entity.getDeletedAt())
        .version(entity.getVersion());

    if (entity.getFirstName() != null && entity.getLastName() != null) {
      params = params.personalInfo(new PersonName(entity.getFirstName(), entity.getLastName()));
    }

    params = params.contactDetails(
        new ContactDetails(
            entity.getEmail() != null ? new Email(entity.getEmail()) : null,
            entity.getPhone() != null ? new PhoneNumber(entity.getPhone()) : null));

    params = params.businessProfile(BusinessProfile.builder()
        .company(entity.getCompany())
        .industry(entity.getIndustry())
        .brandVoice(entity.getBrandVoice())
        .brandColors(entity.getBrandColors())
        .targetMarket(entity.getTargetMarket())
        .socialMediaHandles(SocialMediaHandles.fromString(entity.getSocialMediaHandles()))
        .build());

    params.interactionIds(entity.getInteractions() != null ? entity.getInteractions().stream()
        .map(interactionEntity -> new InteractionId(interactionEntity.getId()))
        .collect(Collectors.toSet()) : Collections.emptySet());

    params.opportunityIds(entity.getOpportunities() != null ? entity.getOpportunities().stream()
        .map(opportunityEntity -> new OpportunityId(opportunityEntity.getId()))
        .collect(Collectors.toSet()) : Collections.emptySet());

    return Customer.reconstruct(params.build());
  }
}
