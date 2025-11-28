package at.backend.MarketingCompany.crm.opportunity.infrastructure.graphql.dto;

public record OpportunityFilterInput(
    String searchTerm,
    java.util.List<String> stages,
    String customerId
) {}
