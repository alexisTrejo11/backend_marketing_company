package at.backend.MarketingCompany.crm.interaction.infrastructure.persistence;

import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import at.backend.MarketingCompany.crm.shared.enums.FeedbackType;
import at.backend.MarketingCompany.crm.shared.enums.InteractionType;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
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
    private CustomerModel customerModel;

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