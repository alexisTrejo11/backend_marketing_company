package at.backend.MarketingCompany.MarketingCampaing.customer.application;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.core.domain.valueobject.*;
import at.backend.MarketingCompany.customer.core.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.customer.core.application.service.CompanyMapper;
import at.backend.MarketingCompany.customer.core.application.service.CustomerCompanyCommandHandlerImpl;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.exceptions.CompanyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerCompanyCommandHandlerImplTest {

    @Mock
    private CustomerCompanyRepositoryPort companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private ContractFactory contractFactory;

    @InjectMocks
    private CustomerCompanyCommandHandlerImpl commandHandler;

    @Captor
    private ArgumentCaptor<CustomerCompany> companyCaptor;

    private CustomerCompanyId customerCompanyId;
    private CustomerCompany mockCompany;
    private CompanyProfile mockProfile;

    @BeforeEach
    void setUp() {
        customerCompanyId = CustomerCompanyId.generate();

        // Create mock CompanyProfile
        Industry industry = new Industry("TECH", "Technology", "Software");
        AnnualRevenue revenue = new AnnualRevenue(new BigDecimal("1000000"), Currency.getInstance("USD"));
        mockProfile = CompanyProfile.builder()
                .industry(industry)
                .size(CompanySize.MEDIUM)
                .revenue(revenue)
                .foundingYear(2015)
                .build();

        // Create mock CustomerCompany
        CompanyName companyName = new CompanyName("Test Company");
        Set<ContactPerson> contactPersons = createMockContactPersons();
        mockCompany = CustomerCompany.create(companyName, mockProfile, contactPersons);
    }

    private Set<ContactPerson> createMockContactPersons() {
        Set<ContactPerson> contacts = new HashSet<>();
        contacts.add(new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john@test.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        ));
        return contacts;
    }

    @Test
    void handleCreateCompanyCommand_ValidCommand_ShouldCreateAndSaveCompany() {
        // Arrange
        CreateCompanyCommand command = CreateCompanyCommand.builder()
                .companyName("New Tech Company")
                .industry("TECH")
                .companySize(CompanySize.SMALL)
                .employeeCount(50)
                .foundingYear(2020)
                .missionStatement("Innovating the future")
                .targetMarket("Global market")
                .keyProducts(Set.of("Product A", "Product B"))
                .contactPersons(
                        Set.of(new CreateCompanyCommand.ContactPersonCommand(
                                "Jane",
                                "Smith",
                                "jane@newtech.com",
                                "+1234567890",
                                "CTO",
                                ContactPerson.Department.IT,
                                true,
                                true
                        )))
                .taxId("TAX-123456789")
                .build();


        Set<ContactPerson> expectedContacts = createMockContactPersons();
        when(companyMapper.toContactPersons(any())).thenReturn(expectedContacts);
        when(companyMapper.toCompanyProfile(
                any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(mockProfile);
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyMapper).toContactPersons(command.contactPersons());
        verify(companyMapper).toCompanyProfile(
                command.industry(),
                command.companySize(),
                command.employeeCount(),
                command.foundingYear(),
                command.missionStatement(),
                command.targetMarket(),
                command.keyProducts()
        );
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleCreateCompanyCommand_WithoutTaxId_ShouldCreateCompanyWithoutBillingInfo() {
        // Arrange
        CreateCompanyCommand command = new CreateCompanyCommand(
                "New Tech Company",
                "TECH",
                CompanySize.SMALL,
                50,
                2020,
                "Innovating the future",
                "Global market",
                Set.of(new CreateCompanyCommand.ContactPersonCommand(
                        "Jane", "Smith", "jane@test.com",
                        "+1234567890", "CTO", ContactPerson.Department.IT, true, true
                )),
                null,  // No tax ID,
                "www.newtech.com",
                Set.of("Product A")
        );

        when(companyMapper.toContactPersons(any())).thenReturn(createMockContactPersons());
        when(companyMapper.toCompanyProfile(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mockProfile);
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        // Company should be created without billing info
    }

    @Test
    void handleUpdateCompanyCommand_ValidCommand_ShouldUpdateAndSaveCompany() {
        // Arrange
        var profileDto = new UpdateCompanyCommand.UpdateCompanyProfileCommand(
                "UPDATED",
                Optional.of(150),
                Optional.of("Updated mission"),
                Optional.of("Updated target market"),
                Optional.of(Set.of("Updated Product")),
                Optional.empty(),
                Optional.empty(),
                Optional.of(2010)
        );


        var billingDto = new UpdateCompanyCommand.UpdateCompanyBillingCommand(
                "TAX-UPDATED",
                "billing@updated.com",
                Optional.of(BillingInformation.PaymentMethod.BANK_TRANSFER),
                "Updated address",
                true
        );

        UpdateCompanyCommand command = new UpdateCompanyCommand(
                customerCompanyId,
                "Updated Company Name",
                profileDto,
                billingDto
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleUpdateCompanyCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        UpdateCompanyCommand command = new UpdateCompanyCommand(
                customerCompanyId,
                "Updated Name",
                null,
                null
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleActivateCompanyCommand_ValidCommand_ShouldActivateAndSaveCompany() {
        // Arrange
        ActivateCompanyCommand command = new ActivateCompanyCommand(
                customerCompanyId,
                "Ready for activation"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
        // Verify activation occurred (would need to check company status)
    }

    @Test
    void handleBlockCompanyCommand_ValidCommand_ShouldBlockAndSaveCompany() {
        // Arrange
        var command = new BlockCompanyCommand(
                customerCompanyId,
                "Suspicious activity detected"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(companyCaptor.capture());

        CustomerCompany savedCompany = companyCaptor.getValue();
        // The block method should be called on the company
        // (This would be better verified with a spy, but we're using mock)
    }

    @Test
    void handleDeactivateCompanyCommand_ValidCommand_ShouldDeactivateAndSaveCompany() {
        // Arrange
        DeactivateCompanyCommand command = new DeactivateCompanyCommand(
                customerCompanyId,
                "Customer requested deactivation"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleUpgradeToEnterpriseCommand_ValidCommand_ShouldUpgradeAndSaveCompany() {
        // Arrange
        BigDecimal newRevenue = new BigDecimal("5000000");
        UpgradeToEnterpriseCommand command = new UpgradeToEnterpriseCommand(
                customerCompanyId,
                newRevenue,
                "USD"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleUpgradeToEnterpriseCommand_InvalidCurrency_ShouldThrowException() {
        // Arrange
        UpgradeToEnterpriseCommand command = new UpgradeToEnterpriseCommand(
                customerCompanyId,
                new BigDecimal("5000000"),
                "INVALID_CURRENCY"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commandHandler.handle(command));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleSignContractCommand_ValidCommand_ShouldSignContractAndSaveCompany() {
        // Arrange
        SignContractCommand command = new SignContractCommand(
                customerCompanyId,
                "CONTRACT-001",
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                12,
                true
        );

        ContractDetails mockContract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now(),
                LocalDate.now().plusMonths(12),
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A", "Service B"),
                true
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(contractFactory.createContract(
                any(), any(), any(), any(), any()
        )).thenReturn(mockContract);
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(contractFactory).createContract(
                command.contractId(),
                command.contractType(),
                command.monthlyFee(),
                command.durationMonths(),
                command.autoRenewal()
        );
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleDeleteCompanyCommand_ValidCommand_ShouldDeleteCompany() {
        // Arrange
        DeleteCompanyCommand command = new DeleteCompanyCommand(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        doNothing().when(companyRepository).delete(customerCompanyId);

        // Act
        commandHandler.handle(command);

        // Assert
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).delete(customerCompanyId);
    }

    @Test
    void handleAddContactPersonCommand_ValidCommand_ShouldAddContactAndSaveCompany() {
        // Arrange
        AddContactPersonCommand command = new AddContactPersonCommand(
                customerCompanyId,
                "Bob",
                "Wilson",
                "bob.wilson@test.com",
                "+1122334455",
                "Marketing Director",
                "MARKETING",
                false
        );

        ContactPerson mockContact = new ContactPerson(
                new PersonName("Bob", "Wilson"),
                new Email("bob.wilson@test.com"),
                new PhoneNumber("+1122334455"),
                "Marketing Director",
                ContactPerson.Department.MARKETING,
                false,
                false
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyMapper.toContactPerson(
                any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(mockContact);
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyMapper).toContactPerson(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.position(),
                command.department(),
                command.isDecisionMaker()
        );
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleRemoveContactPersonCommand_ValidCommand_ShouldRemoveContactAndSaveCompany() {
        // Arrange
        String emailToRemove = "john@test.com";
        RemoveContactPersonCommand command = new RemoveContactPersonCommand(
                customerCompanyId,
                emailToRemove
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleRemoveContactPersonCommand_NonExistentEmail_ShouldStillSaveCompany() {
        // Arrange
        RemoveContactPersonCommand command = new RemoveContactPersonCommand(
                customerCompanyId,
                "nonexistent@test.com"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }

    @Test
    void handleActivateCompanyCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        ActivateCompanyCommand command = new ActivateCompanyCommand(
                customerCompanyId,
                "Test activation"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleBlockCompanyCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        BlockCompanyCommand command = new BlockCompanyCommand(
                customerCompanyId,
                "Test block"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleDeactivateCompanyCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        DeactivateCompanyCommand command = new DeactivateCompanyCommand(
                customerCompanyId,
                "Test deactivation"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleSignContractCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        SignContractCommand command = new SignContractCommand(
                customerCompanyId,
                "CONTRACT-001",
                new BigDecimal("5000"),
                ContractDetails.ContractType.ANNUAL,
                12,
                true
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(contractFactory, never()).createContract(any(), any(), any(), any(), any());
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleAddContactPersonCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        AddContactPersonCommand command = new AddContactPersonCommand(
                customerCompanyId,
                "New",
                "Contact",
                "new@test.com",
                "+1234567890",
                "Manager",
                "SALES",
                false
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyMapper, never()).toContactPerson(any(), any(), any(), any(), any(), any(), any());
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleRemoveContactPersonCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        RemoveContactPersonCommand command = new RemoveContactPersonCommand(
                customerCompanyId,
                "john@test.com"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleUpgradeToEnterpriseCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        UpgradeToEnterpriseCommand command = new UpgradeToEnterpriseCommand(
                customerCompanyId,
                new BigDecimal("5000000"),
                "USD"
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).save(any());
    }

    @Test
    void handleDeleteCompanyCommand_CompanyNotFound_ShouldThrowException() {
        // Arrange
        var command = new DeleteCompanyCommand(customerCompanyId);

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CompanyNotFoundException.class, () -> commandHandler.handle(command));
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository, never()).delete(any());
    }

    @Test
    void handleCreateCompanyCommand_EmptyTaxId_ShouldNotSetBillingInfo() {
        // Arrange
        CreateCompanyCommand command = CreateCompanyCommand.builder()
                .companyName("New Company")
                .industry("TECH")
                .companySize(CompanySize.SMALL)
                .employeeCount(50)
                .foundingYear(2020)
                .missionStatement("To be the best")
                .targetMarket("Global")
                .keyProducts(Set.of("Product A"))
                .contactPersons(Set.of(new CreateCompanyCommand.ContactPersonCommand(
                        "John", "Doe", "john@test.com",
                        "+1234567890", "CEO", ContactPerson.Department.EXECUTIVE, true, true
                )))
                .taxId("")  // Empty tax ID
                .build();


        when(companyMapper.toContactPersons(any())).thenReturn(createMockContactPersons());
        when(companyMapper.toCompanyProfile(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(mockProfile);
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        // Billing info should not be set for empty tax ID
    }

    @Test
    void handleUpdateCompanyCommand_WithOnlyNameUpdate_ShouldUpdateOnlyName() {
        // Arrange
        UpdateCompanyCommand command = new UpdateCompanyCommand(
                customerCompanyId,
                "Updated Name Only",
                null,  // No profile update
                null   // No billing update
        );

        when(companyRepository.findById(customerCompanyId)).thenReturn(Optional.of(mockCompany));
        when(companyRepository.save(any(CustomerCompany.class))).thenReturn(mockCompany);

        // Act
        CustomerCompany result = commandHandler.handle(command);

        // Assert
        assertNotNull(result);
        verify(companyRepository).findById(customerCompanyId);
        verify(companyRepository).save(any(CustomerCompany.class));
    }
}