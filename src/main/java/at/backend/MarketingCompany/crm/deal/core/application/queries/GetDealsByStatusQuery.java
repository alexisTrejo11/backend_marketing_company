package at.backend.MarketingCompany.crm.deal.core.application.queries;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealStatus;

import java.util.Set;

import org.springframework.data.domain.Pageable;

public record GetDealsByStatusQuery(Set<DealStatus> statuses, Pageable pageable) {
}
