package at.backend.MarketingCompany.customer.application.dto.query;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public class CompanyQueries {
    
    @Builder
    public record GetCompanyByIdQuery(
        @NotNull CustomerCompanyId id
    ) {}
    
    @Builder
    public record GetAllCompaniesQuery(
        Pageable pageable
    ) {}
    
    @Builder
    public record SearchCompaniesQuery(
        String searchTerm,
        Pageable pageable
    ) {}
    
    @Builder
    public record GetCompaniesByIndustryQuery(
        @NotNull String industryCode
    ) {}
    
    @Builder
    public record GetCompaniesByStatusQuery(
        @NotNull CompanyStatus status
    ) {}
    
    @Builder
    public record GetHighValueCompaniesQuery(
        BigDecimal minRevenue
    ) {}
    
    public record GetStartupsQuery(
        Integer startYearSince
    ) {}
    
    public record GetCompaniesWithExpiringContractsQuery(
        Integer daysThreshold
    ) {}
    
    public record GetCompaniesBySizeQuery(
        @NotNull CompanySize size,
        Pageable pageable
    ) {}
    
    public record IsCompanyActiveQuery(
        @NotNull CustomerCompanyId id
    ) {
        public static IsCompanyActiveQuery from(String id) {
            return new IsCompanyActiveQuery(new CustomerCompanyId(id));
        }
    }
    
    public record HasActiveContractQuery(
        @NotNull CustomerCompanyId id
    ) {

        public static HasActiveContractQuery from(String id) {
            return new HasActiveContractQuery(new CustomerCompanyId(id));
        }
    }
    
    @Builder
    public record GetCompanyMetricsQuery(Optional<CustomerCompanyId> companyId) {
        public static GetCompanyMetricsQuery from(String id) {
            if (id == null) {
                return new GetCompanyMetricsQuery(Optional.empty());
            }
            return new GetCompanyMetricsQuery(Optional.of(new CustomerCompanyId(id)));
        }

    }}