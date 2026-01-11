package at.backend.MarketingCompany.crm.quote.core.application.queries;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record GetQuotesByCustomerQuery(
    CustomerCompanyId customerCompanyId,
    Pageable pageable) {
}
