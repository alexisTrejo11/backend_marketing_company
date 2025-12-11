package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Map;

@Builder
public record CompanyMetrics(
    long totalCompanies,
    long activeCompanies,
    long enterpriseClients,
    long startupClients,
    BigDecimal totalAnnualRevenue,
    Map<String, Long> companiesByIndustry,
    Map<String, Long> companiesBySize,
    int companiesWithExpiringContracts,
    BigDecimal averageContractValue,
    String companyName
) {
    public static CompanyMetrics empty() {
        return CompanyMetrics.builder()
            .totalCompanies(0)
            .activeCompanies(0)
            .enterpriseClients(0)
            .startupClients(0)
            .totalAnnualRevenue(BigDecimal.ZERO)
            .companiesByIndustry(Map.of())
            .companiesBySize(Map.of())
            .companiesWithExpiringContracts(0)
            .averageContractValue(BigDecimal.ZERO)
            .companyName("")
            .build();
    }
}
