package at.backend.MarketingCompany.customer.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.*;
import at.backend.MarketingCompany.customer.adapter.input.graphql.dto.CompanyMetrics;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.customer.adapter.input.graphql.dto.CompanyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
                .map(CustomerCompanyId::getValue)
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
	    return switch (size) {
		    case MICRO -> 5;
		    case SMALL -> 25;
		    case MEDIUM -> 150;
		    case LARGE -> 500;
		    case ENTERPRISE -> 2500;
		    default -> null;
	    };
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
                .build();
    }
}
