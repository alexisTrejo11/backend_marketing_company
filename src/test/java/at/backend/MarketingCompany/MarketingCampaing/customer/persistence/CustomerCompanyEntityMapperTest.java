package at.backend.MarketingCompany.MarketingCampaing.customer.persistence;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.*;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.*;
import at.backend.MarketingCompany.customer.adapter.output.persistence.mapper.CustomerCompanyEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerCompanyEntityMapperTest {

    private CustomerCompanyEntityMapper mapper;
    private CustomerCompany customerCompany;
    private CustomerCompanyEntity entity;

    @BeforeEach
    void setUp() {
        mapper = new CustomerCompanyEntityMapper();
        
        // Create a CustomerCompany for testing
        customerCompany = createTestCustomerCompany();
        
        // Create a CustomerCompanyEntity for testing
        entity = createTestCustomerCompanyEntity();
    }

    private CustomerCompany createTestCustomerCompany() {
        CompanyName companyName = new CompanyName("Tech Innovations Inc.");
        
        Industry industry = new Industry("TECH", "Technology", "Software Development");
        
        AnnualRevenue revenue = new AnnualRevenue(
                new BigDecimal("2500000"),
                Currency.getInstance("USD")
        );
        
        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(CompanySize.MEDIUM)
                .revenue(revenue)
                .missionStatement("Innovating the future")
                .targetMarket("Global enterprise clients")
                .competitorUrls(Set.of("competitor1.com", "competitor2.com"))
                .socialMediaHandles(new SocialMediaHandles(
                        "@techinnovations",
                        "tech-innovations",
                        "Tech Innovations Inc.",
                        "techinnovations",
                        null,
                        null,
                        null
                ))
                .keyProducts(Set.of("Product A", "Product B"))
                .foundingYear(2015)
                .build();
        
        Set<ContactPerson> contactPersons = new HashSet<>();
        contactPersons.add(new ContactPerson(
                PersonName.from("John", "Doe"),
                new Email("john.doe@techinnovations.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        ));
        
        contactPersons.add(new ContactPerson(
                PersonName.from("Jane", "Smith"),
                new Email("jane.smith@techinnovations.com"),
                new PhoneNumber("+1987654321"),
                "CTO",
                ContactPerson.Department.IT,
                true,
                false
        ));
        
        CustomerCompany company = CustomerCompany.create(companyName, profile, contactPersons);
        
        // Add billing information
        BillingInformation billingInfo = new BillingInformation(
                "TAX-987654321",
                Email.from("billing@techinnovations.com"),
                BillingInformation.PaymentMethod.BANK_TRANSFER,
                "123 Business Ave, Tech City, TC 12345",
                true
        );
        
        // Add contract details
        ContractDetails contractDetails = new ContractDetails(
                "CONTRACT-2024-001",
                LocalDate.now().minusMonths(3),
                LocalDate.now().plusMonths(9),
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Premium Support", "API Access", "Analytics Dashboard"),
                true
        );
        
        // Set additional properties
        company.updateBillingInformation(billingInfo);
        company.signContract(contractDetails);
        
        return company;
    }

    private CustomerCompanyEntity createTestCustomerCompanyEntity() {
        CustomerCompanyEntity entity = new CustomerCompanyEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCompanyName("Tech Innovations Inc.");
        entity.setLegalName("Tech Innovations Incorporated");
        entity.setTaxId("TAX-987654321");
        entity.setWebsite("https://techinnovations.com");
        entity.setFoundingYear(2015);
        
        entity.setIndustryCode("TECH");
        entity.setIndustryName("Technology");
        entity.setSector("Software Development");
        
        entity.setCompanySize(CompanySize.MEDIUM);
        entity.setEmployeeCount(150);
        
        entity.setAnnualRevenueAmount(new BigDecimal("2500000"));
        entity.setAnnualRevenueCurrency("USD");
        entity.setRevenueRange(AnnualRevenue.RevenueRange.BETWEEN_1M_10M);
        
        entity.setStatus(CompanyStatus.ACTIVE);
        entity.setIsPublicCompany(false);
        entity.setIsStartup(false);
        
        entity.setTargetMarket("Global enterprise clients");
        entity.setMissionStatement("Innovating the future");
        entity.setKeyProducts(Set.of("Product A", "Product B"));
        entity.setCompetitorUrls(Set.of("competitor1.com", "competitor2.com"));
        
        // Primary contact
        ContactPersonEmbeddable primaryContact = new ContactPersonEmbeddable();
        primaryContact.setFirstName("John");
        primaryContact.setLastName("Doe");
        primaryContact.setEmail("john.doe@techinnovations.com");
        primaryContact.setPhone("+15551234567");
        primaryContact.setPosition("CEO");
        primaryContact.setDepartment("EXECUTIVE");
        primaryContact.setIsDecisionMaker(true);
        primaryContact.setIsPrimaryContact(true);
        entity.setPrimaryContact(primaryContact);
        
        // Additional contacts
        Set<ContactPersonEntity> contactPersons = new HashSet<>();
        
        ContactPersonEntity contact1 = new ContactPersonEntity();
        contact1.setFirstName("Jane");
        contact1.setLastName("Smith");
        contact1.setEmail("jane.smith@techinnovations.com");
        contact1.setPhone("+15559876543");
        contact1.setPosition("CTO");
        contact1.setDepartment("IT");
        contact1.setIsDecisionMaker(true);
        contact1.setIsPrimaryContact(false);
        contactPersons.add(contact1);
        
        entity.setContactPersons(contactPersons);
        
        // Contract details
        ContractDetailsEmbeddable contractDetails = new ContractDetailsEmbeddable();
        contractDetails.setContractId("CONTRACT-2024-001");
        contractDetails.setContractStartDate(LocalDate.now().minusMonths(3));
        contractDetails.setContractEndDate(LocalDate.now().plusMonths(9));
        contractDetails.setMonthlyFee(new BigDecimal("5000"));
        contractDetails.setContractType("ANNUAL");
        contractDetails.setAutoRenewal(true);
        contractDetails.setIsActive(true);
        entity.setContractDetails(contractDetails);
        
        // Billing information
        BillingInformationEmbeddable billingInfo = new BillingInformationEmbeddable();
        billingInfo.setBillingEmail("billing@techinnovations.com");
        billingInfo.setPreferredPaymentMethod("BANK_TRANSFER");
        billingInfo.setBillingAddress("123 Business Ave, Tech City, TC 12345");
        billingInfo.setApprovedCredit(true);
        entity.setBillingInformation(billingInfo);
        
        // Social media
        SocialMediaEmbeddable socialMedia = new SocialMediaEmbeddable();
        socialMedia.setTwitterHandle("@techinnovations");
        socialMedia.setLinkedinUrl("tech-innovations");
        socialMedia.setFacebookUrl("Tech Innovations Inc.");
        socialMedia.setInstagramHandle("techinnovations");
        entity.setSocialMedia(socialMedia);
        
        // Timestamps and version
        entity.setCreatedAt(LocalDateTime.now().minusMonths(6));
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setDeletedAt(null);
        entity.setVersion(1);
        
        return entity;
    }

    @Test
    void toEntity_ValidCustomerCompany_ShouldMapCorrectly() {
        // Act
        CustomerCompanyEntity result = mapper.toEntity(customerCompany);
        
        // Assert
        assertNotNull(result);
        assertEquals(customerCompany.getCompanyName().value(), result.getCompanyName());
        assertEquals(customerCompany.getCompanyProfile().industry().code(), result.getIndustryCode());
        assertEquals(customerCompany.getCompanyProfile().size(), result.getCompanySize());
        assertEquals(customerCompany.getStatus(), result.getStatus());
        assertNotNull(result.getPrimaryContact());
        assertEquals("John", result.getPrimaryContact().getFirstName());
        assertEquals("Doe", result.getPrimaryContact().getLastName());
        assertEquals("john.doe@techinnovations.com", result.getPrimaryContact().getEmail());
        
        // Check billing info
        assertNotNull(result.getBillingInformation());
        assertEquals(customerCompany.getBillingInfo().billingEmail(), 
                    result.getBillingInformation().getBillingEmail());
        assertEquals(customerCompany.getBillingInfo().preferredPaymentMethod().name(),
                    result.getBillingInformation().getPreferredPaymentMethod());
        
        // Check contract details
        assertNotNull(result.getContractDetails());
        assertEquals(customerCompany.getContractDetails().contractId(), 
                    result.getContractDetails().getContractId());
        assertEquals(customerCompany.getContractDetails().monthlyFee(), 
                    result.getContractDetails().getMonthlyFee());
        
        // Check revenue mapping
        assertNotNull(result.getAnnualRevenueAmount());
        assertEquals(customerCompany.getCompanyProfile().revenue().amount(), 
                    result.getAnnualRevenueAmount());
        assertEquals(customerCompany.getCompanyProfile().revenue().currency().getCurrencyCode(),
                    result.getAnnualRevenueCurrency());
        
        // Check collections
        assertNotNull(result.getKeyProducts());
        assertFalse(result.getKeyProducts().isEmpty());
        assertNotNull(result.getCompetitorUrls());
        assertFalse(result.getCompetitorUrls().isEmpty());
    }

    @Test
    void toEntity_CustomerCompanyWithNullFields_ShouldHandleGracefully() {
        // Arrange
        CompanyName companyName = new CompanyName("Minimal Company");
        Industry industry = new Industry("OTHER", "Other", "General");
        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(CompanySize.SMALL)
                .revenue(null)
                .foundingYear(null)
                .build();
        
        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                PersonName.from("Min", "Contact"),
                new Email("min@company.com"),
                null,
                null,
                null,
                false,
                false
        ));
        
        CustomerCompany minimalCompany = CustomerCompany.create(companyName, profile, contactPersons);
        
        // Act
        CustomerCompanyEntity result = mapper.toEntity(minimalCompany);
        
        // Assert
        assertNotNull(result);
        assertEquals("Minimal Company", result.getCompanyName());
        assertEquals("OTHER", result.getIndustryCode());
        assertEquals(CompanySize.SMALL, result.getCompanySize());
        assertNull(result.getAnnualRevenueAmount());
        assertNull(result.getAnnualRevenueCurrency());
        assertNull(result.getRevenueRange());
        assertNotNull(result.getPrimaryContact());
        assertEquals("Min", result.getPrimaryContact().getFirstName());
        assertEquals("Not specified", result.getPrimaryContact().getPosition());
        assertEquals("OTHER", result.getPrimaryContact().getDepartment());
    }

    @Test
    void toEntity_CustomerCompanyWithoutRevenue_ShouldMapNullValues() {
        // Arrange
        CompanyName companyName = new CompanyName("Non-Revenue Company");
        Industry industry = new Industry("SERV", "Services", "Consulting");
        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(CompanySize.SMALL)
                .revenue(null)
                .foundingYear(2020)
                .build();
        
        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                PersonName.from("Test", "Contact"),
                new Email("test@company.com"),
                null,
                "Manager",
                ContactPerson.Department.SALES,
                false,
                true
        ));
        
        CustomerCompany company = CustomerCompany.create(companyName, profile, contactPersons);
        
        // Act
        CustomerCompanyEntity result = mapper.toEntity(company);
        
        // Assert
        assertNotNull(result);
        assertNull(result.getAnnualRevenueAmount());
        assertNull(result.getAnnualRevenueCurrency());
        assertNull(result.getRevenueRange());
    }

    @Test
    void toDomain_ValidEntity_ShouldMapCorrectly() {
        // Act
        CustomerCompany result = mapper.toDomain(entity);
        
        // Assert
        assertNotNull(result);
        assertEquals(entity.getCompanyName(), result.getCompanyName().value());
        assertEquals(entity.getIndustryCode(), result.getCompanyProfile().industry().code());
        assertEquals(entity.getCompanySize(), result.getCompanyProfile().size());
        assertEquals(entity.getStatus(), result.getStatus());
        
        // Check contact persons
        assertEquals(2, result.getContactPersons().size()); // Primary + one additional
        
        // Check primary contact exists
        boolean hasPrimaryContact = result.getContactPersons().stream()
                .anyMatch(ContactPerson::isPrimaryContact);
        assertTrue(hasPrimaryContact);
        
        // Check decision makers
        long decisionMakersCount = result.getContactPersons().stream()
                .filter(ContactPerson::isDecisionMaker)
                .count();
        assertEquals(2, decisionMakersCount);
        
        // Check billing information
        assertNotNull(result.getBillingInfo());
        assertEquals(entity.getTaxId(), result.getBillingInfo().taxId());
        assertEquals(entity.getBillingInformation().getBillingEmail(), 
                    result.getBillingInfo().billingEmail());
        
        // Check contract details
        assertNotNull(result.getContractDetails());
        assertEquals(entity.getContractDetails().getContractId(), 
                    result.getContractDetails().contractId());
        assertEquals(entity.getContractDetails().getMonthlyFee(), 
                    result.getContractDetails().monthlyFee());
        assertEquals(entity.getContractDetails().getContractType(),
                    result.getContractDetails().type().name());
        
        // Check revenue
        assertNotNull(result.getCompanyProfile().revenue());
        assertEquals(entity.getAnnualRevenueAmount(), 
                    result.getCompanyProfile().revenue().amount());
        
        // Check founding year
        assertEquals(entity.getFoundingYear(), result.getCompanyProfile().foundingYear());
        
        // Check collections
        assertNotNull(result.getCompanyProfile().keyProducts());
        assertFalse(result.getCompanyProfile().keyProducts().isEmpty());
        assertNotNull(result.getCompanyProfile().competitorUrls());
        assertFalse(result.getCompanyProfile().competitorUrls().isEmpty());
    }

    @Test
    void toDomain_EntityWithNullFields_ShouldHandleGracefully() {
        // Arrange
        CustomerCompanyEntity minimalEntity = new CustomerCompanyEntity();
        minimalEntity.setId(UUID.randomUUID().toString());
        minimalEntity.setCompanyName("Minimal Entity");
        minimalEntity.setIndustryCode("OTHER");
        minimalEntity.setIndustryName("Other");
        minimalEntity.setSector("General");
        minimalEntity.setCompanySize(CompanySize.SMALL);
        minimalEntity.setStatus(CompanyStatus.PROSPECT);
        
        // No primary contact
        // No billing information
        // No contract details
        // No revenue
        
        // Act
        CustomerCompany result = mapper.toDomain(minimalEntity);
        
        // Assert
        assertNotNull(result);
        assertEquals("Minimal Entity", result.getCompanyName().value());
        assertEquals("OTHER", result.getCompanyProfile().industry().code());
        assertEquals(CompanySize.SMALL, result.getCompanyProfile().size());
        assertEquals(CompanyStatus.PROSPECT, result.getStatus());
        
        // Contact persons should be empty since no primary contact
        assertTrue(result.getContactPersons().isEmpty());
        
        // Billing info should be null
        assertNull(result.getBillingInfo());
        
        // Contract details should be null
        assertNull(result.getContractDetails());
        
        // Revenue should be null
        assertNull(result.getCompanyProfile().revenue());
    }

    @Test
    void toDomain_EntityWithInvalidPaymentMethod_ShouldThrowException() {
        // Arrange
        CustomerCompanyEntity entityWithInvalidPayment = createTestCustomerCompanyEntity();
        entityWithInvalidPayment.getBillingInformation().setPreferredPaymentMethod("INVALID_METHOD");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entityWithInvalidPayment));
    }

    @Test
    void toDomain_EntityWithInvalidContractType_ShouldThrowException() {
        // Arrange
        CustomerCompanyEntity entityWithInvalidContract = createTestCustomerCompanyEntity();
        entityWithInvalidContract.getContractDetails().setContractType("INVALID_TYPE");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entityWithInvalidContract));
    }

    @Test
    void toDomain_EntityWithInvalidDepartment_ShouldThrowException() {
        // Arrange
        CustomerCompanyEntity entityWithInvalidDepartment = createTestCustomerCompanyEntity();
        entityWithInvalidDepartment.getPrimaryContact().setDepartment("INVALID_DEPARTMENT");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entityWithInvalidDepartment));
    }

    @Test
    void toEntityAndBackToDomain_ShouldPreserveAllData() {
        // Arrange
        CustomerCompany original = customerCompany;

        // Act
        CustomerCompanyEntity entity = mapper.toEntity(original);
        CustomerCompany reconstructed = mapper.toDomain(entity);

        // Assert
        assertNotNull(reconstructed);
        assertEquals(original.getCompanyName().value(), reconstructed.getCompanyName().value());
        assertEquals(original.getStatus(), reconstructed.getStatus());

        // Check profile
        assertEquals(original.getCompanyProfile().industry().code(),
                    reconstructed.getCompanyProfile().industry().code());
        assertEquals(original.getCompanyProfile().size(),
                    reconstructed.getCompanyProfile().size());
        assertEquals(original.getCompanyProfile().foundingYear(),
                    reconstructed.getCompanyProfile().foundingYear());

        // Check contact persons count
        assertEquals(original.getContactPersons().size(),
                    reconstructed.getContactPersons().size());

        // Check billing info
        assertNotNull(reconstructed.getBillingInfo());
        assertEquals(original.getBillingInfo().taxId(), reconstructed.getBillingInfo().taxId());
        assertEquals(original.getBillingInfo().preferredPaymentMethod(),
                    reconstructed.getBillingInfo().preferredPaymentMethod());

        // Check contract details
        assertNotNull(reconstructed.getContractDetails());
        assertEquals(original.getContractDetails().contractId(),
                    reconstructed.getContractDetails().contractId());
        assertEquals(original.getContractDetails().type(),
                    reconstructed.getContractDetails().type());
    }

    @Test
    void toEntity_ShouldMapEmployeeCountBasedOnCompanySize() {
        // Arrange
        CustomerCompany smallCompany = createCustomerCompanyWithSize(CompanySize.SMALL);
        CustomerCompany mediumCompany = createCustomerCompanyWithSize(CompanySize.MEDIUM);
        CustomerCompany largeCompany = createCustomerCompanyWithSize(CompanySize.LARGE);
        CustomerCompany enterpriseCompany = createCustomerCompanyWithSize(CompanySize.ENTERPRISE);
        
        // Act & Assert
        CustomerCompanyEntity smallEntity = mapper.toEntity(smallCompany);
        assertEquals(25, smallEntity.getEmployeeCount());
        
        CustomerCompanyEntity mediumEntity = mapper.toEntity(mediumCompany);
        assertEquals(150, mediumEntity.getEmployeeCount());
        
        CustomerCompanyEntity largeEntity = mapper.toEntity(largeCompany);
        assertEquals(500, largeEntity.getEmployeeCount());
        
        CustomerCompanyEntity enterpriseEntity = mapper.toEntity(enterpriseCompany);
        assertEquals(2500, enterpriseEntity.getEmployeeCount());
    }

    private CustomerCompany createCustomerCompanyWithSize(CompanySize size) {
        CompanyName companyName = new CompanyName("Test Company - " + size);
        Industry industry = new Industry("TECH", "Technology", "Software");
        CompanyProfile profile = CompanyProfile.builder()
                .industry(industry)
                .size(size)
                .revenue(null)
                .foundingYear(2020)
                .build();
        
        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                PersonName.from("Test", "Contact"),
                new Email("test@company.com"),
                null,
                "Manager",
                ContactPerson.Department.OTHER,
                false,
                true
        ));
        
        return CustomerCompany.create(companyName, profile, contactPersons);
    }

    @Test
    void toEntity_ShouldMapSocialMediaHandles() {
        // Arrange
        SocialMediaHandles socialMedia = new SocialMediaHandles(
                "@twitter",
                "instagram-profile",
                "facebook-page",
                "linkedin-profile",
                null,
                null,
                null
        );
        
        CompanyProfile profileWithSocialMedia = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Software"))
                .size(CompanySize.MEDIUM)
                .revenue(null)
                .socialMediaHandles(socialMedia)
                .build();
        
        CompanyName companyName = new CompanyName("Social Media Company");
        Set<ContactPerson> contactPersons = Set.of(new ContactPerson(
                PersonName.from("Social", "Media"),
                new Email("social@media.com"),
                null,
                "Manager",
                ContactPerson.Department.MARKETING,
                false,
                true
        ));
        
        CustomerCompany company = CustomerCompany.create(companyName, profileWithSocialMedia, contactPersons);
        
        // Act
        CustomerCompanyEntity entity = mapper.toEntity(company);
        
        // Assert
        assertNotNull(entity.getSocialMedia());
        assertEquals("@twitter", entity.getSocialMedia().getTwitterHandle());
        assertEquals("linkedin-profile", entity.getSocialMedia().getLinkedinUrl());
        assertEquals("facebook-page", entity.getSocialMedia().getFacebookUrl());
        assertEquals("instagram-profile", entity.getSocialMedia().getInstagramHandle());
    }
}