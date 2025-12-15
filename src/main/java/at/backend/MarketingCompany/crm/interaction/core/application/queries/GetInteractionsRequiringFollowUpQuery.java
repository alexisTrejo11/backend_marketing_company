package at.backend.MarketingCompany.crm.interaction.core.application.queries;

import org.springframework.data.domain.Pageable;

public record GetInteractionsRequiringFollowUpQuery(Pageable pageable) {
}
