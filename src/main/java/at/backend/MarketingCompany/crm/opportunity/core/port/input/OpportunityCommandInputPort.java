package at.backend.MarketingCompany.crm.opportunity.core.port.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;

public interface OpportunityCommandInputPort {
  Opportunity createOpportunity(CreateOpportunityCommand command);

  Opportunity updateOpportunityDetails(UpdateOpportunityDetailsCommand command);

  Opportunity qualifyOpportunity(QualifyOpportunityCommand command);

  Opportunity moveToProposal(MoveToProposalCommand command);

  Opportunity moveToNegotiation(MoveToNegotiationCommand command);

  Opportunity closeOpportunityWon(CloseOpportunityWonCommand command);

  Opportunity closeOpportunityLost(CloseOpportunityLostCommand command);

  Opportunity reopenOpportunity(ReopenOpportunityCommand command);

  Opportunity updateOpportunityAmount(UpdateOpportunityAmountCommand command);

  Opportunity updateOpportunityProbability(UpdateOpportunityProbabilityCommand command);

  void deleteOpportunity(DeleteOpportunityCommand command);

}
