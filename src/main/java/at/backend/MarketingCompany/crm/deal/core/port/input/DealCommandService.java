package at.backend.MarketingCompany.crm.deal.core.port.input;

import at.backend.MarketingCompany.crm.deal.core.application.commands.*;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;

public interface DealCommandService {
  Deal createDeal(CreateDealCommand command);

  Deal signDeal(SignDealCommand command);

  Deal markDealAsPaid(MarkDealAsPaidCommand command);

  Deal startDealExecution(StartDealExecutionCommand command);

  Deal completeDeal(CompleteDealCommand command);

  Deal cancelDeal(CancelDealCommand command);

  Deal updateDealServices(UpdateDealServicesCommand command);
}
