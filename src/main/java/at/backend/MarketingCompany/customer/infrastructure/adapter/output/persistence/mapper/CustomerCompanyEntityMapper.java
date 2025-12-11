package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.mapper;


import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerCompanyEntityMapper {

    public CustomerCompanyEntity toEntity(CustomerCompany company) {
        if (company == null) {
            return null;
        }

        CustomerCompanyEntity entity = new CustomerCompanyEntity();

        entity.setId(company.getId().value());
        entity.setCreatedAt(company.getCreatedAt());
        entity.setUpdatedAt(company.getUpdatedAt());
        entity.setDeletedAt(company.getDeletedAt());
        entity.setVersion(company.getVersion());

        entity.setCompanyName(company.getCompanyName().value());
        entity.setTaxId(company.getBillingInfo() != null ? company.getBillingInfo().taxId() : null);
        entity.setWebsite(company.getCompanyProfile() != null ? extractWebsite(company) : null);
        entity.setFoundingYear(company.getCompanyProfile() != null ?
                company.getCompanyProfile().foundingYear() : null);

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
            ContactPerson primary = company.getContactPersons().stream()
                    .filter(ContactPerson::isPrimaryContact)
                    .findFirst()
                    .orElse(company.getContactPersons().iterator().next());

            ContactPersonEmbeddable primaryContact = getContactPersonEmbeddable(primary);

            entity.setPrimaryContact(primaryContact);


            Set<ContactPersonEntity> secondaryContacts = company.getContactPersons().stream()
                    .filter(cp -> !cp.isPrimaryContact())
                    .map(this::mapToContactPersonEntity)
                    .collect(Collectors.toSet());

            for (ContactPersonEntity secondary : secondaryContacts) {
                secondary.setCompany(entity);
            }
            entity.setContactPersons(secondaryContacts);
        }

        if (company.getContractDetails() != null) {
            ContractDetailsEmbeddable contract = getContractDetailsEmbeddable(company);

            entity.setContractDetails(contract);
        }

        if (company.getBillingInfo() != null) {
            BillingInformationEmbeddable billing = new BillingInformationEmbeddable();

            billing.setBillingEmail(company.getBillingInfo().billingEmail().value());
            billing.setPreferredPaymentMethod(company.getBillingInfo().preferredPaymentMethod().name());
            billing.setBillingAddress(company.getBillingInfo().billingAddress());
            billing.setApprovedCredit(company.getBillingInfo().approvedCredit());

            entity.setBillingInformation(billing);
        }

        if (company.getCompanyProfile() != null && company.getCompanyProfile().socialMediaHandles() != null) {
            SocialMediaEmbeddable socialMedia =  SocialMediaEmbeddable.fromDomain(company.getCompanyProfile().socialMediaHandles());
            entity.setSocialMedia(socialMedia);

        }

        return entity;
    }



    private static @NotNull ContractDetailsEmbeddable getContractDetailsEmbeddable(CustomerCompany company) {
        ContractDetailsEmbeddable contract = new ContractDetailsEmbeddable();
        contract.setContractId(company.getContractDetails().contractId());
        contract.setContractStartDate(company.getContractDetails().startDate());
        contract.setContractEndDate(company.getContractDetails().endDate());
        contract.setMonthlyFee(company.getContractDetails().monthlyFee());
        contract.setContractType(company.getContractDetails().type().name());
        contract.setAutoRenewal(company.getContractDetails().autoRenewal());
        contract.setIsActive(company.getContractDetails().isActive());
        return contract;
    }

    private static @NotNull ContactPersonEmbeddable getContactPersonEmbeddable(ContactPerson primary) {
        ContactPersonEmbeddable primaryContact = new ContactPersonEmbeddable();
        primaryContact.setFirstName(primary.name().firstName());
        primaryContact.setLastName(primary.name().lastName());
        primaryContact.setEmail(primary.email() != null ? primary.email().value() : null);
        primaryContact.setPhone(primary.phone() != null ? primary.phone().value() : null);
        primaryContact.setPosition(primary.position());
        primaryContact.setDepartment(primary.department().name());
        primaryContact.setIsDecisionMaker(primary.isDecisionMaker());
        primaryContact.setIsPrimaryContact(primary.isPrimaryContact());
        return primaryContact;
    }

    public CustomerCompany toDomain(CustomerCompanyEntity entity) {
        CompanyName companyName = new CompanyName(entity.getCompanyName());

        Industry industry = new Industry(
                entity.getIndustryCode(),
                entity.getIndustryName(),
                entity.getSector()
        );

        CompanySize companySize = entity.getCompanySize();

        AnnualRevenue revenue = null;
        if (entity.getAnnualRevenueAmount() != null) {
            revenue = new AnnualRevenue(
                    entity.getAnnualRevenueAmount(),
                    Currency.getInstance(entity.getAnnualRevenueCurrency()),
                    entity.getRevenueRange()
            );
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

        if (entity.getPrimaryContact() != null) {
            ContactPerson primary = mapEmbeddableToContactPerson(entity.getPrimaryContact());
            contactPersons.add(primary);
        }

        ContractDetails contractDetails = null;
        if (entity.getContractDetails() != null) {
            contractDetails = new ContractDetails(
                    entity.getContractDetails().getContractId(),
                    entity.getContractDetails().getContractStartDate(),
                    entity.getContractDetails().getContractEndDate(),
                    entity.getContractDetails().getMonthlyFee(),
                    ContractDetails.ContractType.valueOf(entity.getContractDetails().getContractType()),
                    Set.of(),
                    entity.getContractDetails().getAutoRenewal()
            );
        }

        BillingInformation billingInfo = null;
        if (entity.getBillingInformation() != null) {
            billingInfo = new BillingInformation(
                    entity.getTaxId(),
                    entity.getBillingInformation().getBillingEmail() != null
                            ? new Email(entity.getBillingInformation().getBillingEmail())
                            : null,
                    BillingInformation.PaymentMethod.valueOf(
                            entity.getBillingInformation().getPreferredPaymentMethod()),
                    entity.getBillingInformation().getBillingAddress(),
                    entity.getBillingInformation().getApprovedCredit()
            );
        }

        return CustomerCompany.reconstruct(CustomerCompanyReconstructParams.builder()
                .id(new CustomerCompanyId(entity.getId()))
                .companyName(companyName)
                .companyProfile(companyProfile)
                .status(entity.getStatus())
                .contactPersons(contactPersons)
                .billingInfo(billingInfo)
                .contractDetails(contractDetails)
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
                entity.getIsPrimaryContact()
        );
    }

    private ContactPerson mapEmbeddableToContactPerson(ContactPersonEmbeddable embeddable) {
        return new ContactPerson(
                PersonName.from(embeddable.getFirstName(), embeddable.getLastName()),
                embeddable.getEmail() != null ? new Email(embeddable.getEmail()) : null,
                embeddable.getPhone() != null ? new PhoneNumber(embeddable.getPhone()) : null,
                embeddable.getPosition(),
                ContactPerson.Department.valueOf(embeddable.getDepartment()),
                embeddable.getIsDecisionMaker(),
                embeddable.getIsPrimaryContact()
        );
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
