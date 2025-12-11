package at.backend.MarketingCompany.crm.interaction.infrastructure.graphql.dto;

import at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.output.CustomerInfo;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;

public interface ExternalDataFetcher {
  CustomerInfo fetchCustomerInfo(CustomerCompanyId customerCompanyId);
}
