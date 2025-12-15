package at.backend.MarketingCompany.crm.opportunity.core.port.input;

import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CloseOpportunityLostCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CloseOpportunityWonCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.CreateOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.DeleteOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.MoveToNegotiationCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.MoveToProposalCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.QualifyOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.ReopenOpportunityCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityAmountCommand;
import at.backend.MarketingCompany.crm.opportunity.core.application.commands.UpdateOpportunityDetailsCommand;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.Opportunity;

public interface OpportunityCommandService {
  Opportunity createOpportunity(CreateOpportunityCommand command);

  Opportunity updateOpportunityDetails(UpdateOpportunityDetailsCommand command);

  Opportunity qualifyOpportunity(QualifyOpportunityCommand command);

  Opportunity moveToProposal(MoveToProposalCommand command);

  Opportunity moveToNegotiation(MoveToNegotiationCommand command);

  Opportunity closeOpportunityWon(CloseOpportunityWonCommand command);

  Opportunity closeOpportunityLost(CloseOpportunityLostCommand command);

  Opportunity reopenOpportunity(ReopenOpportunityCommand command);

  Opportunity updateOpportunityAmount(UpdateOpportunityAmountCommand command);

  void deleteOpportunity(DeleteOpportunityCommand command);

}
