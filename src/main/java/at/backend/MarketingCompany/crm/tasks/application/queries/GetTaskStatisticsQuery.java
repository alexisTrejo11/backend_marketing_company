package at.backend.MarketingCompany.crm.tasks.application.queries;

public record GetTaskStatisticsQuery(
    String customerId,
    String assigneeId
) {}
