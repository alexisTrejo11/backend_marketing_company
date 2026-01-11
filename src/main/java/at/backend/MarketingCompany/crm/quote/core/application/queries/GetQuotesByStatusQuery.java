package at.backend.MarketingCompany.crm.quote.core.application.queries;

import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public record GetQuotesByStatusQuery(
    QuoteStatus status,
    Pageable pageable) {
}
