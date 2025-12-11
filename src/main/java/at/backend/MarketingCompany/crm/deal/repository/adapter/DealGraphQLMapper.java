package at.backend.MarketingCompany.crm.deal.repository.adapter;

import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.FinalAmount;
import at.backend.MarketingCompany.crm.deal.repository.dto.output.DealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DealGraphQLMapper {
    
    //private final ExternalDataFetcher externalDataFetcher;

    public DealResponse toGraphQLResponse(Deal deal) {
        /*
        // Fetch datos externos para nested objects
        var customer = externalDataFetcher.fetchCustomerInfo(deal.customerCompanyId());
        var opportunity = externalDataFetcher.fetchOpportunityInfo(deal.opportunityId());
        var campaignManager = deal.campaignManagerId() != null ? 
            externalDataFetcher.fetchUserInfo(deal.campaignManagerId()) : null;
        var services = externalDataFetcher.fetchServicePackagesInfo(deal.servicePackageIds());

         */
        return DealResponse.builder()
            .id(deal.getId().value())
            .customerId(deal.getCustomerId() != null ? deal.getCustomerId().value().toString() : null)
            //.customer(customer)
            .opportunityId(deal.getOpportunityId() != null ? deal.getOpportunityId().value().toString() : null)
            //.opportunity(opportunity)
            .dealStatus(deal.getDealStatus().name())
            .finalAmount(deal.getFinalAmount().map(FinalAmount::value).orElse(null))
            .startDate(deal.getPeriod().startDate())
            .endDate(deal.getPeriod().endDate().isPresent() ? deal.getPeriod().endDate().get() : null)
            .campaignManagerId(deal.getCampaignManagerId().isPresent() ? deal.getCampaignManagerId().get().value().toString() : null)
            //.campaignManager(campaignManager)
            //.services(services)
            .deliverables(deal.getDeliverables().orElse(null))
            .terms(deal.getTerms().orElse(null))
            .createdAt(deal.getCreatedAt())
            .updatedAt(deal.getUpdatedAt())
            .build();
    }

    public Page<DealResponse> toPagedResponse(List<DealResponse> deals, Pageable pageable) {
        // Implementar lógica de paginación real
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), deals.size());
        
        var pagedContent = deals.subList(start, end);
        return new PageImpl<>(pagedContent, pageable, deals.size());
    }
}