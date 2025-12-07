package at.backend.MarketingCompany.MarketingCampaing.crm.deal.application;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.crm.deal.application.DealApplicationServiceImpl;
import at.backend.MarketingCompany.crm.deal.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.deal.application.commands.*;
import at.backend.MarketingCompany.crm.deal.application.queries.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealNotFoundException;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealStatusTransitionException;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;
import at.backend.MarketingCompany.crm.deal.domain.respository.DealRepository;
import at.backend.MarketingCompany.common.exceptions.ExternalServiceException;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Deal Application Service")
class DealApplicationServiceTest {

  @Mock
  private DealRepository dealRepository;

  @Mock
  private ExternalModuleValidator externalValidator;

  @InjectMocks
  private DealApplicationServiceImpl dealApplicationService;

  @Captor
  private ArgumentCaptor<Deal> dealCaptor;

  private DealId validDealId;
  private CustomerId validCustomerId;
  private OpportunityId validOpportunityId;
  private EmployeeId validEmployeeId;
  private List<ServicePackageId> validServiceIds;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private Deal draftDeal;
  private Deal signedDeal;

  @BeforeEach
  void setUp() {
    validDealId = DealId.create();
    validCustomerId = CustomerId.generate();
    validOpportunityId = OpportunityId.generate();
    validEmployeeId = EmployeeId.generate();
    validServiceIds = List.of(
        ServicePackageId.generate(),
        ServicePackageId.generate());
    validStartDate = LocalDate.now().plusDays(1);
    validEndDate = validStartDate.plusDays(30);

    // Setup deals for testing
    draftDeal = createDealInState(DealStatus.DRAFT);
    signedDeal = createDealInState(DealStatus.SIGNED);
  }

  @Nested
  @DisplayName("Create Deal Command")
  class CreateDealCommandTests {

    @Test
    @DisplayName("should generate deal successfully with valid commands")
    void handleCreateDeal_WithValidCommand_ShouldCreateDeal() {
      // Given
      var command = new CreateDealCommand(
          validCustomerId,
          validOpportunityId,
          validServiceIds, validStartDate);

      doNothing().when(externalValidator).validateCustomerExists(validCustomerId);
      doNothing().when(externalValidator).validateOpportunityExists(validOpportunityId);
      doNothing().when(externalValidator).validateServicesExist(validServiceIds);

      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      assertThat(result).isNotNull();
      assertThat(result.getDealStatus()).isEqualTo(DealStatus.DRAFT);
      assertThat(result.getCustomerId()).isEqualTo(validCustomerId);
      assertThat(result.getOpportunityId()).isEqualTo(validOpportunityId);

      verify(externalValidator).validateCustomerExists(validCustomerId);
      verify(externalValidator).validateOpportunityExists(validOpportunityId);
      verify(externalValidator).validateServicesExist(validServiceIds);
      verify(dealRepository).save(any(Deal.class));
    }

    @Test
    @DisplayName("should throw exception when customer validation fails")
    void handleCreateDeal_WithInvalidCustomer_ShouldThrowException() {
      // Given
      var command = new CreateDealCommand(
          validCustomerId,
          validOpportunityId,
          validServiceIds, validStartDate);

      doThrow(new ExternalServiceException("Customer not found"))
          .when(externalValidator).validateCustomerExists(validCustomerId);

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(ExternalServiceException.class)
          .hasMessageContaining("Customer not found");

      verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    @DisplayName("should throw exception when services validation fails")
    void handleCreateDeal_WithInvalidServices_ShouldThrowException() {
      // Given
      var command = new CreateDealCommand(
          validCustomerId,
          validOpportunityId,
          validServiceIds, validStartDate);

      doNothing().when(externalValidator).validateCustomerExists(validCustomerId);
      doNothing().when(externalValidator).validateOpportunityExists(validOpportunityId);
      doThrow(new ExternalServiceException("Relationship Entity not found"))
          .when(externalValidator).validateServicesExist(any());

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(ExternalServiceException.class)
          .hasMessageContaining("Relationship Entity not found");

      verify(dealRepository, never()).save(any(Deal.class));
    }
  }

  @Nested
  @DisplayName("Sign Deal Command")
  class SignDealCommandTests {

    @Test
    @DisplayName("should sign deal successfully")
    void handleSignDeal_WithValidCommand_ShouldSignDeal() {
      // Given
      var finalAmount = new FinalAmount(new BigDecimal("5000.00"));
      var terms = "Payment in 30 days";
      var command = new SignDealCommand(validDealId, finalAmount, terms, validEmployeeId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));
      doNothing().when(externalValidator).validateEmployeeExists(validEmployeeId);
      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      verify(dealRepository).save(dealCaptor.capture());
      Deal savedDeal = dealCaptor.getValue();

      assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.SIGNED);
      assertThat(savedDeal.getFinalAmount()).contains(finalAmount);
      assertThat(savedDeal.getTerms()).contains(terms);
      assertThat(savedDeal.getCampaignManagerId()).contains(validEmployeeId);

      verify(externalValidator).validateEmployeeExists(validEmployeeId);
    }

    @Test
    @DisplayName("should throw exception when deal not found")
    void handleSignDeal_WithNonExistentDeal_ShouldThrowException() {
      // Given
      var command = new SignDealCommand(
          validDealId,
          new FinalAmount(new BigDecimal("5000.00")),
          "terms",
          validEmployeeId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.empty());

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(DealNotFoundException.class);

      verify(externalValidator, never()).validateEmployeeExists(any());
      verify(dealRepository, never()).save(any(Deal.class));
    }

    @Test
    @DisplayName("should throw exception when employee validation fails")
    void handleSignDeal_WithInvalidEmployee_ShouldThrowException() {
      // Given
      var command = new SignDealCommand(
          validDealId,
          new FinalAmount(new BigDecimal("5000.00")),
          "terms",
          validEmployeeId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));
      doThrow(new ExternalServiceException("Employee not found"))
          .when(externalValidator).validateEmployeeExists(validEmployeeId);

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(ExternalServiceException.class)
          .hasMessageContaining("Employee not found");

      verify(dealRepository, never()).save(any(Deal.class));
    }
  }

  @Nested
  @DisplayName("Mark Deal As Paid Command")
  class MarkDealAsPaidCommandTests {

    @Test
    @DisplayName("should mark deal as paid successfully")
    void handleMarkAsPaid_WithValidCommand_ShouldMarkAsPaid() {
      // Given
      var command = new MarkDealAsPaidCommand(validDealId);
      var signedDeal = createDealInState(DealStatus.SIGNED);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(signedDeal));
      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      verify(dealRepository).save(dealCaptor.capture());
      Deal savedDeal = dealCaptor.getValue();

      assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.PAID);
    }

    @Test
    @DisplayName("should throw exception when deal is not in signed state")
    void handleMarkAsPaid_WithInvalidState_ShouldThrowException() {
      // Given
      var command = new MarkDealAsPaidCommand(validDealId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(DealStatusTransitionException.class);

      verify(dealRepository, never()).save(any(Deal.class));
    }
  }

  @Nested
  @DisplayName("Complete Deal Command")
  class CompleteDealCommandTests {

    @Test
    @DisplayName("should complete deal successfully")
    void handleCompleteDeal_WithValidCommand_ShouldCompleteDeal() {
      // Given
      var deliverables = "All services delivered successfully";
      var command = new CompleteDealCommand(validDealId, validEndDate, deliverables);
      var inProgressDeal = createDealInState(DealStatus.IN_PROGRESS);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(inProgressDeal));
      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      verify(dealRepository).save(dealCaptor.capture());
      Deal savedDeal = dealCaptor.getValue();

      assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.COMPLETED);
      assertThat(savedDeal.getDeliverables()).contains(deliverables);
      assertThat(savedDeal.getPeriod().endDate()).contains(validEndDate);
    }

    @Test
    @DisplayName("should throw exception when end date is invalid")
    void handleCompleteDeal_WithInvalidEndDate_ShouldThrowException() {
      // Given
      var invalidEndDate = validStartDate.minusDays(1); // Before start date
      var command = new CompleteDealCommand(validDealId, invalidEndDate, "deliverables");
      var inProgressDeal = createDealInState(DealStatus.IN_PROGRESS);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(inProgressDeal));

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("End date cannot be before start date");

      verify(dealRepository, never()).save(any(Deal.class));
    }
  }

  @Nested
  @DisplayName("Update Deal Services Command")
  class UpdateDealServicesCommandTests {

    @Test
    @DisplayName("should update services successfully")
    void handleUpdateServices_WithValidCommand_ShouldUpdateServices() {
      // Given
      var newServices = List.of(
          ServicePackageId.generate(),
          ServicePackageId.generate());
      var command = new UpdateDealServicesCommand(validDealId, newServices);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));
      doNothing().when(externalValidator).validateServicesExist(newServices);
      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      verify(dealRepository).save(dealCaptor.capture());
      Deal savedDeal = dealCaptor.getValue();

      assertThat(savedDeal.getServicePackageIds()).isEqualTo(newServices);
      verify(externalValidator).validateServicesExist(newServices);
    }

    @Test
    @DisplayName("should throw exception when services validation fails")
    void handleUpdateServices_WithInvalidServices_ShouldThrowException() {
      // Given
      var newServices = List.of(ServicePackageId.generate());
      var command = new UpdateDealServicesCommand(validDealId, newServices);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));
      doThrow(new ExternalServiceException("Relationship Entity not found"))
          .when(externalValidator).validateServicesExist(any());

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(command))
          .isInstanceOf(ExternalServiceException.class);

      verify(dealRepository, never()).save(any(Deal.class));
    }
  }

  @Nested
  @DisplayName("Query Handlers")
  class QueryHandlerTests {

    @Test
    @DisplayName("should return deal by id")
    void handleGetDealById_WithExistingDeal_ShouldReturnDeal() {
      // Given
      var query = new GetDealByIdQuery(validDealId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));

      // When
      Deal result = dealApplicationService.handle(query);

      // Then
      assertThat(result).isEqualTo(draftDeal);
      verify(dealRepository).findById(validDealId);
    }

    @Test
    @DisplayName("should throw exception when deal not found")
    void handleGetDealById_WithNonExistentDeal_ShouldThrowException() {
      // Given
      var query = new GetDealByIdQuery(validDealId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.empty());

      // When & Then
      assertThatThrownBy(() -> dealApplicationService.handle(query))
          .isInstanceOf(DealNotFoundException.class);
    }

    @Test
    @DisplayName("should return deals by status")
    void handleGetDealsByStatus_WithValidQuery_ShouldReturnDeals() {
      // Given
      var statuses = List.of(DealStatus.DRAFT, DealStatus.IN_NEGOTIATION);
      var query = new GetDealsByStatusQuery(statuses);
      var deals = List.of(draftDeal, createDealInState(DealStatus.IN_NEGOTIATION));

      when(dealRepository.findByStatuses(statuses)).thenReturn(deals);

      // When
      List<Deal> result = dealApplicationService.handle(query);

      // Then
      assertThat(result).hasSize(2);
      verify(dealRepository).findByStatuses(statuses);
    }

    @Test
    @DisplayName("should return deals by customer")
    void handleGetDealsByCustomer_WithValidQuery_ShouldReturnDeals() {
      // Given
      var query = new GetDealsByCustomerQuery(validCustomerId);
      var deals = List.of(draftDeal, signedDeal);

      when(dealRepository.findByCustomer(validCustomerId)).thenReturn(deals);

      // When
      List<Deal> result = dealApplicationService.handle(query);

      // Then
      assertThat(result).hasSize(2);
      verify(dealRepository).findByCustomer(validCustomerId);
    }
  }

  @Nested
  @DisplayName("Cancel Deal Command")
  class CancelDealCommandTests {

    @Test
    @DisplayName("should cancel deal successfully")
    void handleCancelDeal_WithValidCommand_ShouldCancelDeal() {
      // Given
      var command = new CancelDealCommand(validDealId);

      when(dealRepository.findById(validDealId)).thenReturn(Optional.of(draftDeal));
      when(dealRepository.save(any(Deal.class))).thenAnswer(invocation -> invocation.getArgument(0));

      // When
      Deal result = dealApplicationService.handle(command);

      // Then
      verify(dealRepository).save(dealCaptor.capture());
      Deal savedDeal = dealCaptor.getValue();

      assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.CANCELLED);
    }
  }

  // ===== HELPER METHODS =====

  private Deal createDealInState(DealStatus state) {
    var createParams = CreateDealParams.builder()
        .customerId(validCustomerId)
        .opportunityId(validOpportunityId)
        .startDate(validStartDate)
        .servicePackageIds(validServiceIds)
        .build();

    var deal = Deal.create(createParams);

    // Transition to desired state
    switch (state) {
      case DRAFT:
        break; // Already in draft
      case IN_NEGOTIATION:
        deal.startNegotiation();
        break;
      case SIGNED:
        deal.startNegotiation();
        deal.signDeal(
            new FinalAmount(new BigDecimal("5000.00")),
            "Payment terms",
            validEmployeeId);
        break;
      case PAID:
        deal.startNegotiation();
        deal.signDeal(
            new FinalAmount(new BigDecimal("5000.00")),
            "Payment terms",
            validEmployeeId);
        deal.markAsPaid();
        break;
      case IN_PROGRESS:
        deal.startNegotiation();
        deal.signDeal(
            new FinalAmount(new BigDecimal("5000.00")),
            "Payment terms",
            validEmployeeId);
        deal.markAsPaid();
        deal.startExecution();
        break;
      case COMPLETED:
        deal.startNegotiation();
        deal.signDeal(
            new FinalAmount(new BigDecimal("5000.00")),
            "Payment terms",
            validEmployeeId);
        deal.markAsPaid();
        deal.startExecution();
        deal.completeDeal(validEndDate, "All deliverables completed");
        break;
      case CANCELLED:
        deal.cancelDeal();
        break;
    }

    return deal;
  }
}
