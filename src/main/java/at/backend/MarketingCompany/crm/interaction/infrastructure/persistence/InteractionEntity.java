package at.backend.MarketingCompany.crm.interaction.infrastructure.persistence;

import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.FeedbackType;
import at.backend.MarketingCompany.crm.interaction.domain.entity.valueobject.InteractionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interactions")
public class InteractionEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerCompanyEntity customerCompany;

  @Column(name = "customer_id", insertable = false, updatable = false)
  private String customerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 50)
  private InteractionType type;

  @Column(name = "date_time", nullable = false)
  private LocalDateTime dateTime;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "outcome", nullable = false, length = 500)
  private String outcome;

  @Enumerated(EnumType.STRING)
  @Column(name = "feedback_type", length = 20)
  private FeedbackType feedbackType;

  @Column(name = "channel_preference", length = 50)
  private String channelPreference;
}
