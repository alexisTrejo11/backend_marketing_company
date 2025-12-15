package at.backend.MarketingCompany.MarketingCampaing.crm.deal.persistence;

import at.backend.MarketingCompany.crm.opportunity.adapter.output.persistence.OpportunityEntity;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.account.user.adapters.outbound.persistence.UserEntity;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntity;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntityMapper;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.persistence.model.ServicePackageEntity;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DealEntityMapperTest {

  private DealEntityMapper mapper;
  private CustomerCompanyId customerCompanyId;
  private OpportunityId opportunityId;
  private List<ServicePackageId> serviceIds;
  private LocalDate startDate;

  @BeforeEach
  void setUp() {
    mapper = new DealEntityMapper();

    customerCompanyId = CustomerCompanyId.generate();
    opportunityId = OpportunityId.generate();
    serviceIds = List.of(
        new ServicePackageId(UUID.randomUUID().toString()),
        new ServicePackageId(UUID.randomUUID().toString()));
    startDate = LocalDate.now().plusDays(1);
  }

  @Test
  void toEntity_WithValidDeal_ShouldMapCorrectly() {
    // Given
    var createParams = CreateDealParams.builder()
        .customerCompanyId(customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(serviceIds)
        .build();

    Deal deal = Deal.create(createParams);

    // When
    DealEntity entity = mapper.toEntity(deal);

    // Then
    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isEqualTo(deal.getId().value());
    assertThat(entity.getDealStatus()).isEqualTo(DealStatus.DRAFT);
    assertThat(entity.getStartDate()).isEqualTo(startDate);
    assertThat(entity.getEndDate()).isNull();
    assertThat(entity.getFinalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(entity.getCustomerCompany().getId()).isEqualTo(customerCompanyId.value());
    assertThat(entity.getOpportunity().getId()).isEqualTo(opportunityId.value());
    assertThat(entity.getServices()).hasSize(2);
  }

  @Test
  void toEntity_WithSignedDeal_ShouldMapAllFields() {
    // Given
    var createParams = CreateDealParams.builder()
        .customerCompanyId(
            customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(serviceIds)
        .build();

    Deal deal = Deal.create(createParams);
    deal.startNegotiation();

    var finalAmount = new FinalAmount(new BigDecimal("5000.00"));
    var terms = "Payment terms";
    var managerId = EmployeeId.generate();

    deal.signDeal(finalAmount, terms, managerId);

    // When
    DealEntity entity = mapper.toEntity(deal);

    // Then
    assertThat(entity.getDealStatus()).isEqualTo(DealStatus.SIGNED);
    assertThat(entity.getFinalAmount()).isEqualByComparingTo("5000.00");
    assertThat(entity.getTerms()).isEqualTo(terms);
    assertThat(entity.getCampaignManager().getId()).isEqualTo(managerId.value());
  }

  @Test
  void toDomain_WithValidEntity_ShouldMapCorrectly() {
    // Given
    DealEntity entity = new DealEntity();
    entity.setId(UUID.randomUUID().toString());
    entity.setDealStatus(DealStatus.SIGNED);
    entity.setStartDate(startDate);
    entity.setEndDate(startDate.plusDays(30));
    entity.setFinalAmount(new BigDecimal("5000.00"));
    entity.setTerms("Payment terms");
    entity.setDeliverables("Project deliverables");
    entity.setCreatedAt(LocalDateTime.now().minusDays(1));
    entity.setUpdatedAt(LocalDateTime.now());
    entity.setVersion(1);

    // Set up relationships with mock entity
    var customer = new CustomerCompanyEntity(customerCompanyId.value());
    var opportunity = new OpportunityEntity(UUID.randomUUID().toString());
    var manager = new UserEntity(UUID.randomUUID().toString());
    var services = serviceIds.stream()
        .map(id -> new ServicePackageEntity(id.value()))
        .toList();

    entity.setCustomerCompany(customer);
    entity.setOpportunity(opportunity);
    entity.setCampaignManager(manager);
    entity.setServices(services);

    // When
    Deal deal = mapper.toDomain(entity);

    // Then
    assertThat(deal).isNotNull();
    assertThat(deal.getId().value()).isEqualTo(entity.getId());
    assertThat(deal.getDealStatus()).isEqualTo(DealStatus.SIGNED);
    assertThat(deal.getCustomerId().value()).isEqualTo(customerCompanyId.value());
    assertThat(deal.getOpportunityId().value()).isEqualTo(opportunityId.value());
    assertThat(deal.getServicePackageIds()).hasSize(2);
    assertThat(deal.getFinalAmount()).isPresent();
    assertThat(deal.getTerms()).isPresent();
    assertThat(deal.getDeliverables()).isPresent();
  }

  @Test
  void toEntity_and_toDomain_ShouldBeReversible() {
    // Given
    var createParams = CreateDealParams.builder()
        .customerCompanyId(customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(serviceIds)
        .build();

    Deal originalDeal = Deal.create(createParams);
    originalDeal.startNegotiation();
    originalDeal.signDeal(
        new FinalAmount(new BigDecimal("3000.00")),
        "Test terms",
        EmployeeId.generate());

    // When
    DealEntity entity = mapper.toEntity(originalDeal);
    Deal restoredDeal = mapper.toDomain(entity);

    // Then
    assertThat(restoredDeal.getId()).isEqualTo(originalDeal.getId());
    assertThat(restoredDeal.getDealStatus()).isEqualTo(originalDeal.getDealStatus());
    assertThat(restoredDeal.getCustomerId()).isEqualTo(originalDeal.getCustomerId());
    assertThat(restoredDeal.getOpportunityId()).isEqualTo(originalDeal.getOpportunityId());
    assertThat(restoredDeal.getServicePackageIds()).isEqualTo(originalDeal.getServicePackageIds());
    assertThat(restoredDeal.getFinalAmount()).isEqualTo(originalDeal.getFinalAmount());
    assertThat(restoredDeal.getTerms()).isEqualTo(originalDeal.getTerms());
  }

  @Test
  void updateEntity_ShouldUpdateOnlyAllowedFields() {
    // Given
    var createParams = CreateDealParams.builder()
        .customerCompanyId(customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(serviceIds)
        .build();

    Deal originalDeal = Deal.create(createParams);
    DealEntity entity = mapper.toEntity(originalDeal);

    // Modify deal
    originalDeal.startNegotiation();
    originalDeal.signDeal(
        new FinalAmount(new BigDecimal("4000.00")),
        "Updated terms",
        EmployeeId.generate());

    // When
    mapper.updateEntity(entity, originalDeal);

    // Then
    assertThat(entity.getDealStatus()).isEqualTo(DealStatus.SIGNED);
    assertThat(entity.getFinalAmount()).isEqualByComparingTo("4000.00");
    assertThat(entity.getTerms()).isEqualTo("Updated terms");
    // ID and relationships should remain unchanged
    assertThat(entity.getId()).isEqualTo(originalDeal.getId().value());
    assertThat(entity.getCustomerCompany().getId()).isEqualTo(customerCompanyId.value());
  }
}
