package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto.input;

import at.backend.MarketingCompany.shared.dto.PageInput;

import java.util.List;

public record OpportunityFilterInput(
    String searchTerm,
    List<String> stages,
    String customerId,
    PageInput pageInput
) {}
