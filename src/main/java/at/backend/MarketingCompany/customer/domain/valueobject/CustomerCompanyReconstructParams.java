package at.backend.MarketingCompany.customer.domain.valueobject;

import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.InteractionId;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record CustomerCompanyReconstructParams(
    CustomerCompanyId id,
    CompanyName companyName,
    CompanyProfile companyProfile,
    CompanyStatus status,
    Set<ContactPerson> contactPersons,
    Set<OpportunityId> opportunities,
    Set<InteractionId> interactions,
    BillingInformation billingInfo,
    ContractDetails contractDetails,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
