package at.backend.MarketingCompany.MarketingCampaing.crm.deal.persistence;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealStatus;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntity;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.model.DealEntityMapper;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.repository.DealRepositoryImpl;
import at.backend.MarketingCompany.crm.deal.adapter.output.persistence.repository.JpaDealRepository;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.external.*;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ DealEntityMapper.class, DealRepositoryImpl.class })
class DealRepositoryIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private JpaDealRepository jpaDealRepository;

  @Autowired
  private DealEntityMapper dealEntityMapper;

  @Autowired
  private DealRepositoryImpl dealRepository;

  private CustomerCompanyId customerCompanyId;
  private OpportunityId opportunityId;
  private List<ServicePackageId> serviceIds;
  private LocalDate startDate;

  @BeforeEach
  void setUp() {
    jpaDealRepository.deleteAll();

    customerCompanyId = CustomerCompanyId.generate();
    opportunityId = OpportunityId.generate();
    serviceIds = List.of(
        ServicePackageId.generate(),
        ServicePackageId.generate());
    startDate = LocalDate.now().plusDays(1);
  }

  private CreateDealParams createValidDealParams() {
    return CreateDealParams.builder()
        .customerCompanyId(customerCompanyId)
        .opportunityId(opportunityId)
        .startDate(startDate)
        .servicePackageIds(serviceIds)
        .build();
  }

  @Test
  void save_WithValidDeal_ShouldPersistSuccessfully() {
    // Given
    Deal deal = Deal.create(createValidDealParams());

    // When
    Deal savedDeal = dealRepository.save(deal);

    // Then
    assertThat(savedDeal).isNotNull();
    assertThat(savedDeal.getId()).isNotNull();
    assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.DRAFT);
    assertThat(savedDeal.getCustomerId()).isEqualTo(customerCompanyId);
    assertThat(savedDeal.getOpportunityId()).isEqualTo(opportunityId);
    assertThat(savedDeal.getServicePackageIds()).hasSize(2);

    // Verify in database
    Optional<DealEntity> entity = jpaDealRepository.findById(savedDeal.getId().value());
    assertThat(entity).isPresent();
    assertThat(entity.get().getDealStatus()).isEqualTo(DealStatus.DRAFT);
    assertThat(entity.get().getCustomerCompany().getId()).isEqualTo(customerCompanyId.value());
  }

  @Test
  void findById_WithExistingDeal_ShouldReturnDeal() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    Deal savedDeal = dealRepository.save(deal);

    // When
    Optional<Deal> foundDeal = dealRepository.findById(savedDeal.getId());

    // Then
    assertThat(foundDeal).isPresent();
    assertThat(foundDeal.get().getId()).isEqualTo(savedDeal.getId());
    assertThat(foundDeal.get().getDealStatus()).isEqualTo(DealStatus.DRAFT);
  }

  @Test
  void findById_WithNonExistentDeal_ShouldReturnEmpty() {
    // Given
    DealId nonExistentId = new DealId(UUID.randomUUID().toString());

    // When
    Optional<Deal> foundDeal = dealRepository.findById(nonExistentId);

    // Then
    assertThat(foundDeal).isEmpty();
  }

  @Test
  void findByCustomer_WithExistingDeals_ShouldReturnDeals() {
    // Given
    Deal deal1 = Deal.create(createValidDealParams());
    Deal deal2 = Deal.create(createValidDealParams());

    dealRepository.save(deal1);
    dealRepository.save(deal2);

    // When
    List<Deal> customerDeals = dealRepository.findByCustomer(customerCompanyId);

    // Then
    assertThat(customerDeals).hasSize(2);
    assertThat(customerDeals).allMatch(deal -> deal.getCustomerId().equals(customerCompanyId));
  }

  @Test
  void findByStatuses_WithMultipleStatuses_ShouldReturnFilteredDeals() {
    // Given
    Deal draftDeal = Deal.create(createValidDealParams());
    Deal signedDeal = Deal.create(createValidDealParams());
    signedDeal.startNegotiation();
    signedDeal.signDeal(
        new FinalAmount(new BigDecimal("5000.00")),
        "Payment terms",
        EmployeeId.generate());

    dealRepository.save(draftDeal);
    dealRepository.save(signedDeal);

    // When
    List<DealStatus> statuses = List.of(DealStatus.DRAFT, DealStatus.SIGNED);
    List<Deal> foundDeals = dealRepository.findByStatuses(statuses);

    // Then
    assertThat(foundDeals).hasSize(2);
    assertThat(foundDeals)
        .extracting(Deal::getDealStatus)
        .containsExactlyInAnyOrder(DealStatus.DRAFT, DealStatus.SIGNED);
  }

  @Test
  void save_WithSignedDeal_ShouldPersistAllFields() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    deal.startNegotiation();

    var finalAmount = new FinalAmount(new BigDecimal("7500.50"));
    var terms = "Payment in 30 days with 50% advance";
    var managerId = EmployeeId.generate();

    deal.signDeal(finalAmount, terms, managerId);

    // When
    Deal savedDeal = dealRepository.save(deal);

    // Then
    assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.SIGNED);
    assertThat(savedDeal.getFinalAmount()).contains(finalAmount);
    assertThat(savedDeal.getTerms()).contains(terms);
    assertThat(savedDeal.getCampaignManagerId()).contains(managerId);

    // Verify database
    DealEntity entity = jpaDealRepository.findById(savedDeal.getId().value()).orElseThrow();
    assertThat(entity.getFinalAmount()).isEqualByComparingTo("7500.50");
    assertThat(entity.getTerms()).isEqualTo(terms);
    assertThat(entity.getCampaignManager().getId()).isEqualTo(managerId.value());
  }

  @Test
  void save_WithCompletedDeal_ShouldPersistEndDateAndDeliverables() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    deal.startNegotiation();
    deal.signDeal(
        new FinalAmount(new BigDecimal("5000.00")),
        "Terms",
        EmployeeId.generate());
    deal.markAsPaid();
    deal.startExecution();

    LocalDate endDate = startDate.plusDays(30);
    String deliverables = "All services delivered and approved";

    deal.completeDeal(endDate, deliverables);

    // When
    Deal savedDeal = dealRepository.save(deal);

    // Then
    assertThat(savedDeal.getDealStatus()).isEqualTo(DealStatus.COMPLETED);
    assertThat(savedDeal.getPeriod().endDate()).contains(endDate);
    assertThat(savedDeal.getDeliverables()).contains(deliverables);

    // Verify database
    DealEntity entity = jpaDealRepository.findById(savedDeal.getId().value()).orElseThrow();
    assertThat(entity.getEndDate()).isEqualTo(endDate);
    assertThat(entity.getDeliverables()).isEqualTo(deliverables);
  }

  @Test
  void existsById_WithExistingDeal_ShouldReturnTrue() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    Deal savedDeal = dealRepository.save(deal);

    // When
    boolean exists = dealRepository.existsById(savedDeal.getId());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void existsById_WithNonExistentDeal_ShouldReturnFalse() {
    // Given
    DealId nonExistentId = new DealId(UUID.randomUUID().toString());

    // When
    boolean exists = dealRepository.existsById(nonExistentId);

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void delete_WithExistingDeal_ShouldRemoveFromDatabase() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    Deal savedDeal = dealRepository.save(deal);

    // When
    dealRepository.delete(savedDeal);

    // Then
    Optional<DealEntity> entity = jpaDealRepository.findById(savedDeal.getId().value());
    assertThat(entity).isEmpty();
  }

  @Test
  void updateServicePackages_ShouldPersistChanges() {
    // Given
    Deal deal = Deal.create(createValidDealParams());
    Deal savedDeal = dealRepository.save(deal);

    var newServices = List.of(
        ServicePackageId.generate(),
        ServicePackageId.generate(),
        ServicePackageId.generate());

    savedDeal.updateServicePackages(newServices);

    // When
    Deal updatedDeal = dealRepository.save(savedDeal);

    // Then
    assertThat(updatedDeal.getServicePackageIds()).hasSize(3);
    assertThat(updatedDeal.getServicePackageIds()).isEqualTo(newServices);

    // Verify database
    DealEntity entity = jpaDealRepository.findById(updatedDeal.getId().value()).orElseThrow();
    assertThat(entity.getServices()).hasSize(3);
  }
}
