package at.backend.MarketingCompany.crm.deal.repository.persistence.model;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.opportunity.domain.Opportunity;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.user.api.Model.User;
import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deals")
public class DealEntity extends BaseJpaEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "deal_status", nullable = false)
    private DealStatus dealStatus;

    @Column(name = "final_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal finalAmount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "campaign_manager_id")
    private User campaignManager;

    @Column(name = "deliverables", columnDefinition = "TEXT")
    private String deliverables;

    @Column(name = "terms", columnDefinition = "TEXT")
    private String terms;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customerModel;

    @ManyToOne
    @JoinColumn(name = "opportunity_id", nullable = false)
    private Opportunity opportunity;

    @ManyToMany
    @JoinTable(
            name = "deal_service_packages",
            joinColumns = @JoinColumn(name = "deal_id"),
            inverseJoinColumns = @JoinColumn(name = "service_package_id"))
    private List<ServicePackageEntity> services;
}

