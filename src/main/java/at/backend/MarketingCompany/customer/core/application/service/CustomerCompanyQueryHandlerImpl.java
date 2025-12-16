package at.backend.MarketingCompany.customer.core.application.service;

import at.backend.MarketingCompany.customer.core.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.core.port.input.CustomerCompanyQueryHandler;
import at.backend.MarketingCompany.customer.core.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.exceptions.CompanyNotFoundException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.adapter.input.web.dto.CompanyMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerCompanyQueryHandlerImpl implements CustomerCompanyQueryHandler {
    private final CustomerCompanyRepositoryPort companyRepository;
    private static final BigDecimal DEFAULT_HIGH_VALUE_THRESHOLD = new BigDecimal("10000000"); // 10M
    private static final int DEFAULT_STARTUP_MAX_YEARS = 5;

    @Override
    @Transactional(readOnly = true)
    public CustomerCompany getCompanyById(GetCompanyByIdQuery query) {
        log.debug("Fetching company by ID: {}", query.id());
        return findCompanyOrThrow(query.id());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerCompany> getAllCompanies(GetAllCompaniesQuery query) {
        log.debug("Fetching all companies, page: {}, size: {}",
                query.pageable().getPageNumber(), query.pageable().getPageSize());
        return companyRepository.findAll(query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> searchCompanies(SearchCompaniesQuery query) {
        log.debug("Searching companies with term: {}", query.searchTerm());
        return companyRepository.searchCompanies(query.searchTerm(), query.pageable())
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> getCompaniesByIndustry(GetCompaniesByIndustryQuery query) {
        log.debug("Fetching companies by industry: {}", query.industryCode());
        return companyRepository.findByIndustry(query.industryCode());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> getCompaniesByStatus(GetCompaniesByStatusQuery query) {
        log.debug("Fetching companies by status: {}", query.status());
        return companyRepository.findByStatus(query.status());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> getHighValueCompanies(GetHighValueCompaniesQuery query) {
        BigDecimal minRevenue = query.minRevenue() != null
                ? query.minRevenue()
                : DEFAULT_HIGH_VALUE_THRESHOLD;

        log.debug("Fetching high-value companies with minimum revenue: {}", minRevenue);

        List<CustomerCompany> allCompanies = companyRepository.findAll(Pageable.unpaged()).getContent();

        return allCompanies.stream()
                .filter(company -> isHighValueCompany(company, minRevenue))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> getStartups(GetStartupsQuery query) {
        int years = query.startYearSince() != null ? query.startYearSince() : DEFAULT_STARTUP_MAX_YEARS;
        log.debug("Fetching startups founded in the last {} years", years);
        return companyRepository.findRecentStartups(years);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> getCompaniesWithExpiringContracts(GetCompaniesWithExpiringContractsQuery query) {
        log.debug("Fetching companies with expiring contracts");
        return companyRepository.findCompaniesWithExpiringContracts();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCompanyActive(IsCompanyActiveQuery query) {
        log.debug("Checking if company is active: {}", query.id());
        CustomerCompany company = findCompanyOrThrow(query.id());
        return company.isActive();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveContract(HasActiveContractQuery query) {
        log.debug("Checking if company has active contract: {}", query.id());
        CustomerCompany company = findCompanyOrThrow(query.id());
        return company.hasActiveContract();
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyMetrics getCompanyMetrics(GetCompanyMetricsQuery query) {
        if (query.companyId().isPresent()) {
            log.debug("Fetching metrics for single company: {}", query.companyId().get());
            return getSingleCompanyMetrics(query.companyId().get());
        } else {
            log.debug("Fetching global company metrics");
            return getGlobalCompanyMetrics();
        }
    }

    private CustomerCompany findCompanyOrThrow(CustomerCompanyId id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }


    private boolean isHighValueCompany(CustomerCompany company, BigDecimal minRevenue) {
        if (company.getCompanyProfile() == null ||
                company.getCompanyProfile().revenue() == null) {
            return false;
        }

        BigDecimal companyRevenue = company.getCompanyProfile().revenue().amount();
        return companyRevenue != null && companyRevenue.compareTo(minRevenue) >= 0;
    }

    private CompanyMetrics getSingleCompanyMetrics(CustomerCompanyId companyId) {
        CustomerCompany company = findCompanyOrThrow(companyId);

        BigDecimal revenue = BigDecimal.ZERO;
        if (company.getCompanyProfile() != null &&
                company.getCompanyProfile().revenue() != null &&
                company.getCompanyProfile().revenue().amount() != null) {
            revenue = company.getCompanyProfile().revenue().amount();
        }

        String companyName = company.getCompanyName() != null
                ? company.getCompanyName().toString()
                : "Unknown";

        return CompanyMetrics.builder()
                .totalCompanies(1)
                .activeCompanies(company.isActive() ? 1 : 0)
                .enterpriseClients(company.isEnterprise() ? 1 : 0)
                .startupClients(company.isStartup() ? 1 : 0)
                .totalAnnualRevenue(revenue)
                .companyName(companyName)
                .build();
    }

    private CompanyMetrics getGlobalCompanyMetrics() {
        List<CustomerCompany> allCompanies;
        try {
            allCompanies = companyRepository.findAll(
                    org.springframework.data.domain.Pageable.unpaged()
            ).getContent();
        } catch (Exception e) {
            log.error("Error fetching companies for metrics", e);
            return CompanyMetrics.empty();
        }

        long totalCompanies = allCompanies.size();
        long activeCompanies = allCompanies.stream()
                .filter(CustomerCompany::isActive)
                .count();
        long enterpriseClients = allCompanies.stream()
                .filter(CustomerCompany::isEnterprise)
                .count();
        long startupClients = allCompanies.stream()
                .filter(CustomerCompany::isStartup)
                .count();

        BigDecimal totalAnnualRevenue = allCompanies.stream()
                .filter(company -> company.getCompanyProfile() != null)
                .filter(company -> company.getCompanyProfile().revenue() != null)
                .filter(company -> company.getCompanyProfile().revenue().amount() != null)
                .map(company -> company.getCompanyProfile().revenue().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Long> companiesByIndustry = allCompanies.stream()
                .filter(company -> company.getCompanyProfile() != null)
                .filter(company -> company.getCompanyProfile().industry() != null)
                .collect(Collectors.groupingBy(
                        company -> company.getCompanyProfile().industry().code(),
                        Collectors.counting()
                ));

        Map<String, Long> companiesBySize = allCompanies.stream()
                .filter(company -> company.getCompanyProfile() != null)
                .filter(company -> company.getCompanyProfile().size() != null)
                .collect(Collectors.groupingBy(
                        company -> company.getCompanyProfile().size().name(),
                        Collectors.counting()
                ));

        int companiesWithExpiringContracts = 0;
        try {
            companiesWithExpiringContracts = companyRepository
                    .findCompaniesWithExpiringContracts().size();
        } catch (Exception e) {
            log.warn("Error fetching companies with expiring contracts", e);
        }

        // CHANGE: Calculate average contract value if possible
        BigDecimal averageContractValue = calculateAverageContractValue(allCompanies);

        return CompanyMetrics.builder()
                .totalCompanies(totalCompanies)
                .activeCompanies(activeCompanies)
                .enterpriseClients(enterpriseClients)
                .startupClients(startupClients)
                .totalAnnualRevenue(totalAnnualRevenue)
                .companiesByIndustry(companiesByIndustry)
                .companiesBySize(companiesBySize)
                .companiesWithExpiringContracts(companiesWithExpiringContracts)
                .averageContractValue(averageContractValue)
                .build();
    }


    private BigDecimal calculateAverageContractValue(List<CustomerCompany> companies) {
        List<BigDecimal> contractValues = companies.stream()
                .filter(CustomerCompany::hasActiveContract)
                .map(CustomerCompany::getActiveContractValue)
                .filter(Objects::nonNull)
                .toList();

        if (contractValues.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = contractValues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(new BigDecimal(contractValues.size()), 2, BigDecimal.ROUND_HALF_UP);
    }

}
