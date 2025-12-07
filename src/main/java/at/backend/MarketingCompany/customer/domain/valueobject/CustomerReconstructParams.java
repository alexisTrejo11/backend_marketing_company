package at.backend.MarketingCompany.customer.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Set;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import lombok.Builder;

@Builder
public record CustomerReconstructParams(
    CustomerId id,
    PersonName personalInfo,
    ContactDetails contactDetails,
    BusinessProfile businessProfile,
    Set<OpportunityId> opportunityIds,
    Set<InteractionId> interactionIds,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {

}
