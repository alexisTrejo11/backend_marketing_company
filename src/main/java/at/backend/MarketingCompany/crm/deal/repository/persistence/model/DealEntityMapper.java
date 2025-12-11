package at.backend.MarketingCompany.crm.deal.repository.persistence.model;

import at.backend.MarketingCompany.account.user.adapters.outbound.persistence.UserEntity;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.persistence.model.ServicePackageEntity;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DealEntityMapper {
  public DealEntity toEntity(Deal deal) {
    if (deal == null)
      return null;

    DealEntity entity = new DealEntity();
    if (deal.getId() != null)
      entity.setId(deal.getId().asString());
    entity.setDealStatus(deal.getDealStatus());
    entity.setStartDate(deal.getPeriod().startDate());
    entity.setEndDate(deal.getPeriod().endDate().orElse(null));

    // Optional
    deal.getFinalAmount().ifPresent(amount -> entity.setFinalAmount(amount.value()));

    deal.getDeliverables().ifPresent(entity::setDeliverables);
    deal.getTerms().ifPresent(entity::setTerms);

    // Audit fields
    entity.setCreatedAt(deal.getCreatedAt());
    entity.setUpdatedAt(deal.getUpdatedAt());
    entity.setDeletedAt(deal.getDeletedAt());
    entity.setVersion(deal.getVersion());

    // Relations
    if (deal.getCustomerId() != null) {
      var customer = new CustomerCompanyEntity(deal.getCustomerId().value());
      entity.setCustomerCompany(customer);
    }

    if (deal.getOpportunityId() != null) {
      var opportunity = new OpportunityEntity((deal.getOpportunityId().value()));
      entity.setOpportunity(opportunity);
    }

    if (deal.getCampaignManagerId().isPresent()) {
      var employee = new UserEntity(deal.getCampaignManagerId().get().value());
      entity.setCampaignManager(employee);
    }

    if (deal.getServicePackageIds() != null) {
      var services = deal.getServicePackageIds().stream()
          .map(serviceId -> new ServicePackageEntity(serviceId.value()))
          .collect(Collectors.toList());
      entity.setServices(services);
    }

    return entity;
  }

  public Deal toDomain(DealEntity entity) {
    if (entity == null)
      return null;

    var reconstructParams = DealReconstructParams.builder()
        .id(DealId.from(entity.getId()))
        .customerCompanyId(entity.getCustomerCompany() != null
            ? new CustomerCompanyId(entity.getCustomerCompany().getId())
            : null)
        .opportunityId(entity.getOpportunity() != null
            ? new OpportunityId(entity.getOpportunity().getId())
            : null)
        .dealStatus(entity.getDealStatus())
        .finalAmount(entity.getFinalAmount() != null ? new FinalAmount(entity.getFinalAmount())
            : null)
        .campaignManagerId(entity.getCampaignManager() != null
            ? new EmployeeId(entity.getCampaignManager().getId())
            : null)
        .deliverables(entity.getDeliverables())
        .terms(entity.getTerms())
        .servicePackageIds(entity.getServices() != null ? entity.getServices().stream()
            .map(service -> new ServicePackageId(service.getId()))
            .collect(Collectors.toList()) : List.of())
        .version(entity.getVersion())
        .period(new ContractPeriod(entity.getStartDate(),
            Optional.ofNullable(entity.getEndDate())))
        .deletedAt(entity.getDeletedAt())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();

    return Deal.reconstruct(reconstructParams);
  }

  public void updateEntity(DealEntity existingEntity, Deal deal) {
    existingEntity.setDealStatus(deal.getDealStatus());
    existingEntity.setStartDate(deal.getPeriod().startDate());
    existingEntity.setEndDate(deal.getPeriod().endDate().orElse(null));

    deal.getFinalAmount().ifPresentOrElse(
        amount -> existingEntity.setFinalAmount(amount.value()),
        () -> existingEntity.setFinalAmount(null));

    deal.getDeliverables().ifPresentOrElse(
        existingEntity::setDeliverables,
        () -> existingEntity.setDeliverables(null));

    deal.getTerms().ifPresentOrElse(
        existingEntity::setTerms,
        () -> existingEntity.setTerms(null));

  }
}
