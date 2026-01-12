package at.backend.MarketingCompany.crm.interaction.core.ports.input;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.crm.interaction.core.application.analytics.CustomerInteractionAnalytics;
import at.backend.MarketingCompany.crm.interaction.core.application.analytics.InteractionStatistics;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetCustomerInteractionAnalyticsQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionByIdQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionStatisticsQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByCustomerAndTypeQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByCustomerQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByDateRangeQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByFeedbackTypeQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsByTypeQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetInteractionsRequiringFollowUpQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetNegativeInteractionsQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetPositiveInteractionsQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetRecentInteractionsQuery;
import at.backend.MarketingCompany.crm.interaction.core.application.queries.GetTodayInteractionsQuery;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;

public interface InteractionQueryService {
  Interaction getInteractionById(GetInteractionByIdQuery query);

  Page<Interaction> getInteractionsByCustomer(GetInteractionsByCustomerQuery query);

  Page<Interaction> getInteractionsByType(GetInteractionsByTypeQuery query);

  Page<Interaction> getInteractionsByFeedbackType(GetInteractionsByFeedbackTypeQuery query);

  Page<Interaction> getInteractionsByCustomerAndType(GetInteractionsByCustomerAndTypeQuery query);

  Page<Interaction> getInteractionsByDateRange(GetInteractionsByDateRangeQuery query);

  Page<Interaction> getRecentInteractions(GetRecentInteractionsQuery query);

  Page<Interaction> getTodayInteractions(GetTodayInteractionsQuery query);

  Page<Interaction> getInteractionsRequiringFollowUp(GetInteractionsRequiringFollowUpQuery query);

  Page<Interaction> getPositiveInteractions(GetPositiveInteractionsQuery query);

  Page<Interaction> getNegativeInteractions(GetNegativeInteractionsQuery query);

  InteractionStatistics getInteractionStatistics(GetInteractionStatisticsQuery query);

  CustomerInteractionAnalytics getCustomerInteractionAnalytics(GetCustomerInteractionAnalyticsQuery query);
}
