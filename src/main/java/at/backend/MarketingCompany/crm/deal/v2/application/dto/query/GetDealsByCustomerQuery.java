package at.backend.MarketingCompany.crm.deal.v2.application.dto.query;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.external.CustomerId;

public record GetDealsByCustomerQuery(CustomerId customerId) {}
