package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

public record GetInteractionStatisticsQuery(CustomerId customerId) {
    public static GetInteractionStatisticsQuery from(String customerId) {
        return new GetInteractionStatisticsQuery(new CustomerId(customerId));
    }
}
