package at.backend.MarketingCompany.crm.opportunity.domain.entity;

import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.opportunity.domain.exceptions.OpportunityValidationException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class Opportunity extends BaseDomainEntity<OpportunityId> {
  private at.backend.MarketingCompany.customer.domain.valueobject.CustomerId customerId;
  private String title;
  private Amount amount;
  private OpportunityStage stage;
  private ExpectedCloseDate expectedCloseDate;

  private Opportunity(OpportunityId opportunityId) {
    super(opportunityId);
  }

  private Opportunity(OpportunityReconstructParams params) {
    super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
    this.customerId = params.customerId();
    this.title = params.title();
    this.amount = params.amount();
    this.stage = params.stage();
    this.expectedCloseDate = params.expectedCloseDate();
    validateState();
  }

  public static Opportunity reconstruct(OpportunityReconstructParams params) {
    return new Opportunity(params);
  }

  public static Opportunity create(CreateOpportunityParams params) {
    validateCreationParams(params);

    Opportunity newOpportunity = new Opportunity(OpportunityId.generate());
    newOpportunity.customerId = params.customerId();
    newOpportunity.title = params.title();
    newOpportunity.amount = params.amount();
    newOpportunity.stage = OpportunityStage.LEAD;
    newOpportunity.expectedCloseDate = params.expectedCloseDate();
    newOpportunity.createdAt = LocalDateTime.now();
    newOpportunity.updatedAt = LocalDateTime.now();
    newOpportunity.version = 1;

    return newOpportunity;
  }

  public void updateDetails(String title, Amount amount, ExpectedCloseDate expectedCloseDate) {
    if (title == null || title.trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title cannot be empty");
    }

    this.title = title.trim();
    this.amount = amount;
    this.expectedCloseDate = expectedCloseDate;
    updateTimestamp();
  }

  public void qualify() {
    validateStageTransition(OpportunityStage.QUALIFIED);
    this.stage = OpportunityStage.QUALIFIED;
    updateTimestamp();
  }

  public void moveToProposal() {
    validateStageTransition(OpportunityStage.PROPOSAL);
    this.stage = OpportunityStage.PROPOSAL;
    updateTimestamp();
  }

  public void moveToNegotiation() {
    validateStageTransition(OpportunityStage.NEGOTIATION);
    this.stage = OpportunityStage.NEGOTIATION;
    updateTimestamp();
  }

  public void closeWon() {
    validateStageTransition(OpportunityStage.CLOSED_WON);
    this.stage = OpportunityStage.CLOSED_WON;
    updateTimestamp();
  }

  public void closeLost() {
    validateStageTransition(OpportunityStage.CLOSED_LOST);
    this.stage = OpportunityStage.CLOSED_LOST;
    updateTimestamp();
  }

  public void reopen() {
    if (this.stage != OpportunityStage.CLOSED_WON && this.stage != OpportunityStage.CLOSED_LOST) {
      throw new OpportunityValidationException("Only closed opportunities can be reopened");
    }
    this.stage = OpportunityStage.QUALIFIED;
    updateTimestamp();
  }

  public void updateAmount(Amount newAmount) {
    if (this.stage == OpportunityStage.CLOSED_WON || this.stage == OpportunityStage.CLOSED_LOST) {
      throw new OpportunityValidationException("Cannot update amount for a closed opportunity");
    }
    this.amount = newAmount;
    updateTimestamp();
  }

  public boolean isClosed() {
    return this.stage == OpportunityStage.CLOSED_WON || this.stage == OpportunityStage.CLOSED_LOST;
  }

  public boolean isWon() {
    return this.stage == OpportunityStage.CLOSED_WON;
  }

  public boolean isLost() {
    return this.stage == OpportunityStage.CLOSED_LOST;
  }

  public boolean isOverdue() {
    return expectedCloseDate != null && expectedCloseDate.isOverdue();
  }

  public boolean canBeModified() {
    return !isClosed();
  }

  private void validateState() {
    if (customerId == null) {
      throw new OpportunityValidationException("Customer ID is required");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title is required");
    }
    if (stage == null) {
      throw new OpportunityValidationException("Opportunity stage is required");
    }
  }

  private static void validateCreationParams(CreateOpportunityParams params) {
    if (params == null) {
      throw new OpportunityValidationException("Creation parameters cannot be null");
    }
    if (params.customerId() == null) {
      throw new OpportunityValidationException("Customer ID is required");
    }
    if (params.title() == null || params.title().trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title is required");
    }
  }

  private void validateStageTransition(OpportunityStage newStage) {
    if (this.stage == OpportunityStage.CLOSED_WON || this.stage == OpportunityStage.CLOSED_LOST) {
      throw new OpportunityValidationException("Cannot change stage of a closed opportunity");
    }

    if (!this.stage.canTransitionTo(newStage)) {
      throw new OpportunityValidationException(
          String.format("Cannot transition from %s to %s", this.stage, newStage));
    }
  }

  private void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

  public Optional<Amount> getAmount() {
    return Optional.ofNullable(amount);
  }

  public Optional<ExpectedCloseDate> getExpectedCloseDate() {
    return Optional.ofNullable(expectedCloseDate);
  }

  @Override
  public void markAsDeleted() {
    if (!isClosed()) {
      throw new OpportunityValidationException("Only closed opportunities can be deleted");
    }
    super.markAsDeleted();
  }
}
