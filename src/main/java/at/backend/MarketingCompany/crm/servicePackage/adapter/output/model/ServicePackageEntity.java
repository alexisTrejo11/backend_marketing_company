package at.backend.MarketingCompany.crm.servicePackage.adapter.output.model;

import java.math.BigDecimal;
import java.util.List;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Frequency;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "services_packages")
public class ServicePackageEntity extends BaseJpaEntity {
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(name = "service_type", nullable = false, length = 50)
  private ServiceType serviceType;

  @Column(name = "deliverables", columnDefinition = "TEXT")
  private String deliverables;

  @Column(name = "estimated_hours", nullable = false)
  private Integer estimatedHours;

  @Enumerated(EnumType.STRING)
  @Column(name = "complexity", nullable = false, length = 20)
  private Complexity complexity;

  @Column(name = "is_recurring", nullable = false)
  private Boolean isRecurring;

  @Enumerated(EnumType.STRING)
  @Column(name = "frequency", length = 50)
  private Frequency frequency;

  @Column(name = "project_duration")
  private Integer projectDuration;

  @ElementCollection
  @CollectionTable(name = "marketing_service_kpis", joinColumns = @JoinColumn(name = "service_id"))
  @Column(name = "kpi", nullable = false)
  private List<String> kpis;

  @ElementCollection
  @CollectionTable(name = "marketing_service_platforms", joinColumns = @JoinColumn(name = "service_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "platform", nullable = false)
  private List<SocialNetworkPlatform> socialNetworkPlatforms;

  @Column(name = "active", nullable = false)
  private Boolean active;

  public ServicePackageEntity(Long id) {
    this.id = id;
  }
}
