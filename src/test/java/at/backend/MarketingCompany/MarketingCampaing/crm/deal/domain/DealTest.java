package at.backend.MarketingCompany.MarketingCampaing.crm.deal.domain;

import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealStatusTransitionException;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Deal Domain Entity")
class DealTest {

  private CustomerCompanyId validCustomerCompanyId;
  private OpportunityId validOpportunityId;
  private List<ServicePackageId> validServiceIds;
  private LocalDate validStartDate;

  @BeforeEach
  void setUp() {
    validCustomerCompanyId = CustomerCompanyId.generate();
    validOpportunityId = OpportunityId.generate();
    validServiceIds = List.of(
        ServicePackageId.generate(),
        ServicePackageId.generate());
    validStartDate = LocalDate.now().plusDays(1);
  }

  private CreateDealParams.CreateDealParamsBuilder validCreateParams() {
    return CreateDealParams.builder()
        .customerCompanyId(validCustomerCompanyId)
        .opportunityId(validOpportunityId)
        .startDate(validStartDate)
        .servicePackageIds(validServiceIds);
  }

  @Nested
  @DisplayName("Creation")
  class CreationTests {

    @Test
    @DisplayName("should generate deal with valid parameters")
    void createDeal_WithValidParams_ShouldCreateDeal() {
      // When
      Deal deal = Deal.create(validCreateParams().build());

      // Then
      assertThat(deal).isNotNull();
      assertThat(deal.getId()).isNotNull();
      assertThat(deal.getCustomerId()).isEqualTo(validCustomerCompanyId);
      assertThat(deal.getOpportunityId()).isEqualTo(validOpportunityId);
      assertThat(deal.getDealStatus()).isEqualTo(DealStatus.DRAFT);
      assertThat(deal.getServicePackageIds()).isEqualTo(validServiceIds);
      assertThat(deal.getFinalAmount()).contains(FinalAmount.zero());
      assertThat(deal.getPeriod().startDate()).isEqualTo(validStartDate);
      assertThat(deal.getPeriod().endDate()).isNull();
      assertThat(deal.getCreatedAt()).isNotNull();
      assertThat(deal.getUpdatedAt()).isNotNull();
      assertThat(deal.getVersion()).isEqualTo(1);
    }

    @Test
    @DisplayName("should throw exception when generate params are null")
    void createDeal_WithNullParams_ShouldThrowException() {
      // When & Then
      assertThatThrownBy(() -> Deal.create(null))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Creation parameters cannot be null");
    }

    @Test
    @DisplayName("should throw exception when customerCompanyId is null")
    void createDeal_WithNullCustomerId_ShouldThrowException() {
      // Given
      var paramsBuilder = validCreateParams();

      // When & Then
      assertThatThrownBy(() -> {
        validCreateParams().customerCompanyId(null).build();
      }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when opportunityId is null")
    void createDeal_WithNullOpportunityId_ShouldThrowException() {
      // When & Then
      assertThatThrownBy(() -> validCreateParams().opportunityId(null).build())
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("should throw exception when start date is null")
    void createDeal_WithNullStartDate_ShouldThrowException() {
      // When & Then
      assertThatThrownBy(() -> validCreateParams().startDate(null).build())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("StartDate must not be null");
    }
  }

  @Nested
  @DisplayName("State Transitions")
  class StateTransitionTests {

    private Deal draftDeal;

    @BeforeEach
    void setUp() {
      draftDeal = Deal.create(validCreateParams().build());
    }

    @Test
    @DisplayName("should transition from DRAFT to IN_NEGOTIATION")
    void startNegotiation_FromDraft_ShouldTransitionToInNegotiation() {
      // When
      draftDeal.startNegotiation();

      // Then
      assertThat(draftDeal.getDealStatus()).isEqualTo(DealStatus.IN_NEGOTIATION);
      assertThat(draftDeal.getUpdatedAt()).isAfter(draftDeal.getCreatedAt());
    }

    @Test
    @DisplayName("should sign deal from IN_NEGOTIATION with valid parameters")
    void signDeal_FromInNegotiation_ShouldTransitionToSigned() {
      // Given
      draftDeal.startNegotiation();
      var amount = new FinalAmount(new BigDecimal("5000.00"));
      var terms = "Payment in 30 days";
      var managerId = EmployeeId.generate();

      // When
      draftDeal.signDeal(amount, terms, managerId);

      // Then
      assertThat(draftDeal.getDealStatus()).isEqualTo(DealStatus.SIGNED);
      assertThat(draftDeal.getFinalAmount()).contains(amount);
      assertThat(draftDeal.getTerms()).contains(terms);
      assertThat(draftDeal.getCampaignManagerId()).contains(managerId);
    }

    @Test
    @DisplayName("should throw exception when signing with null amount")
    void signDeal_WithNullAmount_ShouldThrowException() {
      // Given
      draftDeal.startNegotiation();

      // When & Then
      assertThatThrownBy(() -> draftDeal.signDeal(null, "terms", EmployeeId.generate()))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Final amount must be positive");
    }

    @Test
    @DisplayName("should throw exception when signing with zero amount")
    void signDeal_WithZeroAmount_ShouldThrowException() {
      // Given
      draftDeal.startNegotiation();
      var zeroAmount = FinalAmount.zero();

      // When & Then
      assertThatThrownBy(() -> draftDeal.signDeal(zeroAmount, "terms", EmployeeId.generate()))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Final amount must be positive");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should throw exception when signing with invalid terms")
    void signDeal_WithInvalidTerms_ShouldThrowException(String invalidTerms) {
      // Given
      draftDeal.startNegotiation();
      var amount = new FinalAmount(new BigDecimal("5000.00"));

      // When & Then
      assertThatThrownBy(() -> draftDeal.signDeal(amount, invalidTerms, EmployeeId.generate()))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Terms must be provided");
    }

    @Test
    @DisplayName("should throw exception when signing with null manager")
    void signDeal_WithNullManager_ShouldThrowException() {
      // Given
      draftDeal.startNegotiation();
      var amount = new FinalAmount(new BigDecimal("5000.00"));

      // When & Then
      assertThatThrownBy(() -> draftDeal.signDeal(amount, "terms", null))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Campaign manager must be assigned");
    }

    @Test
    @DisplayName("should mark as paid from SIGNED state")
    void markAsPaid_FromSigned_ShouldTransitionToPaid() {
      // Given
      var signedDeal = createSignedDeal();

      // When
      signedDeal.markAsPaid();

      // Then
      assertThat(signedDeal.getDealStatus()).isEqualTo(DealStatus.PAID);
    }

    @Test
    @DisplayName("should start execution from PAID state")
    void startExecution_FromPaid_ShouldTransitionToInProgress() {
      // Given
      var paidDeal = createPaidDeal();

      // When
      paidDeal.startExecution();

      // Then
      assertThat(paidDeal.getDealStatus()).isEqualTo(DealStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("should complete deal from IN_PROGRESS state")
    void completeDeal_FromInProgress_ShouldTransitionToCompleted() {
      // Given
      var inProgressDeal = createInProgressDeal();
      var endDate = validStartDate.plusDays(30);
      var deliverables = "All services delivered successfully";

      // When
      inProgressDeal.completeDeal(endDate, deliverables);

      // Then
      assertThat(inProgressDeal.getDealStatus()).isEqualTo(DealStatus.COMPLETED);
      assertThat(inProgressDeal.getPeriod().endDate()).isEqualTo(endDate);
      assertThat(inProgressDeal.getDeliverables()).contains(deliverables);
    }

    @Test
    @DisplayName("should throw exception when completing with end date before start date")
    void completeDeal_WithEndDateBeforeStart_ShouldThrowException() {
      // Given
      var inProgressDeal = createInProgressDeal();
      var invalidEndDate = validStartDate.minusDays(1);

      // When & Then
      assertThatThrownBy(() -> inProgressDeal.completeDeal(invalidEndDate, "deliverables"))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("End date cannot be before start date");
    }

    @Test
    @DisplayName("should cancel deal from any active state")
    void cancelDeal_FromActiveState_ShouldTransitionToCancelled() {
      // Given
      draftDeal.startNegotiation();

      // When
      draftDeal.cancelDeal();

      // Then
      assertThat(draftDeal.getDealStatus()).isEqualTo(DealStatus.CANCELLED);
    }

    @ParameterizedTest
    @EnumSource(value = DealStatus.class, names = { "COMPLETED", "CANCELLED" })
    @DisplayName("should not allow transitions from terminal states")
    void performAction_FromTerminalState_ShouldThrowException(DealStatus terminalStatus) {
      // Given
      var terminalDeal = createDealInState(terminalStatus);

      // When & Then - Try to perform any action
      assertThatThrownBy(terminalDeal::startNegotiation)
          .isInstanceOf(DealStatusTransitionException.class);
    }
  }

  @Nested
  @DisplayName("Service Package Updates")
  class ServicePackageEntityTests {

    @Test
    @DisplayName("should update service packages in modifiable states")
    void updateServicePackages_InModifiableState_ShouldUpdateSuccessfully() {
      // Given
      var deal = Deal.create(validCreateParams().build());
      var newServices = List.of(
          ServicePackageId.generate(),
          ServicePackageId.generate(),
          ServicePackageId.generate());

      // When
      deal.updateServicePackages(newServices);

      // Then
      assertThat(deal.getServicePackageIds()).isEqualTo(newServices);
    }

    @Test
    @DisplayName("should throw exception when updating services in completed state")
    void updateServicePackages_InCompletedState_ShouldThrowException() {
      // Given
      var completedDeal = createCompletedDeal();
      var newServices = List.of(ServicePackageId.generate());

      // When & Then
      assertThatThrownBy(() -> completedDeal.updateServicePackages(newServices))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Cannot update service packages");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("should throw exception when updating with invalid service list")
    void updateServicePackages_WithInvalidList_ShouldThrowException(List<ServicePackageId> invalidServices) {
      // Given
      var deal = Deal.create(validCreateParams().build());

      // When & Then
      assertThatThrownBy(() -> deal.updateServicePackages(invalidServices))
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Service packages list cannot be empty");
    }
  }

  @Nested
  @DisplayName("Reconstruction")
  class ReconstructionTests {

    @Test
    @DisplayName("should reconstruct deal with valid parameters")
    void reconstruct_WithValidParams_ShouldRecreateDeal() {
      // Given
      var dealId = DealId.create();
      var createdAt = LocalDateTime.now().minusDays(1);
      var updatedAt = LocalDateTime.now();

      var params = DealReconstructParams.builder()
          .id(dealId)
          .version(2)
          .createdAt(createdAt)
          .updatedAt(updatedAt)
          .customerCompanyId(validCustomerCompanyId)
          .opportunityId(validOpportunityId)
          .dealStatus(DealStatus.SIGNED)
          .finalAmount(new FinalAmount(new BigDecimal("7500.00")))
          .period(new ContractPeriod(validStartDate, Optional.of(validStartDate.plusDays(60))))
          .campaignManagerId(EmployeeId.generate())
          .terms("Payment terms")
          .deliverables("Initial deliverables")
          .servicePackageIds(validServiceIds)
          .build();

      // When
      Deal deal = Deal.reconstruct(params);

      // Then
      assertThat(deal.getId()).isEqualTo(dealId);
      assertThat(deal.getVersion()).isEqualTo(2);
      assertThat(deal.getCreatedAt()).isEqualTo(createdAt);
      assertThat(deal.getUpdatedAt()).isEqualTo(updatedAt);
      assertThat(deal.getDealStatus()).isEqualTo(DealStatus.SIGNED);
    }

    @Test
    @DisplayName("should validate state during reconstruction")
    void reconstruct_WithInvalidState_ShouldThrowException() {
      // Given - Missing required fields
      var params = DealReconstructParams.builder()
          .id(DealId.create())
          .dealStatus(DealStatus.DRAFT)
          .period(new ContractPeriod(validStartDate, null))
          .servicePackageIds(validServiceIds)
          .build();

      // When & Then - Should fail validation during reconstruction
      assertThatThrownBy(() -> Deal.reconstruct(params))
          .isInstanceOf(DealValidationException.class);
    }
  }

  @Nested
  @DisplayName("Business Logic Methods")
  class BusinessLogicTests {

    @Test
    @DisplayName("should return correct values for isSigned method")
    void isSigned_ForDifferentStates_ShouldReturnCorrectValue() {
      // Given
      var draftDeal = Deal.create(validCreateParams().build());
      var signedDeal = createSignedDeal();

      // Then
      assertThat(draftDeal.isSigned()).isFalse();
      assertThat(signedDeal.isSigned()).isTrue();
    }

    @Test
    @DisplayName("should return correct values for canBeModified method")
    void canBeModified_ForDifferentStates_ShouldReturnCorrectValue() {
      // Given
      var draftDeal = Deal.create(validCreateParams().build());
      var inNegotiationDeal = createDealInState(DealStatus.IN_NEGOTIATION);
      var signedDeal = createSignedDeal();

      // Then
      assertThat(draftDeal.canBeModified()).isTrue();
      assertThat(inNegotiationDeal.canBeModified()).isTrue();
      assertThat(signedDeal.canBeModified()).isFalse();
    }

    @Test
    @DisplayName("should allow deletion only for completed or cancelled deals")
    void softDelete_ForDifferentStates_ShouldBehaveCorrectly() {
      // Given
      var completedDeal = createCompletedDeal();
      var cancelledDeal = createDealInState(DealStatus.CANCELLED);
      var activeDeal = createSignedDeal();

      // When & Then - Completed and cancelled can be deleted
      assertThatCode(completedDeal::softDelete).doesNotThrowAnyException();
      assertThatCode(cancelledDeal::softDelete).doesNotThrowAnyException();

      // Active deals cannot be deleted
      assertThatThrownBy(activeDeal::softDelete)
          .isInstanceOf(DealValidationException.class)
          .hasMessageContaining("Only cancelled or completed deals can be deleted");
    }
  }

  // ===== HELPER METHODS =====

  private Deal createSignedDeal() {
    var deal = Deal.create(validCreateParams().build());
    deal.startNegotiation();
    deal.signDeal(
        new FinalAmount(new BigDecimal("5000.00")),
        "Payment terms",
        EmployeeId.generate());
    return deal;
  }

  private Deal createPaidDeal() {
    var deal = createSignedDeal();
    deal.markAsPaid();
    return deal;
  }

  private Deal createInProgressDeal() {
    var deal = createPaidDeal();
    deal.startExecution();
    return deal;
  }

  private Deal createCompletedDeal() {
    var deal = createInProgressDeal();
    deal.completeDeal(
        validStartDate.plusDays(30),
        "All deliverables completed");
    return deal;
  }

  private Deal createDealInState(DealStatus state) {
    return switch (state) {
      case DRAFT -> Deal.create(validCreateParams().build());
      case IN_NEGOTIATION -> {
        var deal = Deal.create(validCreateParams().build());
        deal.startNegotiation();
        yield deal;
      }
      case SIGNED -> createSignedDeal();
      case PAID -> createPaidDeal();
      case IN_PROGRESS -> createInProgressDeal();
      case COMPLETED -> createCompletedDeal();
      case CANCELLED -> {
        var deal = Deal.create(validCreateParams().build());
        deal.cancelDeal();
        yield deal;
      }
    };
  }
}
