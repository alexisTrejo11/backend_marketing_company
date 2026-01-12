package at.backend.MarketingCompany.crm.opportunity.core.application.commands;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.ExpectedCloseDate;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.NextSteps;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Probability;

public record UpdateOpportunityDetailsCommand(
    OpportunityId opportunityId,
    String title,
    Amount amount,
    ExpectedCloseDate expectedCloseDate,
    NextSteps nextSteps,
    Probability probability) {

  public UpdateOpportunityDetailsCommand {
    if (opportunityId == null) {
      throw new IllegalArgumentException("OpportunityId cannot be null");
    }
  }

  public static UpdateOpportunityDetailsCommand from(
      String opportunityId,
      String title,
      BigDecimal amount,
      LocalDate expectedCloseDate,
      String nextValue,
      LocalDateTime nextStepsDueDate,
      Integer probabilityValue) {

    Amount amt = null;
    if (amount != null) {
      amt = Amount.create(amount);
    }

    ExpectedCloseDate expCloseDate = null;
    if (expectedCloseDate != null) {
      expCloseDate = ExpectedCloseDate.create(expectedCloseDate);
    }

    NextSteps nextSteps = null;
    if (nextValue != null || nextStepsDueDate != null) {
      nextSteps = NextSteps.create(nextValue, nextStepsDueDate);
    }

    Probability prob = null;
    if (probabilityValue != null) {
      prob = Probability.of(probabilityValue);
    }

    return new UpdateOpportunityDetailsCommand(
        OpportunityId.of(opportunityId),
        title,
        amt,
        expCloseDate,
        nextSteps,
        prob);
  }
}
