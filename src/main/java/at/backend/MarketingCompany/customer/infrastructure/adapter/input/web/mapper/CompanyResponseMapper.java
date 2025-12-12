package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.mapper;

import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyMetrics;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CompanyResponseMapper {

    private static final String DEFAULT_STRING = "";
    private static final BigDecimal DEFAULT_BIG_DECIMAL = BigDecimal.ZERO;

    public CompanyResponse toResponse(CustomerCompany company) {
        if (company == null) {
            log.warn("Attempting to map null CustomerCompany to response");
            return null;
        }

        try {
            return CompanyResponse.builder()
                    .id(safeGetId(company))
                    .companyName(safeGetCompanyName(company))
                    .legalName(safeGetLegalName(company))
                    .taxId(safeGetTaxId(company))
                    .website(safeGetWebsite(company))
                    .foundingYear(safeGetFoundingYear(company))
                    .industry(mapIndustry(company))
                    .size(safeGetCompanySize(company))
                    .employeeCount(safeGetEmployeeCount(company))
                    .annualRevenue(mapRevenue(company))
                    .status(safeGetStatus(company))
                    .isPublicCompany(safeIsPublicCompany(company))
                    .isStartup(safeIsStartup(company))
                    .targetMarket(safeGetTargetMarket(company))
                    .missionStatement(safeGetMissionStatement(company))
                    .keyProducts(safeGetKeyProducts(company))
                    .competitorUrls(safeGetCompetitorUrls(company))
                    .contactPersons(mapContactPersons(company))
                    .contractDetails(mapContractDetails(company))
                    .billingInfo(mapBillingInfo(company))
                    .socialMedia(mapSocialMedia(company))
                    .opportunityCount(safeGetOpportunityCount(company))
                    .interactionCount(safeGetInteractionCount(company))
                    .createdAt(safeGetCreatedAt(company))
                    .updatedAt(safeGetUpdatedAt(company))
                    .build();

        } catch (Exception e) {
            log.error("Error mapping CustomerCompany to response. ID: {}", safeGetId(company), e);
            throw new MappingException("Failed to map company to response", e);
        }
    }

    public PageResponse<CompanyResponse> toPageResponse(Page<CustomerCompany> companiesPage) {
        if (companiesPage == null || companiesPage.isEmpty()) {
            return PageResponse.empty();
        }
        return PageResponse.of(companiesPage.map(this::toResponse));
    }


    private String safeGetId(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getId)
                .map(CustomerCompanyId::value)
                .map(Object::toString)
                .orElse(null);
    }

    private String safeGetCompanyName(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyName)
                .map(CompanyName::value)
                .orElse(DEFAULT_STRING);
    }

    private String safeGetLegalName(CustomerCompany company) {
        // Si no tienes legalName separado, puedes usar companyName
        return safeGetCompanyName(company);
    }

    private String safeGetTaxId(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getBillingInfo)
                .map(BillingInformation::taxId)
                .orElse(null);
    }

    private String safeGetWebsite(CustomerCompany company) {
        return null;
    }

    private Integer safeGetFoundingYear(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::foundingYear)
                .orElse(null);
    }

    private CompanyResponse.IndustryResponse mapIndustry(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::industry)
                .map(industry -> CompanyResponse.IndustryResponse.builder()
                        .code(safeGet(industry::code, DEFAULT_STRING))
                        .name(safeGet(industry::name, DEFAULT_STRING))
                        .sector(safeGet(industry::sector, DEFAULT_STRING))
                        .build())
                .orElse(null);
    }

    private CompanySize safeGetCompanySize(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::size)
                .orElse(CompanySize.UNKNOWN);
    }

    private Integer safeGetEmployeeCount(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::size)
                .map(this::estimateEmployeeCount)
                .orElse(null);
    }

    private CompanyResponse.RevenueResponse mapRevenue(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::revenue)
                .map(revenue -> CompanyResponse.RevenueResponse.builder()
                        .amount(safeGet(revenue::amount, DEFAULT_BIG_DECIMAL))
                        .currency(safeGet(() -> revenue.currency().getCurrencyCode(), "USD"))
                        .range(safeGet(() -> revenue.range().name(), "UNKNOWN"))
                        .build())
                .orElse(null);
    }

    private CompanyStatus safeGetStatus(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getStatus)
                .orElse(CompanyStatus.UNKNOWN);
    }

    private boolean safeIsPublicCompany(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::isPublicCompany)
                .orElse(false);
    }

    private boolean safeIsStartup(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::isStartup)
                .orElse(false);
    }

    private String safeGetTargetMarket(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::targetMarket)
                .orElse(null);
    }

    private String safeGetMissionStatement(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::missionStatement)
                .orElse(null);
    }

    private Set<String> safeGetKeyProducts(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::keyProducts)
                .orElse(Collections.emptySet());
    }

    private Set<String> safeGetCompetitorUrls(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::competitorUrls)
                .orElse(Collections.emptySet());
    }

    private Set<CompanyResponse.ContactPersonResponse> mapContactPersons(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getContactPersons)
                .orElse(Collections.emptySet())
                .stream()
                .map(this::mapContactPerson)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private CompanyResponse.ContactPersonResponse mapContactPerson(ContactPerson contactPerson) {
        if (contactPerson == null) return null;

        return CompanyResponse.ContactPersonResponse.builder()
                .id(generateContactPersonId(contactPerson))
                .firstName(safeGet(() -> contactPerson.name().firstName(), DEFAULT_STRING))
                .lastName(safeGet(() -> contactPerson.name().lastName(), DEFAULT_STRING))
                .email(safeGet(() -> contactPerson.email().value(), null))
                .phone(safeGet(() -> contactPerson.phone().value(), null))
                .position(safeGet(contactPerson::position, DEFAULT_STRING))
                .department(safeGet(() -> contactPerson.department().name(), "OTHER"))
                .isDecisionMaker(safeGet(contactPerson::isDecisionMaker, false))
                .isPrimaryContact(safeGet(contactPerson::isPrimaryContact, false))
                .build();
    }

    private CompanyResponse.ContractDetailsResponse mapContractDetails(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getContractDetails)
                .map(contract -> CompanyResponse.ContractDetailsResponse.builder()
                        .contractId(safeGet(contract::contractId, DEFAULT_STRING))
                        .startDate(safeGet(contract::startDate, null))
                        .endDate(safeGet(contract::endDate, null))
                        .monthlyFee(safeGet(contract::monthlyFee, DEFAULT_BIG_DECIMAL))
                        .type(safeGet(() -> contract.type().name(), "UNKNOWN"))
                        .autoRenewal(safeGet(contract::autoRenewal, false))
                        .isActive(safeGet(contract::isActive, false))
                        .isExpiringSoon(safeGet(contract::isExpiringSoon, false))
                        .build())
                .orElse(null);
    }

    private CompanyResponse.BillingInfoResponse mapBillingInfo(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getBillingInfo)
                .map(billing -> CompanyResponse.BillingInfoResponse.builder()
                        .billingEmail(billing.billingEmail() !=  null ?  billing.billingEmail().value() : null)
                        .preferredPaymentMethod(safeGet(() -> billing.preferredPaymentMethod().name(), "INVOICE"))
                        .billingAddress(safeGet(billing::billingAddress, null))
                        .approvedCredit(safeGet(billing::approvedCredit, false))
                        .build())
                .orElse(null);
    }

    private CompanyResponse.SocialMediaResponse mapSocialMedia(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCompanyProfile)
                .map(CompanyProfile::socialMediaHandles)
                .map(social -> CompanyResponse.SocialMediaResponse.builder()
                        .linkedinUrl(extractSocialUrl(social, "linkedin"))
                        .twitterHandle(extractSocialHandle(social, "twitter"))
                        .facebookUrl(extractSocialUrl(social, "facebook"))
                        .instagramHandle(extractSocialHandle(social, "instagram"))
                        .build())
                .orElse(null);
    }

    private Integer safeGetOpportunityCount(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getOpportunities)
                .map(Set::size)
                .orElse(0);
    }

    private Integer safeGetInteractionCount(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getInteractions)
                .map(Set::size)
                .orElse(0);
    }

    private OffsetDateTime safeGetCreatedAt(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getCreatedAt)
                .map(dt -> dt.atOffset(OffsetDateTime.now().getOffset()))
                .orElse(null);
    }

    private OffsetDateTime safeGetUpdatedAt(CustomerCompany company) {
        return Optional.ofNullable(company)
                .map(CustomerCompany::getUpdatedAt)
                .map(dt -> dt.atOffset(OffsetDateTime.now().getOffset()))
                .orElse(null);
    }


    private <T> T safeGet(SupplierWithException<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (NullPointerException | IllegalArgumentException e) {
            log.debug("Safe get encountered null or invalid value, using default: {}", defaultValue);
            return defaultValue;
        } catch (Exception e) {
            log.warn("Unexpected error in safeGet", e);
            return defaultValue;
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }


    private Integer estimateEmployeeCount(CompanySize size) {
        switch (size) {
            case MICRO: return 5;
            case SMALL: return 25;
            case MEDIUM: return 150;
            case LARGE: return 500;
            case ENTERPRISE: return 2500;
            default: return null;
        }
    }

    private String generateContactPersonId(ContactPerson contactPerson) {
        String base = safeGet(() -> contactPerson.name().firstName(), "") +
                safeGet(() -> contactPerson.name().lastName(), "") +
                safeGet(() -> contactPerson.email().value(), "");
        return UUID.nameUUIDFromBytes(base.getBytes()).toString();
    }

    private String extractSocialUrl(SocialMediaHandles social, String platform) {
        return null;
    }

    private String extractSocialHandle(SocialMediaHandles social, String platform) {
        return null;
    }

    public List<CompanyResponse> toResponses(List<CustomerCompany> companies) {
        if (companies == null || companies.isEmpty()) {
            return Collections.emptyList();
        }

        return companies.stream()
                .map(this::toResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public CompanyMetrics toMetrics(CustomerCompany company) {
        if (company == null) return null;

        return CompanyMetrics.builder()
                .totalCompanies(1)
                .activeCompanies(company.isActive() ? 1 : 0)
                .enterpriseClients(company.isEnterprise() ? 1 : 0)
                .startupClients(company.isStartup() ? 1 : 0)
                .totalAnnualRevenue(safeGet(() -> company.getCompanyProfile().revenue().amount(), DEFAULT_BIG_DECIMAL))
                .companiesByIndustry(Collections.singletonMap(
                        safeGet(() -> company.getCompanyProfile().industry().code(), "UNKNOWN"),
                        1L
                ))
                .companiesBySize(Collections.singletonMap(
                        safeGet(() -> company.getCompanyProfile().size().name(), "UNKNOWN"),
                        1L
                ))
                .companiesWithExpiringContracts(company.getContractDetails() != null &&
                        company.getContractDetails().isExpiringSoon() ? 1 : 0)
                .averageContractValue(safeGet(() -> company.getContractDetails().monthlyFee(), DEFAULT_BIG_DECIMAL))
                .build();
    }
}
