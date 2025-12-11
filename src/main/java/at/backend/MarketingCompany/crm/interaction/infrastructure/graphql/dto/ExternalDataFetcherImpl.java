package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto;

import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.CustomerInfo;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.stereotype.Component;

@Component
public class ExternalDataFetcherImpl implements ExternalDataFetcher {
  @Override
  public CustomerInfo fetchCustomerInfo(CustomerCompanyId customerCompanyId) {
    return null;
  }
}
