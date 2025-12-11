package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import at.backend.MarketingCompany.crm.interaction.infrastructure.persistence.InteractionEntity;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import at.backend.MarketingCompany.customer.domain.valueobject.AnnualRevenue;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer_companies",
        indexes = {
                @Index(name = "idx_company_name", columnList = "company_name"),
                @Index(name = "idx_industry", columnList = "industry_code"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_tax_id", columnList = "tax_id", unique = true),
                @Index(name = "idx_company_size", columnList = "company_size")
        })
public class CustomerCompanyEntity extends BaseJpaEntity {
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "tax_id", unique = true)
    private String taxId;

    @Column(name = "website")
    private String website;

    @Column(name = "founding_year")
    private Integer foundingYear;

    @Column(name = "industry_code", nullable = false)
    private String industryCode;

    @Column(name = "industry_name")
    private String industryName;

    @Column(name = "sector")
    private String sector;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_size")
    private CompanySize companySize;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "annual_revenue_amount", precision = 15, scale = 2)
    private BigDecimal annualRevenueAmount;

    @Column(name = "annual_revenue_currency", length = 3)
    private String annualRevenueCurrency;

    @Column(name = "revenue_range")
    private AnnualRevenue.RevenueRange revenueRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CompanyStatus status;

    @Column(name = "is_public_company")
    private Boolean isPublicCompany;

    @Column(name = "is_startup")
    private Boolean isStartup;

    @Column(name = "target_market", length = 1000)
    private String targetMarket;

    @Column(name = "mission_statement", length = 2000)
    private String missionStatement;

    @ElementCollection
    @CollectionTable(name = "company_key_products",
            joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "product")
    private Set<String> keyProducts = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "company_competitors",
            joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "competitor_url")
    private Set<String> competitorUrls = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "primary_contact_first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "primary_contact_last_name")),
            @AttributeOverride(name = "email", column = @Column(name = "primary_contact_email")),
            @AttributeOverride(name = "phone", column = @Column(name = "primary_contact_phone")),
            @AttributeOverride(name = "position", column = @Column(name = "primary_contact_position")),
            @AttributeOverride(name = "department", column = @Column(name = "primary_contact_department")),
            @AttributeOverride(name = "isDecisionMaker", column = @Column(name = "primary_contact_decision_maker")),
            @AttributeOverride(name = "isPrimaryContact", column = @Column(name = "primary_contact_is_primary"))
    })
    private ContactPersonEmbeddable primaryContact;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactPersonEntity> contactPersons = new HashSet<>();

    @Embedded
    private ContractDetailsEmbeddable contractDetails;

    @Embedded
    private BillingInformationEmbeddable billingInformation;

    @OneToMany(mappedBy = "customerCompany", fetch = FetchType.LAZY)
    private Set<OpportunityEntity> opportunities = new HashSet<>();

    @OneToMany(mappedBy = "customerCompany", fetch = FetchType.LAZY)
    private Set<InteractionEntity> interactions = new HashSet<>();

    @Embedded
    private SocialMediaEmbeddable socialMedia;


    public CustomerCompanyEntity(String id) {
        this.id = id;
    }
}