package at.backend.MarketingCompany.customer.adapter.output.persistence.mapper;

import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.ContactPersonEntity;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.AnnualRevenue;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyName;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyProfile;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.core.domain.valueobject.ContactPerson;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyReconstructParams;
import at.backend.MarketingCompany.customer.core.domain.valueobject.Industry;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerCompanyEntityMapper {

  public CustomerCompanyEntity toEntity(CustomerCompany company) {
    if (company == null) {
      return null;
    }

    CustomerCompanyEntity entity = new CustomerCompanyEntity();

    entity.setId(company.getId().getValue());
    entity.setCreatedAt(company.getCreatedAt());
    entity.setUpdatedAt(company.getUpdatedAt());
    entity.setDeletedAt(company.getDeletedAt());
    entity.setVersion(company.getVersion());

    entity.setCompanyName(company.getCompanyName().value());
    entity.setWebsite(company.getCompanyProfile() != null ? extractWebsite(company) : null);
    entity.setFoundingYear(company.getCompanyProfile() != null ? company.getCompanyProfile().foundingYear() : null);

    if (company.getCompanyProfile() != null && company.getCompanyProfile().industry() != null) {
      entity.setIndustryCode(company.getCompanyProfile().industry().code());
      entity.setIndustryName(company.getCompanyProfile().industry().name());
      entity.setSector(company.getCompanyProfile().industry().sector());
    }

    if (company.getCompanyProfile() != null) {
      entity.setCompanySize(company.getCompanyProfile().size());
      entity.setEmployeeCount(extractEmployeeCount(company.getCompanyProfile().size()));

      if (company.getCompanyProfile().revenue() != null) {
        entity.setAnnualRevenueAmount(company.getCompanyProfile().revenue().amount());
        entity.setAnnualRevenueCurrency(company.getCompanyProfile().revenue().currency().getCurrencyCode());
        entity.setRevenueRange(company.getCompanyProfile().revenue().range());
      }
    }

    entity.setStatus(company.getStatus());
    entity.setIsStartup(company.isStartup());
    entity.setIsPublicCompany(company.getCompanyProfile() != null && company.getCompanyProfile().isPublicCompany());

    if (company.getCompanyProfile() != null) {
      entity.setTargetMarket(company.getCompanyProfile().targetMarket());
      entity.setMissionStatement(company.getCompanyProfile().missionStatement());
      entity.setKeyProducts(company.getCompanyProfile().keyProducts());
      entity.setCompetitorUrls(company.getCompanyProfile().competitorUrls());
    }

    if (!company.getContactPersons().isEmpty()) {
      Set<ContactPersonEntity> contactPersonEntities = company.getContactPersons().stream()
          .map(this::mapToContactPersonEntity)
          .collect(Collectors.toSet());

      for (ContactPersonEntity secondary : contactPersonEntities) {
        secondary.setCompany(entity);
      }
      entity.setContactPersons(contactPersonEntities);
    }

    return entity;
  }

  public CustomerCompany toDomain(CustomerCompanyEntity entity) {
    CompanyName companyName = new CompanyName(entity.getCompanyName());

    Industry industry = new Industry(
        entity.getIndustryCode(),
        entity.getIndustryName(),
        entity.getSector());

    CompanySize companySize = entity.getCompanySize();

    AnnualRevenue revenue = null;
    if (entity.getAnnualRevenueAmount() != null) {
      revenue = new AnnualRevenue(
          entity.getAnnualRevenueAmount(),
          Currency.getInstance(entity.getAnnualRevenueCurrency()),
          entity.getRevenueRange());
    }

    CompanyProfile companyProfile = CompanyProfile.builder()
        .industry(industry)
        .size(companySize)
        .revenue(revenue)
        .missionStatement(entity.getMissionStatement())
        .targetMarket(entity.getTargetMarket())
        .competitorUrls(entity.getCompetitorUrls())
        .keyProducts(entity.getKeyProducts())
        .foundingYear(entity.getFoundingYear())
        .build();

    Set<ContactPersonEntity> rawContactPersons = entity.getContactPersons();
    Set<ContactPerson> contactPersons = rawContactPersons != null
        ? rawContactPersons.stream()
            .map(this::mapToContactPerson)
            .collect(Collectors.toSet())
        : new HashSet<>();

    return CustomerCompany.reconstruct(CustomerCompanyReconstructParams.builder()
        .id(new CustomerCompanyId(entity.getId()))
        .companyName(companyName)
        .companyProfile(companyProfile)
        .status(entity.getStatus())
        .contactPersons(contactPersons)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .deletedAt(entity.getDeletedAt())
        .version(entity.getVersion())
        .build());
  }

  private ContactPerson mapToContactPerson(ContactPersonEntity entity) {
    return new ContactPerson(
        PersonName.from(entity.getFirstName(), entity.getLastName()),
        entity.getEmail() != null ? new Email(entity.getEmail()) : null,
        entity.getPhone() != null ? new PhoneNumber(entity.getPhone()) : null,
        entity.getPosition(),
        ContactPerson.Department.valueOf(entity.getDepartment()),
        entity.getIsDecisionMaker(),
        entity.getIsPrimaryContact());
  }

  private String extractWebsite(CustomerCompany company) {
    return ""; // Placeholder
  }

  private Integer extractEmployeeCount(CompanySize size) {
    return switch (size) {
      case MICRO -> 5;
      case SMALL -> 25;
      case MEDIUM -> 150;
      case LARGE -> 500;
      case ENTERPRISE -> 2500;
      default -> null;
    };
  }

  private ContactPersonEntity mapToContactPersonEntity(ContactPerson domain) {
    ContactPersonEntity entity = new ContactPersonEntity();
    entity.setFirstName(domain.name().firstName());
    entity.setLastName(domain.name().lastName());
    entity.setEmail(domain.email() != null ? domain.email().value() : null);
    entity.setPhone(domain.phone() != null ? domain.phone().value() : null);
    entity.setPosition(domain.position());
    entity.setDepartment(domain.department().name());
    entity.setIsDecisionMaker(domain.isDecisionMaker());
    entity.setIsPrimaryContact(domain.isPrimaryContact());

    return entity;
  }
}
