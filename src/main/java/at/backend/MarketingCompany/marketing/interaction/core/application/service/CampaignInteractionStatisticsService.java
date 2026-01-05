package at.backend.MarketingCompany.marketing.interaction.core.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.DeviceBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.GeographicBreakdown;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.InteractionStatistics;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionStatisticsInputPort;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignInteractionStatisticsService implements InteractionStatisticsInputPort {

  private final InteractionRepositoryPort interactionRepository;
  private final CampaignRepositoryPort campaignRepository;

  @Override
  @Transactional(readOnly = true)
  public InteractionStatistics getInteractionStatistics(MarketingCampaignId campaignId) {
    log.debug("Getting interaction statistics for campaign: {}", campaignId.getValue());

    Long totalInteractions = interactionRepository.countByCampaignId(campaignId);
    Long totalConversions = interactionRepository.countConversionsByCampaignId(campaignId);
    BigDecimal totalConversionValue = interactionRepository.calculateTotalConversionValueByCampaignId(campaignId);
    Long uniqueCustomers = interactionRepository.countUniqueCustomersByCampaignId(campaignId);
    Long uniqueChannels = interactionRepository.countUniqueChannelsByCampaignId(campaignId);

    Double conversionRate = calculateConversionRate(totalInteractions, totalConversions);
    BigDecimal averageConversionValue = calculateAverageConversionValue(
        totalConversionValue,
        totalConversions);

    // Get breakdowns
    InteractionStatistics.InteractionTypeBreakdown typeBreakdown = buildTypeBreakdown(campaignId);
    DeviceBreakdown deviceBreakdown = buildDeviceBreakdown(campaignId);
    GeographicBreakdown geographicBreakdown = buildGeographicBreakdown(campaignId);

    MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);

    return InteractionStatistics.builder()
        .campaignId(campaignId.getValue())
        .campaignName(campaign != null ? campaign.getName().value() : null)
        .totalInteractions(totalInteractions)
        .totalConversions(totalConversions)
        .conversionRate(conversionRate)
        .totalConversionValue(totalConversionValue)
        .averageConversionValue(averageConversionValue)
        .uniqueCustomers(uniqueCustomers)
        .uniqueChannels(uniqueChannels)
        .typeBreakdown(typeBreakdown)
        .deviceBreakdown(deviceBreakdown)
        .geographicBreakdown(geographicBreakdown)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public Double getConversionRate(MarketingCampaignId campaignId) {
    Long totalInteractions = interactionRepository.countByCampaignId(campaignId);
    Long totalConversions = interactionRepository.countConversionsByCampaignId(campaignId);

    return calculateConversionRate(totalInteractions, totalConversions);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAverageConversionValue(MarketingCampaignId campaignId) {
    BigDecimal totalValue = interactionRepository
        .calculateTotalConversionValueByCampaignId(campaignId);
    Long totalConversions = interactionRepository.countConversionsByCampaignId(campaignId);

    return calculateAverageConversionValue(totalValue, totalConversions);
  }

  private Double calculateConversionRate(Long totalInteractions, Long totalConversions) {
    if (totalInteractions == null || totalInteractions == 0) {
      return 0.0;
    }
    return BigDecimal.valueOf(totalConversions)
        .divide(BigDecimal.valueOf(totalInteractions), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100))
        .doubleValue();
  }

  private BigDecimal calculateAverageConversionValue(
      BigDecimal totalValue,
      Long totalConversions) {

    if (totalValue == null || totalConversions == null || totalConversions == 0) {
      return BigDecimal.ZERO;
    }
    return totalValue.divide(
        BigDecimal.valueOf(totalConversions),
        2,
        RoundingMode.HALF_UP);
  }

  private InteractionStatistics.InteractionTypeBreakdown buildTypeBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> typeCounts = interactionRepository
        .countByInteractionTypeByCampaignId(campaignId);

    return InteractionStatistics.InteractionTypeBreakdown.builder()
        .adClicks(typeCounts.getOrDefault("AD_CLICK", 0L))
        .adViews(typeCounts.getOrDefault("AD_VIEW", 0L))
        .emailOpens(typeCounts.getOrDefault("EMAIL_OPEN", 0L))
        .emailClicks(typeCounts.getOrDefault("EMAIL_CLICK", 0L))
        .landingPageVisits(typeCounts.getOrDefault("LANDING_PAGE_VISIT", 0L))
        .formSubmits(typeCounts.getOrDefault("FORM_SUBMIT", 0L))
        .socialEngagements(typeCounts.getOrDefault("SOCIAL_ENGAGEMENT", 0L))
        .webinarRegistrations(typeCounts.getOrDefault("WEBINAR_REGISTRATION", 0L))
        .whitepaperDownloads(typeCounts.getOrDefault("WHITEPAPER_DOWNLOAD", 0L))
        .build();
  }

  private DeviceBreakdown buildDeviceBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> deviceCounts = interactionRepository
        .countByDeviceTypeByCampaignId(campaignId);

    return DeviceBreakdown.builder()
        .desktop(deviceCounts.getOrDefault("desktop", 0L))
        .mobile(deviceCounts.getOrDefault("mobile", 0L))
        .tablet(deviceCounts.getOrDefault("tablet", 0L))
        .unknown(deviceCounts.getOrDefault("unknown", 0L))
        .build();
  }

  private GeographicBreakdown buildGeographicBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> topCountries = interactionRepository
        .findTopCountriesByCampaignId(campaignId, 10);
    Map<String, Long> topCities = interactionRepository
        .findTopCitiesByCampaignId(campaignId, 10);

    return new GeographicBreakdown(topCountries, topCities);
  }
}
