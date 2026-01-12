package at.backend.MarketingCompany.crm.deal.adapter.input.graphql;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.response.DealResponse;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.FinalAmount;
import at.backend.MarketingCompany.shared.PageResponse;

@Component
public class DealResponseMapper {
  public DealResponse toResponse(Deal deal) {
    return DealResponse.builder()
        .id(deal.getId().asString())
        .status(deal.getDealStatus().name())
        .finalAmount(deal.getFinalAmount().map(FinalAmount::value).orElse(null))
        .startDate(deal.getPeriod().startDate())
        .endDate(deal.getPeriod().endDate().isPresent() ? deal.getPeriod().endDate().get() : null)
        .opportunityId(deal.getOpportunityId() != null ? deal.getOpportunityId().getValue().toString() : null)
        .campaignManagerId(
            deal.getCampaignManagerId().isPresent() ? deal.getCampaignManagerId().get().value().toString() : null)
        .customerCompanyId(deal.getCustomerId() != null ? deal.getCustomerId().getValue().toString() : null)
        .deliverables(deal.getDeliverables().orElse(null))
        .terms(deal.getTerms().orElse(null))
        .createdAt(deal.getCreatedAt())
        .updatedAt(deal.getUpdatedAt())
        .build();
  }

  public List<DealResponse> toResponses(List<Deal> deals) {
    if (deals == null)
      return List.of();

    return deals.stream()
        .map(this::toResponse)
        .toList();
  }

  public PageResponse<DealResponse> toPagedResponse(Page<Deal> dealsPage) {
    if (dealsPage == null)
      return PageResponse.empty();

    Page<DealResponse> responsePage = dealsPage.map(this::toResponse);
    return PageResponse.of(responsePage);
  }
}
