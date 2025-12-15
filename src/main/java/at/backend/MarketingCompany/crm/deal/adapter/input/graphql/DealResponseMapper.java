package at.backend.MarketingCompany.crm.deal.adapter.input.graphql;

import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.response.DealResponse;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.FinalAmount;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DealResponseMapper {
  public DealResponse toResponse(Deal deal) {
    return DealResponse.builder()
        .id(deal.getId().value())
        .dealStatus(deal.getDealStatus().name())
        .finalAmount(deal.getFinalAmount().map(FinalAmount::value).orElse(null))
        .startDate(deal.getPeriod().startDate())
        .endDate(deal.getPeriod().endDate().isPresent() ? deal.getPeriod().endDate().get() : null)
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
