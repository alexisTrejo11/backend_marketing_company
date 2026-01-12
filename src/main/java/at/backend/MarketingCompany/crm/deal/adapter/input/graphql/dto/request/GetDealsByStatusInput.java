package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.util.Set;

import at.backend.MarketingCompany.crm.deal.core.application.queries.GetDealsByStatusQuery;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealStatus;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GetDealsByStatusInput(
    @NotNull @Size(min = 1) Set<DealStatus> statuses,
    @NotNull PageInput pageInput) {
		public GetDealsByStatusQuery toQuery() {
			return new GetDealsByStatusQuery(
					statuses,
					pageInput.toPageable());
		}
}
