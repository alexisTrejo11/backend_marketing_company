package at.backend.MarketingCompany.MarketingCampaing.customer.persistence;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.*;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.adapter.output.persistence.mapper.CustomerCompanyEntityMapper;
import at.backend.MarketingCompany.customer.adapter.output.persistence.repository.CustomerCompanyJpaRepository;
import at.backend.MarketingCompany.customer.adapter.output.persistence.repository.CustomerCompanyPersistenceAdapter;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCompanyPersistenceAdapterTest {

    @Mock
    private CustomerCompanyJpaRepository customerCompanyJpaRepository;

    @Mock
    private CustomerCompanyEntityMapper customerCompanyMapper;

    @InjectMocks
    private CustomerCompanyPersistenceAdapter persistenceAdapter;

    private CustomerCompany mockCustomerCompany;
    private CustomerCompanyEntity mockEntity;
    private CustomerCompanyId customerCompanyId;

    @BeforeEach
    void setUp() {
        customerCompanyId = CustomerCompanyId.generate();
        
        // Create mock CustomerCompany
        mockCustomerCompany = createMockCustomerCompany();
        mockEntity = createMockCustomerCompanyEntity();
    }

    private CustomerCompany createMockCustomerCompany() {
        CompanyName companyName = new CompanyName("Test Company");
        
        Industry industry = new Industry("TECH", "Technology", "Software");
        AnnualRevenue revenue = new AnnualRevenue(new BigDecimal("1000000"), Currency.getInstance("USD"));
        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(CompanySize.MEDIUM)
                .revenue(revenue)
                .foundingYear(2015)
                .build();
        
        Set<ContactPerson> contactPersons = new HashSet<>();
        contactPersons.add(new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john@test.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        ));
        
        CustomerCompany company = CustomerCompany.create(companyName, profile, contactPersons);
        return company;
    }

    private CustomerCompanyEntity createMockCustomerCompanyEntity() {
        CustomerCompanyEntity entity = new CustomerCompanyEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCompanyName("Test Company");
        entity.setIndustryCode("TECH");
        entity.setIndustryName("Technology");
        entity.setSector("Software");
        entity.setCompanySize(CompanySize.MEDIUM);
        entity.setStatus(CompanyStatus.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    @Test
    void save_ValidCustomerCompany_ShouldSaveAndReturnDomainObject() {
        // Arrange
        when(customerCompanyMapper.toEntity(mockCustomerCompany)).thenReturn(mockEntity);
        when(customerCompanyJpaRepository.save(mockEntity)).thenReturn(mockEntity);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        CustomerCompany result = persistenceAdapter.save(mockCustomerCompany);

        // Assert
        assertNotNull(result);
        verify(customerCompanyMapper).toEntity(mockCustomerCompany);
        verify(customerCompanyJpaRepository).save(mockEntity);
        verify(customerCompanyMapper).toDomain(mockEntity);
    }

    @Test
    void findById_ExistingId_ShouldReturnCustomerCompany() {
        // Arrange
        when(customerCompanyJpaRepository.findById(customerCompanyId.value()))
                .thenReturn(Optional.of(mockEntity));
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        Optional<CustomerCompany> result = persistenceAdapter.findById(customerCompanyId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockCustomerCompany, result.get());
        verify(customerCompanyJpaRepository).findById(customerCompanyId.value());
        verify(customerCompanyMapper).toDomain(mockEntity);
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmptyOptional() {
        // Arrange
        when(customerCompanyJpaRepository.findById(customerCompanyId.value()))
                .thenReturn(Optional.empty());

        // Act
        Optional<CustomerCompany> result = persistenceAdapter.findById(customerCompanyId);

        // Assert
        assertFalse(result.isPresent());
        verify(customerCompanyJpaRepository).findById(customerCompanyId.value());
        verify(customerCompanyMapper, never()).toDomain(any());
    }

    @Test
    void findAll_WithPageable_ShouldReturnPageOfCustomerCompanies() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        Page<CustomerCompanyEntity> entityPage = new PageImpl<>(entities, pageable, 1);
        
        when(customerCompanyJpaRepository.findAll(pageable)).thenReturn(entityPage);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        Page<CustomerCompany> result = persistenceAdapter.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(mockCustomerCompany, result.getContent().get(0));
        verify(customerCompanyJpaRepository).findAll(pageable);
        verify(customerCompanyMapper, times(1)).toDomain(mockEntity);
    }

    @Test
    void delete_ExistingId_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(customerCompanyJpaRepository).deleteById(customerCompanyId.value());

        // Act
        persistenceAdapter.delete(customerCompanyId);

        // Assert
        verify(customerCompanyJpaRepository).deleteById(customerCompanyId.value());
    }

    @Test
    void existsById_ExistingId_ShouldReturnTrue() {
        // Arrange
        when(customerCompanyJpaRepository.existsById(customerCompanyId.value())).thenReturn(true);

        // Act
        boolean result = persistenceAdapter.existsById(customerCompanyId);

        // Assert
        assertTrue(result);
        verify(customerCompanyJpaRepository).existsById(customerCompanyId.value());
    }

    @Test
    void findByIndustry_ValidIndustryCode_ShouldReturnList() {
        // Arrange
        String industryCode = "TECH";
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findByIndustryCode(industryCode)).thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findByIndustry(industryCode);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockCustomerCompany, result.get(0));
        verify(customerCompanyJpaRepository).findByIndustryCode(industryCode);
    }

    @Test
    void findByCompanyNameContaining_ValidName_ShouldReturnList() {
        // Arrange
        String name = "Test";
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findByCompanyNameContainingIgnoreCase(name)).thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findByCompanyNameContaining(name);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerCompanyJpaRepository).findByCompanyNameContainingIgnoreCase(name);
    }

    @Test
    void findByStatus_ValidStatus_ShouldReturnList() {
        // Arrange
        CompanyStatus status = CompanyStatus.ACTIVE;
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findByStatus(status)).thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerCompanyJpaRepository).findByStatus(status);
    }

    @Test
    void findByCompanySize_ValidSize_ShouldReturnPage() {
        // Arrange
        CompanySize size = CompanySize.MEDIUM;
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        Page<CustomerCompanyEntity> entityPage = new PageImpl<>(entities, pageable, 1);
        
        when(customerCompanyJpaRepository.findByCompanySize(size, pageable)).thenReturn(entityPage);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        Page<CustomerCompany> result = persistenceAdapter.findByCompanySize(size, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerCompanyJpaRepository).findByCompanySize(size, pageable);
    }

    @Test
    void findHighValueClients_ShouldReturnList() {
        // Arrange
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findHighValueCompanies(any(BigDecimal.class)))
                .thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findHighValueClients();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerCompanyJpaRepository).findHighValueCompanies(new BigDecimal("10000000"));
    }

    @Test
    void findByTaxId_ExistingTaxId_ShouldReturnCustomerCompany() {
        // Arrange
        String taxId = "TAX-123";
        
        when(customerCompanyJpaRepository.findByTaxId(taxId))
                .thenReturn(Optional.of(mockEntity));
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        Optional<CustomerCompany> result = persistenceAdapter.findByTaxId(taxId);

        // Assert
        assertTrue(result.isPresent());
        verify(customerCompanyJpaRepository).findByTaxId(taxId);
    }

    @Test
    void findCompaniesWithExpiringContracts_ShouldReturnList() {
        // Arrange
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findCompaniesWithExpiringContracts(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findCompaniesWithExpiringContracts();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerCompanyJpaRepository).findCompaniesWithExpiringContracts(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void findRecentStartups_ValidYears_ShouldReturnList() {
        // Arrange
        int years = 5;
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        
        when(customerCompanyJpaRepository.findRecentStartups(any(Integer.class)))
                .thenReturn(entities);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        List<CustomerCompany> result = persistenceAdapter.findRecentStartups(years);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(customerCompanyJpaRepository).findRecentStartups(any(Integer.class));
    }

    @Test
    void searchCompanies_WithSearchTerm_ShouldReturnPage() {
        // Arrange
        String searchTerm = "test";
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerCompanyEntity> entities = Arrays.asList(mockEntity);
        Page<CustomerCompanyEntity> entityPage = new PageImpl<>(entities, pageable, 1);
        
        when(customerCompanyJpaRepository.searchCompanies(searchTerm, pageable)).thenReturn(entityPage);
        when(customerCompanyMapper.toDomain(mockEntity)).thenReturn(mockCustomerCompany);

        // Act
        Page<CustomerCompany> result = persistenceAdapter.searchCompanies(searchTerm, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(customerCompanyJpaRepository).searchCompanies(searchTerm, pageable);
    }

    @Test
    void existsByTaxId_ExistingTaxId_ShouldReturnTrue() {
        // Arrange
        String taxId = "TAX-123";
        when(customerCompanyJpaRepository.existsByTaxId(taxId)).thenReturn(true);

        // Act
        boolean result = persistenceAdapter.existsByTaxId(taxId);

        // Assert
        assertTrue(result);
        verify(customerCompanyJpaRepository).existsByTaxId(taxId);
    }
}