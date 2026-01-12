package at.backend.MarketingCompany.crm.deal.core.application.queries;

import org.springframework.data.domain.Pageable;

public record GetAllDealsQuery(Pageable pageable) {

}
