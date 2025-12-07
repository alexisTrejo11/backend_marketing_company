package at.backend.MarketingCompany.customer.application.port.input;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.customer.application.dto.query.GetAllCustomersQuery;
import at.backend.MarketingCompany.customer.application.dto.query.GetCustomerByIdQuery;
import at.backend.MarketingCompany.customer.application.dto.query.HasCompetitorsQuery;
import at.backend.MarketingCompany.customer.application.dto.query.HasSocialMediaHandlesQuery;
import at.backend.MarketingCompany.customer.application.dto.query.IsCustomerBlockedQuery;
import at.backend.MarketingCompany.customer.domain.Customer;

public interface CustomerQueryHandler {
  Page<Customer> handle(GetAllCustomersQuery query);

  Customer handle(GetCustomerByIdQuery query);

  boolean handle(IsCustomerBlockedQuery query);

  boolean handle(HasSocialMediaHandlesQuery query);

  boolean handle(HasCompetitorsQuery query);
}
