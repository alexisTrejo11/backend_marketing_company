package at.backend.MarketingCompany.customer.api.repository;

import at.backend.MarketingCompany.crm.interaction.domain.Interaction;
import at.backend.MarketingCompany.crm.opportunity.domain.Opportunity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "company")
    private String company;

    @Column(name = "industry")
    private String industry;

    @Column(name = "brand_voice")
    private String brandVoice;

    @Column(name = "brand_colors")
    private String brandColors;

    @Column(name = "target_market")
    private String targetMarket;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    private Set<String> competitorUrls;

    @Column(name = "social_media_handles")
    private String socialMediaHandles;


    @OneToMany(mappedBy = "customerModel", fetch = FetchType.LAZY)
    private List<Opportunity> opportunities;

    @OneToMany(mappedBy = "customerModel", fetch = FetchType.LAZY)
    private List<Interaction> interactions;

    public CustomerModel(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isBlocked(){
        return false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

