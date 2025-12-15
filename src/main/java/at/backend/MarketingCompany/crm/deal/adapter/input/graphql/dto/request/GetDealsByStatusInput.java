package at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request;

import java.util.Set;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.shared.dto.PageInput;

public record GetDealsByStatusInput(
    Set<DealStatus> statuses,
    PageInput pageInput) {

}
