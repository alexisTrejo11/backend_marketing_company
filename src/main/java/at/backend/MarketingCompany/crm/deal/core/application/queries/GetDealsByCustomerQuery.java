package at.backend.MarketingCompany.crm.deal.core.application.queries;

import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.dto.PageInput;

public record GetDealsByCustomerQuery(CustomerCompanyId customerCompanyId, Pageable pageable) {
  public static GetDealsByCustomerQuery from(String id, PageInput pageInput) {
    return new GetDealsByCustomerQuery(new CustomerCompanyId(id), pageInput.toPageable());
  }
}
