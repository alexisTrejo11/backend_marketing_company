package at.backend.MarketingCompany.crm.servicePackage.v2.infrastructure.persistence.model;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.Utils.enums.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.Frequency;
import at.backend.MarketingCompany.crm.Utils.enums.SocialNetworkPlatform;
import at.backend.MarketingCompany.crm.Utils.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    public ServicePackageEntity(String id) {
        this.id = id;
    }
}
