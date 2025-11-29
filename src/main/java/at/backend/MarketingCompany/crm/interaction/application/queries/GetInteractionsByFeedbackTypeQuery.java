package at.backend.MarketingCompany.crm.interaction.application.queries;

import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import org.springframework.data.domain.Pageable;

public record GetInteractionsByFeedbackTypeQuery(FeedbackType feedbackType, Pageable pageable) {}
