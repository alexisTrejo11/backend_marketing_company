package at.backend.MarketingCompany.crm.opportunity.core.domain.entity;

import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.LossReason;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.NextSteps;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityStage;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Probability;
import at.backend.MarketingCompany.crm.opportunity.core.domain.exceptions.OpportunityValidationException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

@Getter
public class Opportunity extends BaseDomainEntity<OpportunityId> {
  private CustomerCompanyId customerCompanyId;
  private String title;
  private Amount amount;
  private OpportunityStage stage;
  private ExpectedCloseDate expectedCloseDate;
  private LossReason lossReason;
  private NextSteps nextSteps;
  private Probability probability;

  private Opportunity(OpportunityId opportunityId) {
    super(opportunityId);
    this.lossReason = LossReason.NONE;
    this.nextSteps = NextSteps.EMPTY;
    this.probability = Probability.MIN;
  }

  private Opportunity(OpportunityReconstructParams params) {
    super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
    this.customerCompanyId = params.customerCompanyId();
    this.title = params.title();
    this.amount = params.amount();
    this.stage = params.stage();
    this.expectedCloseDate = params.expectedCloseDate();
    this.lossReason = params.lossReason();
    this.nextSteps = params.nextSteps();
    this.probability = params.probability();
  }

  public static Opportunity reconstruct(OpportunityReconstructParams params) {
    return new Opportunity(params);
  }

  public static Opportunity create(CreateOpportunityParams params) {
    Opportunity newOpportunity = new Opportunity(OpportunityId.generate());
    newOpportunity.customerCompanyId = params.customerCompanyId();
    newOpportunity.title = params.title().trim();
    newOpportunity.amount = params.amount();
    newOpportunity.stage = OpportunityStage.PROSPECTING;
    newOpportunity.expectedCloseDate = params.expectedCloseDate();
    newOpportunity.nextSteps = NextSteps.createInitial();
    newOpportunity.probability = Probability.fromStage(OpportunityStage.PROSPECTING);

    newOpportunity.validateState();
    return newOpportunity;
  }

  public void updateDetails(
      String title,
      Amount amount,
      ExpectedCloseDate expectedCloseDate,
      NextSteps nextSteps,
      Probability probability) {
    validateCanBeModified();

    if (title == null || title.trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title cannot be empty");
    }

    this.title = title.trim();
    this.amount = amount != null ? amount : this.amount;
    this.expectedCloseDate = expectedCloseDate != null ? expectedCloseDate : this.expectedCloseDate;
    this.nextSteps = nextSteps != null ? nextSteps : this.nextSteps;
    this.probability = probability != null ? probability : this.probability;
    updateTimestamp();
  }

  public void qualify() {
    transitionToStage(OpportunityStage.QUALIFICATION);
    this.probability = Probability.fromStage(OpportunityStage.QUALIFICATION);
  }

  public void moveToProposal() {
    transitionToStage(OpportunityStage.PROPOSAL);
    this.probability = Probability.fromStage(OpportunityStage.PROPOSAL);
  }

  public void moveToNegotiation() {
    transitionToStage(OpportunityStage.NEGOTIATION);
    this.probability = Probability.fromStage(OpportunityStage.NEGOTIATION);
  }

  public void closeWon(String closeNotes) {
    validateCanTransitionToClosed();

    this.stage = OpportunityStage.CLOSED_WON;
    this.probability = Probability.WON;
    this.lossReason = null;
    if (closeNotes != null && !closeNotes.trim().isEmpty()) {
      this.nextSteps = NextSteps.withCloseNotes(closeNotes.trim());
    }
    updateTimestamp();
  }

  public void closeLost(LossReason lossReason, String notes) {
    validateCanTransitionToClosed();

    if (lossReason == null) {
      throw new OpportunityValidationException("Loss reason is required when closing as lost");
    }

    this.stage = OpportunityStage.CLOSED_LOST;
    this.probability = Probability.LOST;
    this.lossReason = lossReason;
    if (notes != null && !notes.trim().isEmpty()) {
      this.nextSteps = NextSteps.withCloseNotes(notes.trim());
    }
    updateTimestamp();
  }

  public void reopen(String reason) {
    if (!isClosed()) {
      throw new OpportunityValidationException("Only closed opportunities can be reopened");
    }
    if (reason == null || reason.trim().isEmpty()) {
      throw new OpportunityValidationException("Reopen reason is required");
    }

    this.stage = OpportunityStage.QUALIFICATION;
    this.probability = Probability.fromStage(OpportunityStage.QUALIFICATION);
    this.lossReason = null;
    this.nextSteps = NextSteps.withReopenReason(reason.trim());
    updateTimestamp();
  }

  public void updateAmount(Amount newAmount) {
    validateCanBeModified();
    this.amount = newAmount;
    updateTimestamp();
  }

  public void updateProbability(Probability newProbability) {
    validateCanBeModified();

    if (stage == OpportunityStage.CLOSED_WON || stage == OpportunityStage.CLOSED_LOST) {
      throw new OpportunityValidationException("Cannot update probability for closed opportunities");
    }

    if (!newProbability.isValidForStage(stage)) {
      throw new OpportunityValidationException(
          String.format("Probability %d%% is not valid for stage %s",
              newProbability.value(), stage));
    }

    this.probability = newProbability;
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

  private boolean calculateIsOverdue() {
    // Only active opportunities can be overdue
    if (isClosed()) {
      return false;
    }
    return expectedCloseDate != null && expectedCloseDate.isOverdue();
  }

  public boolean isOverdue() {
    return calculateIsOverdue();
  }

  public boolean canBeModified() {
    return !isClosed();
  }

  private void validateState() {
    if (customerCompanyId == null) {
      throw new OpportunityValidationException("Customer ID is required");
    }
    if (title == null || title.trim().isEmpty()) {
      throw new OpportunityValidationException("Opportunity title is required");
    }
    if (stage == null) {
      throw new OpportunityValidationException("Opportunity stage is required");
    }
    if (probability == null) {
      throw new OpportunityValidationException("Probability is required");
    }
    if (nextSteps == null) {
      throw new OpportunityValidationException("Next steps are required");
    }

    if (!probability.isValidForStage(stage)) {
      throw new OpportunityValidationException(
          String.format("Probability %d%% is not valid for stage %s",
              probability.value(), stage));
    }

    if (lossReason != null && !lossReason.isNone() && !isLost()) {
      throw new OpportunityValidationException("Loss reason can only be set for lost opportunities");
    }
  }

  private void transitionToStage(OpportunityStage newStage) {
    validateStageTransition(newStage);
    this.stage = newStage;
    updateTimestamp();
  }

  private void validateStageTransition(OpportunityStage newStage) {
    validateCanBeModified();

    if (!this.stage.canTransitionTo(newStage)) {
      throw new OpportunityValidationException(
          String.format("Cannot transition from %s to %s", this.stage, newStage));
    }
  }

  private void validateCanBeModified() {
    if (isClosed()) {
      throw new OpportunityValidationException("Cannot modify a closed opportunity");
    }
  }

  private void validateCanTransitionToClosed() {
    if (isClosed()) {
      throw new OpportunityValidationException("Opportunity is already closed");
    }
    if (stage == OpportunityStage.PROSPECTING) {
      throw new OpportunityValidationException(
          "Cannot close opportunity directly from prospecting stage");
    }
  }

  private void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public void softDelete() {
    if (!isClosed()) {
      throw new OpportunityValidationException("Only closed opportunities can be deleted");
    }
    super.softDelete();
  }
}
