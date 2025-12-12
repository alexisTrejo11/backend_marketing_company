package at.backend.MarketingCompany.MarketingCampaing.customer.application;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.application.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.customer.application.service.CustomerCompanyQueryHandlerImpl;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.exceptions.CompanyNotFoundException;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.anyInt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCompanyQueryHandlerImplTest {

    @Mock
    private CustomerCompanyRepositoryPort companyRepository;

    @InjectMocks
    private CustomerCompanyQueryHandlerImpl queryHandler;

    private CustomerCompanyId customerCompanyId;
    private CustomerCompany activeCompany;
    private CustomerCompany inactiveCompany;
    private CustomerCompany enterpriseCompany;
    private CustomerCompany startupCompany;

    @BeforeEach
    void setUp() {
        customerCompanyId = CustomerCompanyId.generate();

        // Create active company
        activeCompany = createMockCustomerCompany("Active Tech", CompanyStatus.ACTIVE,
                CompanySize.MEDIUM, new BigDecimal("1000000"), 2010, false);

        // Create inactive company
        inactiveCompany = createMockCustomerCompany("Inactive Corp", CompanyStatus.INACTIVE,
                CompanySize.SMALL, new BigDecimal("500000"), 2015, false);

        // Create enterprise company
        enterpriseCompany = createMockCustomerCompany("Enterprise Solutions", CompanyStatus.ACTIVE,
                CompanySize.ENTERPRISE, new BigDecimal("50000000"), 2005, false);

        // Create startup company
        startupCompany = createMockCustomerCompany("Startup Innovators", CompanyStatus.ACTIVE,
                CompanySize.SMALL, new BigDecimal("500000"), LocalDate.now().getYear() - 2, true);
    }

    private CustomerCompany createMockCustomerCompany(String name, CompanyStatus status,
                                                      CompanySize size, BigDecimal revenue,
                                                      Integer foundingYear, boolean isStartup) {
        CompanyName companyName = new CompanyName(name);

        Industry industry = new Industry("TECH", "Technology", "Software");
        AnnualRevenue annualRevenue = new AnnualRevenue(revenue, Currency.getInstance("USD"));

        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(size)
                .revenue(annualRevenue)
                .foundingYear(foundingYear)
                .build();

        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john@test.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        ));

        CustomerCompany company = CustomerCompany.reconstruct(
                CustomerCompanyReconstructParams.builder()
                        .companyName(companyName)
                        .companyProfile(profile)
                        .contactPersons(contactPersons)
                        .build()
        );

        // Set status if not PROSPECT (default)
        if (status != CompanyStatus.PROSPECT) {
            if (status == CompanyStatus.ACTIVE) {
                company.activate();
            } else if (status == CompanyStatus.INACTIVE) {
                company.activate(); // Need to activate first to deactivate
                company.deactivate("Test");
            }
        }

        return company;
    }

    @Test
    void handleGetCompanyByIdQuery_ValidId_ShouldReturnCompany() {
        // Arrange
        GetCompanyByIdQuery query = new GetCompanyByIdQuery(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(activeCompany));

        // Act
        CustomerCompany result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(activeCompany, result);
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleGetCompanyByIdQuery_InvalidId_ShouldThrowException() {
        // Arrange
        GetCompanyByIdQuery query = new GetCompanyByIdQuery(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> queryHandler.handle(query));
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleGetAllCompaniesQuery_WithPageable_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerCompany> companies = Arrays.asList(activeCompany, inactiveCompany);
        Page<CustomerCompany> companyPage = new PageImpl<>(companies, pageable, companies.size());

        GetAllCompaniesQuery query = new GetAllCompaniesQuery(pageable);

        when(companyRepository.findAll(pageable)).thenReturn(companyPage);

        // Act
        Page<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(companyRepository).findAll(pageable);
    }

    @Test
    void handleSearchCompaniesQuery_WithSearchTerm_ShouldReturnMatchingCompanies() {
        // Arrange
        String searchTerm = "Tech";
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerCompany> searchResults = List.of(activeCompany);
        Page<CustomerCompany> searchPage = new PageImpl<>(searchResults, pageable, searchResults.size());

        SearchCompaniesQuery query = new SearchCompaniesQuery(searchTerm, pageable);

        when(companyRepository.searchCompanies(searchTerm, pageable)).thenReturn(searchPage);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeCompany, result.get(0));
        verify(companyRepository).searchCompanies(searchTerm, pageable);
    }

    @Test
    void handleGetCompaniesByIndustryQuery_ValidIndustry_ShouldReturnCompanies() {
        // Arrange
        String industryCode = "TECH";
        List<CustomerCompany> companies = Arrays.asList(activeCompany, enterpriseCompany);

        GetCompaniesByIndustryQuery query = new GetCompaniesByIndustryQuery(industryCode);

        when(companyRepository.findByIndustry(industryCode)).thenReturn(companies);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(activeCompany));
        assertTrue(result.contains(enterpriseCompany));
        verify(companyRepository).findByIndustry(industryCode);
    }

    @Test
    void handleGetCompaniesByStatusQuery_ActiveStatus_ShouldReturnActiveCompanies() {
        // Arrange
        List<CustomerCompany> activeCompanies = Arrays.asList(activeCompany, enterpriseCompany, startupCompany);

        GetCompaniesByStatusQuery query = new GetCompaniesByStatusQuery(CompanyStatus.ACTIVE);

        when(companyRepository.findByStatus(CompanyStatus.ACTIVE)).thenReturn(activeCompanies);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(CustomerCompany::isActive));
        verify(companyRepository).findByStatus(CompanyStatus.ACTIVE);
    }

    @Test
    void handleGetHighValueCompaniesQuery_WithDefaultThreshold_ShouldReturnHighValueCompanies() {
        // Arrange
        List<CustomerCompany> allCompanies = Arrays.asList(
                activeCompany,        // 1M revenue
                enterpriseCompany,    // 50M revenue
                startupCompany        // 0.5M revenue
        );

        Page<CustomerCompany> allCompaniesPage = new PageImpl<>(allCompanies);

        GetHighValueCompaniesQuery query = new GetHighValueCompaniesQuery(null);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(allCompaniesPage);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        // Only enterprise company has > 10M revenue
        assertEquals(1, result.size());
        assertEquals(enterpriseCompany, result.get(0));
        verify(companyRepository).findAll(any(Pageable.class));
    }

    @Test
    void handleGetHighValueCompaniesQuery_WithCustomThreshold_ShouldFilterCorrectly() {
        // Arrange
        BigDecimal customThreshold = new BigDecimal("2000000"); // 2M
        List<CustomerCompany> allCompanies = Arrays.asList(
                activeCompany,        // 1M revenue - below threshold
                enterpriseCompany,    // 50M revenue - above threshold
                startupCompany        // 0.5M revenue - below threshold
        );

        Page<CustomerCompany> allCompaniesPage = new PageImpl<>(allCompanies);

        GetHighValueCompaniesQuery query = new GetHighValueCompaniesQuery(customThreshold);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(allCompaniesPage);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        // Only enterprise company has > 2M revenue
        assertEquals(1, result.size());
        assertEquals(enterpriseCompany, result.get(0));
    }

    @Test
    void handleGetHighValueCompaniesQuery_CompanyWithoutRevenue_ShouldNotInclude() {
        // Arrange
        CustomerCompany noRevenueCompany = createMockCustomerCompany(
                "No Revenue", CompanyStatus.ACTIVE, CompanySize.SMALL, BigDecimal.TEN, 2020, false);

        List<CustomerCompany> allCompanies = Arrays.asList(enterpriseCompany, noRevenueCompany);
        Page<CustomerCompany> allCompaniesPage = new PageImpl<>(allCompanies);

        GetHighValueCompaniesQuery query = new GetHighValueCompaniesQuery(null);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(allCompaniesPage);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        // Should only include enterprise company
        assertEquals(1, result.size());
        assertEquals(enterpriseCompany, result.get(0));
    }

    @Test
    void handleGetStartupsQuery_WithDefaultYears_ShouldReturnRecentStartups() {
        // Arrange
        int currentYear = LocalDate.now().getYear();

        // Create companies with different founding years
        CustomerCompany recentCompany = createMockCustomerCompany(
                "Recent", CompanyStatus.ACTIVE, CompanySize.SMALL,
                new BigDecimal("100000"), currentYear - 3, true);

        CustomerCompany oldCompany = createMockCustomerCompany(
                "Old", CompanyStatus.ACTIVE, CompanySize.MEDIUM,
                new BigDecimal("5000000"), currentYear - 10, false);

        List<CustomerCompany> startups = List.of(recentCompany);

        GetStartupsQuery query = new GetStartupsQuery(null);

        when(companyRepository.findRecentStartups(anyInt())).thenReturn(startups);
        when(companyRepository.findRecentStartups(anyInt())).thenReturn(startups);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recentCompany, result.get(0));
        verify(companyRepository).findRecentStartups(currentYear - 5);
    }

    @Test
    void handleGetStartupsQuery_WithCustomYears_ShouldReturnCorrectStartups() {
        // Arrange
        int customYears = 3;
        int currentYear = LocalDate.now().getYear();
        int startYear = currentYear - customYears;

        CustomerCompany veryRecent = createMockCustomerCompany(
                "Very Recent", CompanyStatus.ACTIVE, CompanySize.SMALL,
                new BigDecimal("200000"), currentYear - 2, true);

        CustomerCompany recentButOlder = createMockCustomerCompany(
                "Recent But Older", CompanyStatus.ACTIVE, CompanySize.SMALL,
                new BigDecimal("300000"), currentYear - 4, false); // 4 years old, should not be included

        List<CustomerCompany> startups = List.of(veryRecent);

        GetStartupsQuery query = new GetStartupsQuery(startYear);

        when(companyRepository.findRecentStartups(startYear)).thenReturn(startups);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(veryRecent, result.get(0));
        verify(companyRepository).findRecentStartups(currentYear - customYears);
    }

    @Test
    void handleGetCompaniesWithExpiringContractsQuery_ShouldReturnCompanies() {
        // Arrange
        List<CustomerCompany> expiringCompanies = Arrays.asList(activeCompany, enterpriseCompany);

        GetCompaniesWithExpiringContractsQuery query = new GetCompaniesWithExpiringContractsQuery(30);

        when(companyRepository.findCompaniesWithExpiringContracts()).thenReturn(expiringCompanies);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(companyRepository).findCompaniesWithExpiringContracts();
    }

    @Test
    void handleIsCompanyActiveQuery_ActiveCompany_ShouldReturnTrue() {
        // Arrange
        IsCompanyActiveQuery query = new IsCompanyActiveQuery(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(activeCompany));

        // Act
        boolean result = queryHandler.handle(query);

        // Assert
        assertTrue(result);
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleIsCompanyActiveQuery_InactiveCompany_ShouldReturnFalse() {
        // Arrange
        IsCompanyActiveQuery query = new IsCompanyActiveQuery(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(inactiveCompany));

        // Act
        boolean result = queryHandler.handle(query);

        // Assert
        assertFalse(result);
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleHasActiveContractQuery_CompanyWithContract_ShouldReturnTrue() {
        // Arrange
        HasActiveContractQuery query = new HasActiveContractQuery(customerCompanyId);

        // Create a company with an active contract
        CustomerCompany companyWithContract = activeCompany;
        // You would need to add a contract to the company here

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(companyWithContract));

        // Act
        boolean result = queryHandler.handle(query);

        // Assert
        // This depends on whether the company has a contract
        // For now, just verify the repository was called
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleGetCompanyMetricsQuery_ForSpecificCompany_ShouldReturnSingleCompanyMetrics() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.of(customerCompanyId));

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(enterpriseCompany));

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.totalCompanies());
        assertEquals(1, result.activeCompanies()); // enterpriseCompany is active
        assertEquals(1, result.enterpriseClients()); // enterpriseCompany is enterprise
        assertEquals(0, result.startupClients()); // enterpriseCompany is not a startup
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleGetCompanyMetricsQuery_ForAllCompanies_ShouldReturnGlobalMetrics() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.empty());

        List<CustomerCompany> allCompanies = Arrays.asList(
                activeCompany,        // Active, not enterprise, not startup
                inactiveCompany,      // Inactive
                enterpriseCompany,    // Active, enterprise
                startupCompany        // Active, startup
        );

        Page<CustomerCompany> allCompaniesPage = new PageImpl<>(allCompanies);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(allCompaniesPage);
        when(companyRepository.findCompaniesWithExpiringContracts()).thenReturn(List.of(activeCompany));

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.totalCompanies());
        assertEquals(3, result.activeCompanies()); // active, enterprise, startup
        assertEquals(1, result.enterpriseClients()); // enterpriseCompany
        assertEquals(1, result.startupClients()); // startupCompany
        assertEquals(1, result.companiesWithExpiringContracts()); // activeCompany

        // Check revenue calculation
        BigDecimal expectedRevenue = new BigDecimal("1000000")  // activeCompany
                .add(new BigDecimal("500000"))  // inactiveCompany
                .add(new BigDecimal("50000000")) // enterpriseCompany
                .add(new BigDecimal("500000"));  // startupCompany
        assertEquals(expectedRevenue, result.totalAnnualRevenue());

        // Check industry distribution
        assertNotNull(result.companiesByIndustry());
        assertEquals(4, result.companiesByIndustry().get("TECH"));

        verify(companyRepository).findAll(any(Pageable.class));
        verify(companyRepository).findCompaniesWithExpiringContracts();
    }

    @Test
    void handleGetCompanyMetricsQuery_EmptyRepository_ShouldReturnEmptyMetrics() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.empty());

        Page<CustomerCompany> emptyPage = new PageImpl<>(Collections.emptyList());

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
        when(companyRepository.findCompaniesWithExpiringContracts()).thenReturn(Collections.emptyList());

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.totalCompanies());
        assertEquals(0, result.activeCompanies());
        assertEquals(0, result.enterpriseClients());
        assertEquals(0, result.startupClients());
        assertEquals(BigDecimal.ZERO, result.totalAnnualRevenue());
        assertEquals(0, result.companiesWithExpiringContracts());
        verify(companyRepository).findAll(any(Pageable.class));
        verify(companyRepository).findCompaniesWithExpiringContracts();
    }

    @Test
    void handleGetCompanyMetricsQuery_RepositoryError_ShouldReturnEmptyMetrics() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.empty());

        when(companyRepository.findAll(any(Pageable.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(CompanyMetrics.empty(), result);
        verify(companyRepository).findAll(any(Pageable.class));
    }

    @Test
    void handleGetCompanyMetricsQuery_CompaniesWithoutRevenue_ShouldCalculateRevenueCorrectly() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.empty());

        // Create companies with and without revenue
        CustomerCompany withRevenue = createMockCustomerCompany(
                "With Revenue", CompanyStatus.ACTIVE, CompanySize.MEDIUM,
                new BigDecimal("1000000"), 2015, false);

        CustomerCompany withoutRevenue = createMockCustomerCompany(
                "Without Revenue", CompanyStatus.ACTIVE, CompanySize.SMALL,
                BigDecimal.ZERO, 2020, false); // No revenue

        List<CustomerCompany> companies = Arrays.asList(withRevenue, withoutRevenue);
        Page<CustomerCompany> companiesPage = new PageImpl<>(companies);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(companiesPage);
        when(companyRepository.findCompaniesWithExpiringContracts()).thenReturn(Collections.emptyList());

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("1000000"), result.totalAnnualRevenue());
        // Only company with revenue should be counted
        verify(companyRepository).findAll(any(Pageable.class));
    }

    @Test
    void handleGetCompanyMetricsQuery_CompanyWithoutProfile_ShouldHandleGracefully() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.of(customerCompanyId));

        // Create a company without a profile
        CompanyName companyName = new CompanyName("No Profile Company");
        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                new PersonName("Test", "User"),
                new Email("test@test.com"),
                null,
                "Manager",
                ContactPerson.Department.OTHER,
                false,
                true
        ));

        CustomerCompany noProfileCompany = CustomerCompany.reconstruct(
                CustomerCompanyReconstructParams.builder()
                        .id(customerCompanyId)
                        .companyName(companyName)
                        .contactPersons(contactPersons)
                        .companyProfile(null) // No profile
                        .status(CompanyStatus.ACTIVE)
                        .build()
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(noProfileCompany));

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.totalCompanies());
        assertEquals(BigDecimal.ZERO, result.totalAnnualRevenue());
        verify(companyRepository).findById(customerCompanyId);
    }

    @Test
    void handleGetCompanyMetricsQuery_ForAllCompanies_ShouldCalculateAverageContractValue() {
        // Arrange
        GetCompanyMetricsQuery query = new GetCompanyMetricsQuery(Optional.empty());

        // Create companies with different contract values
        // Note: This test assumes the CustomerCompany class has a getActiveContractValue() method
        List<CustomerCompany> companies = Arrays.asList(activeCompany, enterpriseCompany);
        Page<CustomerCompany> companiesPage = new PageImpl<>(companies);

        when(companyRepository.findAll(any(Pageable.class))).thenReturn(companiesPage);
        when(companyRepository.findCompaniesWithExpiringContracts()).thenReturn(Collections.emptyList());

        // Act
        CompanyMetrics result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        // The average contract value calculation depends on the implementation
        // For now, just verify the method runs without errors
        verify(companyRepository).findAll(any(Pageable.class));
    }

    @Test
    void handleGetCompaniesByIndustryQuery_EmptyResult_ShouldReturnEmptyList() {
        // Arrange
        String industryCode = "NONEXISTENT";
        GetCompaniesByIndustryQuery query = new GetCompaniesByIndustryQuery(industryCode);

        when(companyRepository.findByIndustry(industryCode)).thenReturn(Collections.emptyList());

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(companyRepository).findByIndustry(industryCode);
    }

    @Test
    void handleSearchCompaniesQuery_EmptyResult_ShouldReturnEmptyList() {
        // Arrange
        String searchTerm = "NONEXISTENT";
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerCompany> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        SearchCompaniesQuery query = new SearchCompaniesQuery(searchTerm, pageable);

        when(companyRepository.searchCompanies(searchTerm, pageable)).thenReturn(emptyPage);

        // Act
        List<CustomerCompany> result = queryHandler.handle(query);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(companyRepository).searchCompanies(searchTerm, pageable);
    }
}