package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.shared.dto.PageInput;

import org.springframework.data.domain.Pageable;

public record GetInteractionsByFeedbackTypeQuery(FeedbackType feedbackType, Pageable pageable) {

  public static GetInteractionsByFeedbackTypeQuery of(FeedbackType feedbackType, PageInput pageable) {
    return new GetInteractionsByFeedbackTypeQuery(feedbackType, pageable.toPageable());
  }
}
