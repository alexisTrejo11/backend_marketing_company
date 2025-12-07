package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.interaction.infrastructure.persistence.InteractionEntity;
import at.backend.MarketingCompany.crm.opportunity.infrastructure.persistence.OpportunityEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity extends BaseJpaEntity {
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

  @ElementCollection
  private Set<String> competitorUrls;

  @Column(name = "social_media_handles")
  private String socialMediaHandles;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
  private Set<OpportunityEntity> opportunities;

  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
  private Set<InteractionEntity> interactions;

  public CustomerEntity(String id) {
    this.id = id;
  }

}
