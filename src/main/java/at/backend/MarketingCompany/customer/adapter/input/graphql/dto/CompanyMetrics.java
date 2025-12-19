package at.backend.MarketingCompany.customer.adapter.input.graphql.dto;

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
            .companyName("")
            .build();
    }
}
