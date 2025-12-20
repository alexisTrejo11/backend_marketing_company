package at.backend.MarketingCompany.marketing.interaction.adapter.output.persitence.model;

import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.MarketingCampaignEntity;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "campaign_interactions", indexes = {
    @Index(name = "idx_interactions_campaign_customer", columnList = "campaign_id, customer_id, interaction_date"),
    @Index(name = "idx_interactions_conversion", columnList = "campaign_id, is_conversion, interaction_date"),
    @Index(name = "idx_interactions_utm", columnList = "utm_source, utm_medium, utm_campaign"),
    @Index(name = "idx_interactions_date_type", columnList = "interaction_date, interaction_type")
})
public class CampaignInteractionEntity extends BaseJpaEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "campaign_id", nullable = false)
  private MarketingCampaignEntity campaign;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private CustomerCompanyEntity customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "interaction_type", nullable = false, length = 50)
  private InteractionType interactionType;

  @Column(name = "interaction_date", nullable = false)
  private LocalDateTime interactionDate = LocalDateTime.now();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private MarketingChannelEntity channel;

  @Column(name = "utm_source", length = 100)
  private String utmSource;

  @Column(name = "utm_medium", length = 100)
  private String utmMedium;

  @Column(name = "utm_campaign", length = 100)
  private String utmCampaign;

  @Column(name = "utm_content", length = 100)
  private String utmContent;

  @Column(name = "utm_term", length = 100)
  private String utmTerm;

  @Column(name = "device_type", length = 50)
  private String deviceType;

  @Column(name = "device_os", length = 50)
  private String deviceOs;

  @Column(name = "browser", length = 100)
  private String browser;

  @Column(name = "country_code", length = 2)
  private String countryCode;

  @Column(name = "city", length = 100)
  private String city;

  @Column(name = "deal_id")
  private Long dealId;

  @Column(name = "conversion_value", precision = 10, scale = 2)
  private BigDecimal conversionValue;

  @Column(name = "is_conversion")
  private Boolean isConversion = false;

  @Column(name = "landing_page_url", length = 500)
  private String landingPageUrl;

  @Column(name = "referrer_url", length = 500)
  private String referrerUrl;

  @Column(name = "session_id", length = 100)
  private String sessionId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "properties", columnDefinition = "jsonb")
  private Map<String, Object> properties;

  public enum InteractionType {
    AD_CLICK, AD_VIEW, EMAIL_OPEN, EMAIL_CLICK,
    LANDING_PAGE_VISIT, FORM_SUBMIT, SOCIAL_ENGAGEMENT,
    WEBINAR_REGISTRATION, WHITEPAPER_DOWNLOAD
  }
}