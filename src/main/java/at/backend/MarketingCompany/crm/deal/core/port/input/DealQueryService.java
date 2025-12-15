package at.backend.MarketingCompany.crm.deal.core.port.input;

import org.springframework.data.domain.Page;
import at.backend.MarketingCompany.crm.deal.core.application.queries.*;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;

public interface DealQueryService {
  Page<Deal> getAllDeals(GetAllDealsQuery query);

  Deal getDealById(GetDealByIdQuery query);

  Page<Deal> getDealsByCustomer(GetDealsByCustomerQuery query);

  Page<Deal> getDealsByStatus(GetDealsByStatusQuery query);
}
