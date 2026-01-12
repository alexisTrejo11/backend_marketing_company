package at.backend.MarketingCompany.crm.quote.core.application.queries;

import org.springframework.data.domain.Pageable;

public record GetAllQuotesQuery(Pageable pageable) {
}
