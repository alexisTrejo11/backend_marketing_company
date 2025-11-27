package at.backend.MarketingCompany.crm.deal.application;

import at.backend.MarketingCompany.crm.deal.application.commands.*;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealByIdQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByCustomerQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByStatusQuery;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;

import java.util.List;

public interface DealApplicationService {
    Deal handle(CreateDealCommand command);
    Deal handle(SignDealCommand command);
    Deal handle(MarkDealAsPaidCommand command);
    Deal handle(StartDealExecutionCommand command);
    Deal handle(CompleteDealCommand command);
    Deal handle(CancelDealCommand command);
    Deal handle(UpdateDealServicesCommand command);
    Deal handle(GetDealByIdQuery query);
    List<Deal> handle(GetDealsByCustomerQuery query);
    List<Deal> handle(GetDealsByStatusQuery query);
}
