package at.backend.MarketingCompany.marketing.interaction.core.application.service;

import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.core.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.command.*;
import at.backend.MarketingCompany.marketing.interaction.core.application.dto.*;
import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.InteractionValidator;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.CampaignInteractionServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignInteractionService implements CampaignInteractionServicePort {
  private final InteractionRepositoryPort interactionRepository;
  private final CampaignRepositoryPort campaignRepository;
  private final CustomerCompanyRepositoryPort customerRepository;


  @Override
  @Transactional
  public CampaignInteraction trackInteraction(TrackInteractionCommand command) {
    log.debug("Tracking interaction for campaign: {}, customer: {}", 
        command.campaignId().getValue(), command.customerId().getValue());
    
    MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
        .orElseThrow(() -> new BusinessRuleException("Campaign not found"));
    InteractionValidator.validateCampaignIsActive(campaign.isActive());
    
    CustomerCompany customer = customerRepository.findById(command.customerId())
        .orElseThrow(() -> new BusinessRuleException("Customer not found"));
    InteractionValidator.validateCustomerIsNotBlocked(customer.isBlocked());
    
    InteractionValidator.validateForTracking(LocalDateTime.now(), campaign, customer);

    CampaignInteraction interaction = CampaignInteraction.create(
        command.campaignId(),
        command.customerId(),
        command.interactionType(),
        command.sessionId()
    );
    
    // Set optional fields
    if (command.channelId() != null) {
      interaction.setChannelId(command.channelId());
    }
    if (command.utmSource() != null) {
      interaction.setUtmSource(command.utmSource());
    }
    if (command.utmMedium() != null) {
      interaction.setUtmMedium(command.utmMedium());
    }
    if (command.utmCampaign() != null) {
      interaction.setUtmCampaign(command.utmCampaign());
    }
    if (command.utmContent() != null) {
      interaction.setUtmContent(command.utmContent());
    }
    if (command.utmTerm() != null) {
      interaction.setUtmTerm(command.utmTerm());
    }
    if (command.deviceType() != null) {
      interaction.setDeviceType(command.deviceType());
    }
    if (command.deviceOs() != null) {
      interaction.setDeviceOs(command.deviceOs());
    }
    if (command.browser() != null) {
      interaction.setBrowser(command.browser());
    }
    if (command.countryCode() != null) {
      interaction.setCountryCode(command.countryCode());
    }
    if (command.city() != null) {
      interaction.setCity(command.city());
    }
    if (command.landingPageUrl() != null) {
      interaction.setLandingPageUrl(command.landingPageUrl());
    }
    if (command.referrerUrl() != null) {
      interaction.setReferrerUrl(command.referrerUrl());
    }
    if (command.properties() != null) {
      interaction.setProperties(command.properties());
    }
    
    CampaignInteraction savedInteraction = interactionRepository.save(interaction);
    log.info("Interaction tracked successfully: {}", savedInteraction.getId().getValue());
    
    return savedInteraction;
  }

  @Override
  @Transactional
  public CampaignInteraction markAsConversion(MarkAsConversionCommand command) {
    log.debug("Marking interaction as conversion: {}", command.interactionId().getValue());
    
    CampaignInteraction interaction = findInteractionByIdOrThrow(command.interactionId());
    
    // Validate conversion data
    InteractionValidator.validateConversionData(
        interaction,
        command.dealId(),
        command.conversionValue()
    );
    
    // Mark as conversion using domain method
    interaction.markAsConversion(command.dealId(), command.conversionValue());
    
    CampaignInteraction updatedInteraction = interactionRepository.save(interaction);
    log.info("Interaction marked as conversion: {}", command.interactionId().getValue());
    
    return updatedInteraction;
  }

  @Override
  @Transactional
  public void deleteInteraction(CampaignInteractionId interactionId) {
    log.debug("Deleting interaction: {}", interactionId.getValue());
    
    CampaignInteraction interaction = findInteractionByIdOrThrow(interactionId);
    
    if (interaction.isConversion()) {
      throw new BusinessRuleException(
          "Cannot delete interaction marked as conversion. " +
          "Please remove conversion status first."
      );
    }
    
    interaction.softDelete();
    interactionRepository.save(interaction);
    
    log.info("Interaction deleted successfully: {}", interactionId.getValue());
  }

  // ========== QUERY OPERATIONS ==========

  @Override
  @Transactional(readOnly = true)
  public CampaignInteraction getInteractionById(CampaignInteractionId interactionId) {
    return findInteractionByIdOrThrow(interactionId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> searchInteractions(
      InteractionQuery query, 
      Pageable pageable) {
    
    if (query.isEmpty()) {
      return interactionRepository.findAll(pageable);
    }
    
    return interactionRepository.findByFilters(
        pageable
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByCampaign(
      MarketingCampaignId campaignId,
      Pageable pageable) {
    
    return interactionRepository.findByCampaignId(campaignId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByCustomer(CustomerCompanyId customerId, Pageable pageable) {
    return interactionRepository.findByCustomerId(customerId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getConversionsByCampaign(
      MarketingCampaignId campaignId,
      Pageable pageable) {
    
    return interactionRepository.findByCampaignIdAndIsConversion(
        campaignId, 
        true, 
        pageable
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByChannel(MarketingChannelId channelId, Pageable pageable) {
    return interactionRepository.findByChannelId(channelId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByDateRange(
      LocalDateTime startDate, 
      LocalDateTime endDate, 
      Pageable pageable) {
    
    return interactionRepository.findByDateRange(startDate, endDate, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByUtm(
      String utmSource,
      String utmMedium,
      String utmCampaign,
      Pageable pageable) {
    
    return interactionRepository.findByUtmParameters(
        utmSource, 
        utmMedium, 
        utmCampaign, 
        pageable
    );
  }

  // ========== ANALYTICS OPERATIONS ==========

  @Override
  @Transactional(readOnly = true)
  public InteractionStatistics getInteractionStatistics(MarketingCampaignId campaignId) {
    log.debug("Getting interaction statistics for campaign: {}", campaignId.getValue());
    
    Long totalInteractions = interactionRepository.countByCampaignId(campaignId);
    Long totalConversions = interactionRepository.countConversionsByCampaignId(campaignId);
    BigDecimal totalConversionValue = interactionRepository
        .calculateTotalConversionValueByCampaignId(campaignId);
    Long uniqueCustomers = interactionRepository.countUniqueCustomersByCampaignId(campaignId);
    Long uniqueChannels = interactionRepository.countUniqueChannelsByCampaignId(campaignId);
    
    Double conversionRate = calculateConversionRate(totalInteractions, totalConversions);
    BigDecimal averageConversionValue = calculateAverageConversionValue(
        totalConversionValue, 
        totalConversions
    );
    
    // Get breakdowns
    InteractionStatistics.InteractionTypeBreakdown typeBreakdown =
        buildTypeBreakdown(campaignId);
    DeviceBreakdown deviceBreakdown =
        buildDeviceBreakdown(campaignId);
    GeographicBreakdown geographicBreakdown =
        buildGeographicBreakdown(campaignId);
    
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

  // ========== PRIVATE HELPER METHODS ==========

  private CampaignInteraction findInteractionByIdOrThrow(CampaignInteractionId interactionId) {
    return interactionRepository.findById(interactionId)
        .orElseThrow(() -> new MarketingDomainException(
            "Interaction not found with id: " + interactionId.getValue()
        ));
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
        RoundingMode.HALF_UP
    );
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