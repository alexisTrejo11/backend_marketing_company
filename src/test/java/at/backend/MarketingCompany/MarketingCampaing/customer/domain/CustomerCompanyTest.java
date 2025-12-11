package at.backend.MarketingCompany.MarketingCampaing.customer.domain;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.events.*;
import at.backend.MarketingCompany.customer.domain.exceptions.CustomerDomainException;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerCompanyTest {
    
    private CompanyName validCompanyName;
    private CompanyProfile validCompanyProfile;
    private Set<ContactPerson> validContactPersons;
    private ContactPerson primaryContact;
    private ContactPerson decisionMakerContact;
    
    @BeforeEach
    void setUp() {
        validCompanyName = new CompanyName("TechCorp Solutions");
        
        validCompanyProfile = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Financial"))
                .size(CompanySize.MEDIUM)
                .revenue(new AnnualRevenue(new BigDecimal("500000"), Currency.getInstance("USD"), AnnualRevenue.RevenueRange.BETWEEN_100K_1M))
                .foundingYear(2015)
                .build();
        
        primaryContact = new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john.doe@techcorp.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        );
        
        decisionMakerContact = new ContactPerson(
                new PersonName("Jane", "Smith"),
                new Email("jane.smith@techcorp.com"),
                new PhoneNumber("+1987654321"),
                "CTO",
                ContactPerson.Department.EXECUTIVE,
                true,
                false
        );
        
        validContactPersons = new HashSet<>();
        validContactPersons.add(primaryContact);
        validContactPersons.add(decisionMakerContact);
    }
    
    @Test
    void create_ValidParameters_ShouldCreateCustomerCompany() {
        // Act
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        // Assert
        assertNotNull(company);
        assertNotNull(company.getId());
        assertEquals(validCompanyName, company.getCompanyName());
        assertEquals(validCompanyProfile, company.getCompanyProfile());
        assertEquals(CompanyStatus.PROSPECT, company.getStatus());
        assertEquals(2, company.getContactPersons().size());
        assertTrue(company.getContactPersons().contains(primaryContact));
        assertTrue(company.getContactPersons().contains(decisionMakerContact));
        assertTrue(company.isProspect());
        assertFalse(company.isBlocked());
        assertFalse(company.isActive());
    }
    
    @Test
    void create_ShouldRegisterCompanyCreatedEvent() {
        // Act
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        // Assert
        assertEquals(3, company.getDomainEvents().size()); // Created + 2 contact events
        
        CompanyCreatedEvent createdEvent = (CompanyCreatedEvent) company.getDomainEvents().get(0);
        assertEquals(company.getId(), createdEvent.getCompanyId());
        assertEquals(validCompanyName, createdEvent.getCompanyName());
        assertEquals(validCompanyProfile.industry(), createdEvent.getIndustry());
        assertEquals(CompanyStatus.PROSPECT, createdEvent.getInitialStatus());
    }
    
    @Test
    void create_NoContactPersons_ShouldThrowException() {
        // Arrange
        Set<ContactPerson> emptyContacts = new HashSet<>();
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> CustomerCompany.create(validCompanyName, validCompanyProfile, emptyContacts)
        );
        
        assertEquals("At least one contact person is required", exception.getMessage());
    }

    
    @Test
    void validate_ValidCompany_ShouldNotThrowException() {
        // Arrange
        CustomerCompany company = new CustomerCompany(CustomerCompanyId.generate());
        company.setCompanyName(validCompanyName);
        company.setCompanyProfile(validCompanyProfile);
        company.setContactPersons(validContactPersons);
        
        // Act & Assert
        assertDoesNotThrow(company::validate);
    }
    
    @Test
    void validate_NoCompanyName_ShouldThrowException() {
        // Arrange
        CustomerCompany company = new CustomerCompany(CustomerCompanyId.generate());
        company.setCompanyProfile(validCompanyProfile);
        company.setContactPersons(validContactPersons);
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                company::validate
        );
        
        assertEquals("Company name is required", exception.getMessage());
    }
    
    @Test
    void validate_NoCompanyProfile_ShouldThrowException() {
        // Arrange
        CustomerCompany company = new CustomerCompany(CustomerCompanyId.generate());
        company.setCompanyName(validCompanyName);
        company.setContactPersons(validContactPersons);
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                company::validate
        );
        
        assertEquals("Industry is required", exception.getMessage());
    }
    
    @Test
    void addContactPerson_ValidContact_ShouldAddAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                new HashSet<>(Set.of(primaryContact))
        );
        
        ContactPerson newContact = new ContactPerson(
                new PersonName("Bob", "Wilson"),
                new Email("bob.wilson@techcorp.com"),
                new PhoneNumber("+1122334455"),
                "Marketing Director",
                ContactPerson.Department.MARKETING,
                false,
                false
        );
        
        // Act
        company.addContactPerson(newContact);
        
        // Assert
        assertEquals(2, company.getContactPersons().size());
        assertTrue(company.getContactPersons().contains(newContact));
        
        // Verificar que se registró el evento
        assertEquals(3, company.getDomainEvents().size()); // Created + primary + new contact
    }
    
    @Test
    void addContactPerson_DuplicateEmail_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                new HashSet<>(Set.of(primaryContact))
        );
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.addContactPerson(primaryContact)
        );
        
        assertEquals("Contact with email already exists: " + primaryContact.email().value(), 
                    exception.getMessage());
    }
    
    @Test
    void addContactPerson_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Fraud detected");
        
        ContactPerson newContact = new ContactPerson(
                new PersonName("Bob", "Wilson"),
                new Email("bob.wilson@techcorp.com"),
                new PhoneNumber("+1122334455"),
                "Marketing Director",
                ContactPerson.Department.MARKETING,
                false,
                false
        );
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.addContactPerson(newContact)
        );
        
        assertEquals("Cannot add contacts to a blocked company", exception.getMessage());
    }
    
    @Test
    void removeContactPerson_ValidEmail_ShouldRemoveAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        // Act
        company.removeContactPerson(primaryContact.email());
        
        // Assert
        assertEquals(1, company.getContactPersons().size());
        assertFalse(company.getContactPersons().contains(primaryContact));
        assertTrue(company.getContactPersons().contains(decisionMakerContact));
        
        // Verificar que se registró el evento
        assertEquals(4, company.getDomainEvents().size()); // Created + 2 contacts + removed
    }
    
    @Test
    void removeContactPerson_LastContact_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                new HashSet<>(Set.of(primaryContact))
        );
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.removeContactPerson(primaryContact.email())
        );
        
        assertEquals("Company must have at least one contact person", exception.getMessage());
    }
    
    @Test
    void updateCompanyName_ValidName_ShouldUpdate() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        CompanyName newName = new CompanyName("TechCorp Innovations");
        
        // Act
        company.updateCompanyName(newName);
        
        // Assert
        assertEquals(newName, company.getCompanyName());
    }
    
    @Test
    void updateCompanyName_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Legal issues");
        
        CompanyName newName = new CompanyName("TechCorp Innovations");
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.updateCompanyName(newName)
        );
        
        assertEquals("Cannot update name of a blocked company", exception.getMessage());
    }
    
    @Test
    void activate_FromProspect_ShouldChangeStatusAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );

        // Act
        company.activate();

        // Assert
        assertEquals(CompanyStatus.ACTIVE, company.getStatus());
        assertTrue(company.isActive());
        CompanyStatusChangedEvent event = (CompanyStatusChangedEvent) company.getDomainEvents().get(3);

        assertEquals(CompanyStatus.PROSPECT, event.getOldStatus());
        assertEquals(CompanyStatus.ACTIVE, event.getNewStatus());
    }
    
    @Test
    void activate_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Fraud detected");
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                company::activate
        );
        
        assertEquals("Cannot activate a blocked company", exception.getMessage());
    }
    
    @Test
    void deactivate_FromActive_ShouldChangeStatusAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.activate(); // Primero activar
        
        // Act
        company.deactivate("Customer requested");
        
        // Assert
        assertEquals(CompanyStatus.INACTIVE, company.getStatus());
        assertFalse(company.isActive());
    }
    
    @Test
    void deactivate_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Legal issues");
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.deactivate("Test")
        );
        
        assertEquals("Cannot deactivate a blocked company", exception.getMessage());
    }
    
    @Test
    void block_FromAnyStatus_ShouldChangeStatusAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        // Act
        company.block("Fraudulent activity detected");
        
        // Assert
        assertEquals(CompanyStatus.BLOCKED, company.getStatus());
        assertTrue(company.isBlocked());
    }
    
    @Test
    void signContract_ProspectCompany_ShouldSignAndActivate() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A", "Service B"),
                true
        );
        
        // Act
        company.signContract(contract); // 2 Events Contract + Activation
        
        // Assert
        assertEquals(contract, company.getContractDetails());
        assertEquals(CompanyStatus.ACTIVE, company.getStatus());
        assertTrue(company.hasActiveContract());
        
        // Verificar eventos
        assertEquals(5, company.getDomainEvents().size()); // Created + 2 contacts + contract signed + activation
    }
    
    @Test
    void signContract_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Fraud detected");
        
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A", "Service B"),
                true
        );
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.signContract(contract)
        );
        
        assertEquals("Cannot sign contract for blocked company", exception.getMessage());
    }
    
    @Test
    void upgradeToEnterprise_ValidRevenue_ShouldUpgradeAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        AnnualRevenue enterpriseRevenue = new AnnualRevenue(new BigDecimal("5000000"), Currency.getInstance("USD"), AnnualRevenue.RevenueRange.BETWEEN_1M_10M);
        
        // Act
        company.upgradeToEnterprise(enterpriseRevenue);
        
        // Assert
        assertEquals(CompanySize.ENTERPRISE, company.getCompanyProfile().size());
        assertEquals(enterpriseRevenue, company.getCompanyProfile().revenue());
        assertTrue(company.isEnterprise());
        
        CompanyUpgradedToEnterpriseEvent event = (CompanyUpgradedToEnterpriseEvent)
                company.getDomainEvents().stream()
                        .filter(e -> e instanceof CompanyUpgradedToEnterpriseEvent)
                        .findFirst()
                        .orElse(null);
        
        assertNotNull(event);
        assertEquals(CompanySize.MEDIUM, event.getOldSize());
        assertEquals(CompanySize.ENTERPRISE, event.getNewSize());
    }
    
    @Test
    void upgradeToEnterprise_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Legal issues");
        
        AnnualRevenue enterpriseRevenue = new AnnualRevenue(new BigDecimal("5000000"), Currency.getInstance("USD"), AnnualRevenue.RevenueRange.BETWEEN_1M_10M);
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.upgradeToEnterprise(enterpriseRevenue)
        );
        
        assertEquals("Cannot upgrade blocked company", exception.getMessage());
    }
    
    @Test
    void addOpportunity_ValidOpportunity_ShouldAddAndRegisterEvent() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        OpportunityId opportunityId =  OpportunityId.generate();
        BigDecimal estimatedValue = new BigDecimal("10000");
        String opportunityType = "NEW_BUSINESS";
        
        // Act
        company.addOpportunity(opportunityId, estimatedValue, opportunityType);
        
        // Assert
        assertTrue(company.getOpportunities().contains(opportunityId));
        assertEquals(1, company.getOpportunities().size());
    }
    
    @Test
    void addOpportunity_WhenBlocked_ShouldThrowException() {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        company.block("Fraud detected");
        
        OpportunityId opportunityId = OpportunityId.generate();
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> company.addOpportunity(opportunityId, new BigDecimal("10000"), "TEST")
        );
        
        assertEquals("Cannot add opportunities to a blocked company", exception.getMessage());
    }
    
    @Test
    void getDecisionMakersEmails_ShouldReturnOnlyDecisionMakers() {
        // Arrange
        ContactPerson nonDecisionMaker = new ContactPerson(
                new PersonName("Bob", "Wilson"),
                new Email("bob.wilson@techcorp.com"),
                new PhoneNumber("+1122334455"),
                "Support Agent",
                ContactPerson.Department.OPERATIONS,
                false, // No es decision maker
                false
        );
        
        Set<ContactPerson> contacts = new HashSet<>();
        contacts.add(primaryContact);
        contacts.add(decisionMakerContact);
        contacts.add(nonDecisionMaker);
        
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                contacts
        );
        
        // Act
        Set<Email> decisionMakersEmails = company.getDecisionMakersEmails();
        
        // Assert
        assertEquals(2, decisionMakersEmails.size());
        assertTrue(decisionMakersEmails.contains(primaryContact.email()));
        assertTrue(decisionMakersEmails.contains(decisionMakerContact.email()));
        assertFalse(decisionMakersEmails.contains(nonDecisionMaker.email()));
    }
    
    @Test
    void isStartup_CompanyFoundedLessThan5YearsAgo_ShouldReturnTrue() {
        // Arrange
        CompanyProfile startupProfile = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Financial"))
                .size(CompanySize.SMALL)
                .revenue(new AnnualRevenue(new BigDecimal("100000"), Currency.getInstance("USD"), AnnualRevenue.RevenueRange.BETWEEN_100K_1M))
                .foundingYear(LocalDate.now().getYear() - 3) // Fundada hace 3 años
                .build();
        
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                startupProfile,
                validContactPersons
        );
        
        // Assert
        assertTrue(company.isStartup());
    }
    
    @Test
    void isHighValueClient_HighRevenue_ShouldReturnTrue() {
        // Arrange
        CompanyProfile highValueProfile = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Financial"))
                .size(CompanySize.LARGE)
                .revenue(new AnnualRevenue(new BigDecimal("20000000"), Currency.getInstance("USD"), AnnualRevenue.RevenueRange.BETWEEN_10M_100M))
                .foundingYear(2010)
                .build();
        
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                highValueProfile,
                validContactPersons
        );
        
        // Assert
        assertTrue(company.isHighValueClient());
    }
    
    @Test
    void reconstruct_ValidParams_ShouldReconstructCompany() {
        // Arrange
        CustomerCompanyId id = CustomerCompanyId.generate();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime updatedAt = now.plusHours(1);
        
        CustomerCompanyReconstructParams params = new CustomerCompanyReconstructParams(
                id,
                validCompanyName,
                validCompanyProfile,
                CompanyStatus.ACTIVE,
                validContactPersons,
                Set.of(OpportunityId.generate()),
                Set.of(),
                new BillingInformation(
                        "TAX-123",
                        Email.from("billing@techcorp.com"),
                        BillingInformation.PaymentMethod.BANK_TRANSFER,
                        "123 Main St",
                        true
                ),
                null,
                now,
                updatedAt,
                null,
                1
        );
        
        // Act
        CustomerCompany company = CustomerCompany.reconstruct(params);
        
        // Assert
        assertEquals(id, company.getId());
        assertEquals(validCompanyName, company.getCompanyName());
        assertEquals(validCompanyProfile, company.getCompanyProfile());
        assertEquals(CompanyStatus.ACTIVE, company.getStatus());
        assertEquals(2, company.getContactPersons().size());
        assertEquals(1, company.getOpportunities().size());
        assertNotNull(company.getBillingInfo());
        assertEquals(now, company.getCreatedAt());
        assertEquals(updatedAt, company.getUpdatedAt());
        assertEquals(1, company.getVersion());
    }
    
    @ParameterizedTest
    @EnumSource(value = CompanyStatus.class, names = {"PROSPECT", "LEAD", "ACTIVE", "INACTIVE"})
    void block_FromVariousStatuses_ShouldBlock(CompanyStatus initialStatus) {
        // Arrange
        CustomerCompany company = CustomerCompany.create(
                validCompanyName,
                validCompanyProfile,
                validContactPersons
        );
        
        if (initialStatus != CompanyStatus.PROSPECT) {
            switch (initialStatus) {
                case LEAD:
                    break;
                case ACTIVE:
                    company.activate();
                    break;
                case INACTIVE:
                    company.activate();
                    company.deactivate("Test");
                    break;
            }
        }
        
        // Act
        company.block("Test reason");
        
        // Assert
        assertEquals(CompanyStatus.BLOCKED, company.getStatus());
        assertTrue(company.isBlocked());
    }
}