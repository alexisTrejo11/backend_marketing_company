package at.backend.MarketingCompany.marketing.attribution.core.application;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.marketing.attribution.core.application.command.*;
import at.backend.MarketingCompany.marketing.attribution.core.application.dto.AttributionStatistics;
import at.backend.MarketingCompany.marketing.attribution.core.application.queries.AttributionQuery;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.AttributionValidator;
import at.backend.MarketingCompany.marketing.attribution.core.domain.entity.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.core.domain.valueobject.CampaignAttributionId;
import at.backend.MarketingCompany.marketing.attribution.core.port.input.CampaignAttributionServicePort;
import at.backend.MarketingCompany.marketing.attribution.core.port.output.AttributionRepositoryPort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.output.CampaignRepositoryPort;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignAttributionService implements CampaignAttributionServicePort {

  private final AttributionRepositoryPort attributionRepository;
  private final CampaignRepositoryPort campaignRepository;

  // ========== COMMAND OPERATIONS ==========

  @Override
  @Transactional
  public CampaignAttribution createAttribution(CreateAttributionCommand command) {
    log.debug("Creating attribution for deal: {}, campaign: {}", 
        command.dealId(), command.campaignId().getValue());
    
    // Validate campaign exists
    MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
        .orElseThrow(() -> new BusinessRuleException("Campaign not found"));
    
    // Validate attribution data
    AttributionValidator.validateForCreation(
        command.dealId(),
        campaign,
        command.attributionPercentage(),
        command.attributedRevenue()
    );
    
    // Check for duplicate attribution
    if (attributionRepository.existsByDealIdAndCampaignId(
        command.dealId(), 
        command.campaignId())) {
      throw new BusinessRuleException(
          "Attribution already exists for this deal and campaign"
      );
    }
    
    // Validate weights for custom model
    if (command.attributionModel() == AttributionModel.CUSTOM) {
      AttributionValidator.validateWeights(
          command.firstTouchWeight(),
          command.lastTouchWeight(),
          command.linearWeight()
      );
    }
    
    // Create attribution using domain factory
    CampaignAttribution attribution = CampaignAttribution.create(
        command.dealId(),
        command.campaignId(),
        command.attributionModel(),
        command.attributionPercentage(),
        command.attributedRevenue()
    );
    
    // Add touchpoints if provided
    if (command.touchTimestamps() != null && !command.touchTimestamps().isEmpty()) {
      command.touchTimestamps().forEach(attribution::addTouchpoint);
    }
    
    // Set weights for custom model
    if (command.attributionModel() == AttributionModel.CUSTOM) {
      attribution.setFirstTouchWeight(command.firstTouchWeight());
      attribution.setLastTouchWeight(command.lastTouchWeight());
      attribution.setLinearWeight(command.linearWeight());
    }
    
    CampaignAttribution savedAttribution = attributionRepository.save(attribution);
    log.info("Attribution created successfully with ID: {}", savedAttribution.getId().getValue());
    
    // Update campaign attributed revenue
    updateCampaignAttributedRevenue(command.campaignId());
    
    return savedAttribution;
  }

  @Override
  @Transactional
  public CampaignAttribution updateAttribution(UpdateAttributionCommand command) {
    log.debug("Updating attribution: {}", command.attributionId().getValue());
    
    CampaignAttribution attribution = findAttributionByIdOrThrow(command.attributionId());
    
    // Validate update
    AttributionValidator.validateForUpdate(attribution);
    
    if (command.attributionPercentage() != null) {
      AttributionValidator.validateAttributionPercentage(command.attributionPercentage());
      attribution.setAttributionPercentage(command.attributionPercentage());
    }
    
    if (command.attributedRevenue() != null) {
      AttributionValidator.validateAttributedRevenue(command.attributedRevenue());
      attribution.setAttributedRevenue(command.attributedRevenue());
    }
    
    CampaignAttribution updatedAttribution = attributionRepository.save(attribution);
    log.info("Attribution updated successfully: {}", command.attributionId().getValue());
    
    // Update campaign attributed revenue
    updateCampaignAttributedRevenue(attribution.getCampaignId());
    
    return updatedAttribution;
  }

  @Override
  @Transactional
  public CampaignAttribution addTouchpoint(AddTouchpointCommand command) {
    log.debug("Adding touchpoint to attribution: {}", command.attributionId().getValue());
    
    CampaignAttribution attribution = findAttributionByIdOrThrow(command.attributionId());
    
    // Validate touchpoint
    AttributionValidator.validateTouchpoint(command.touchTimestamp());
    
    attribution.addTouchpoint(command.touchTimestamp());
    
    CampaignAttribution updatedAttribution = attributionRepository.save(attribution);
    log.info("Touchpoint added successfully to attribution: {}", command.attributionId().getValue());
    
    return updatedAttribution;
  }

  @Override
  @Transactional
  public CampaignAttribution recalculateAttribution(RecalculateAttributionCommand command) {
    log.debug("Recalculating attribution: {} with model: {}", 
        command.attributionId().getValue(), command.newModel());
    
    CampaignAttribution attribution = findAttributionByIdOrThrow(command.attributionId());
    
    // Recalculate based on new model
    attribution.setAttributionModel(command.newModel());
    
    // Here you would implement logic to recalculate attribution percentage
    // based on the new model and existing touchpoints
    
    CampaignAttribution recalculatedAttribution = attributionRepository.save(attribution);
    log.info("Attribution recalculated successfully: {}", command.attributionId().getValue());
    
    return recalculatedAttribution;
  }

  @Override
  @Transactional
  public void deleteAttribution(CampaignAttributionId attributionId) {
    log.debug("Deleting attribution: {}", attributionId.getValue());
    
    CampaignAttribution attribution = findAttributionByIdOrThrow(attributionId);
    
    attribution.softDelete();
    attributionRepository.save(attribution);
    
    log.info("Attribution deleted successfully: {}", attributionId.getValue());
    
    // Update campaign attributed revenue
    updateCampaignAttributedRevenue(attribution.getCampaignId());
  }

  // ========== QUERY OPERATIONS ==========

  @Override
  @Transactional(readOnly = true)
  public CampaignAttribution getAttributionById(CampaignAttributionId attributionId) {
    return findAttributionByIdOrThrow(attributionId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> searchAttributions(
      AttributionQuery query,
      Pageable pageable) {
    
    if (query.isEmpty()) {
      return attributionRepository.findAll(pageable);
    }
    
    return attributionRepository.findByFilters(
        query.dealId(),
        query.campaignId(),
        query.attributionModels(),
        query.minAttributionPercentage(),
        query.maxAttributionPercentage(),
        query.minAttributedRevenue(),
        query.maxAttributedRevenue(),
        pageable
    );
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> getAttributionsByDeal(DealId dealId, Pageable pageable) {
    return attributionRepository.findByDealId(dealId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> getAttributionsByCampaign(MarketingCampaignId campaignId, Pageable pageable) {
    return attributionRepository.findByCampaignId(campaignId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> getAttributionsByModel(
      AttributionModel model,
      Pageable pageable) {
    
    return attributionRepository.findByAttributionModel(model, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignAttribution> getTopAttributedCampaigns(Pageable pageable) {
    return attributionRepository.findTopAttributedCampaigns(pageable);
  }

  // ========== ANALYTICS OPERATIONS ==========

  @Override
  @Transactional(readOnly = true)
  public AttributionStatistics getAttributionStatistics(MarketingCampaignId campaignId) {
    log.debug("Getting attribution statistics for campaign: {}", campaignId);
    
    BigDecimal totalRevenue = attributionRepository
        .calculateTotalAttributedRevenueByCampaignId(campaignId);
    BigDecimal avgPercentage = attributionRepository
        .calculateAverageAttributionPercentageByCampaignId(campaignId);
    Long totalAttributions = attributionRepository.countByCampaignId(campaignId);
    Long uniqueDeals = attributionRepository.countUniqueDealsByCampaignId(campaignId);
    
    AttributionStatistics.AttributionModelBreakdown modelBreakdown = 
        buildModelBreakdown(campaignId);
    AttributionStatistics.TouchpointAnalysis touchpointAnalysis = 
        buildTouchpointAnalysis(campaignId);
    AttributionStatistics.RevenueDistribution revenueDistribution = 
        buildRevenueDistribution(campaignId);
    
    MarketingCampaign campaign = campaignRepository.findById(
        campaignId
    ).orElse(null);
    
    return AttributionStatistics.builder()
        .campaignId(campaignId.getValue())
        .campaignName(campaign != null ? campaign.getName().value() : null)
        .totalAttributedRevenue(totalRevenue)
        .averageAttributionPercentage(avgPercentage)
        .totalAttributions(totalAttributions)
        .uniqueDealsAttributed(uniqueDeals)
        .modelBreakdown(modelBreakdown)
        .touchpointAnalysis(touchpointAnalysis)
        .revenueDistribution(revenueDistribution)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getTotalAttributedRevenue(MarketingCampaignId campaignId) {
    return attributionRepository.calculateTotalAttributedRevenueByCampaignId(campaignId);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getAverageAttributionPercentage(MarketingCampaignId campaignId) {
    return attributionRepository.calculateAverageAttributionPercentageByCampaignId(campaignId);
  }

  // ========== PRIVATE HELPER METHODS ==========

  private CampaignAttribution findAttributionByIdOrThrow(CampaignAttributionId attributionId) {
    return attributionRepository.findById(attributionId)
        .orElseThrow(() -> new MarketingDomainException(
            "Attribution not found with id: " + attributionId.getValue()
        ));
  }

  private void updateCampaignAttributedRevenue(MarketingCampaignId campaignId) {
    BigDecimal totalRevenue = getTotalAttributedRevenue(campaignId);
    
    MarketingCampaign campaign = campaignRepository.findById(campaignId)
        .orElse(null);
    
    if (campaign != null) {
      campaign.updateAttributedRevenue(totalRevenue);
      campaignRepository.save(campaign);
    }
  }

  private AttributionStatistics.AttributionModelBreakdown buildModelBreakdown(MarketingCampaignId campaignId) {
    Map<String, Long> modelCounts = attributionRepository
        .countByAttributionModelByCampaignId(campaignId);
    
    return AttributionStatistics.AttributionModelBreakdown.builder()
        .firstTouch(modelCounts.getOrDefault("FIRST_TOUCH", 0L))
        .lastTouch(modelCounts.getOrDefault("LAST_TOUCH", 0L))
        .linear(modelCounts.getOrDefault("LINEAR", 0L))
        .timeDecay(modelCounts.getOrDefault("TIME_DECAY", 0L))
        .custom(modelCounts.getOrDefault("CUSTOM", 0L))
        .build();
  }

  private AttributionStatistics.TouchpointAnalysis buildTouchpointAnalysis(MarketingCampaignId campaignId) {
    Long totalTouchpoints = attributionRepository
        .calculateTotalTouchpointsByCampaignId(campaignId);
    Long uniqueDeals = attributionRepository.countUniqueDealsByCampaignId(campaignId);
    
    Double avgTouchpoints = uniqueDeals > 0 ? 
        totalTouchpoints.doubleValue() / uniqueDeals : 0.0;
    
    Map<Integer, Long> distribution = attributionRepository
        .getTouchpointDistributionByCampaignId(campaignId);
    
    Long minTouchpoints = distribution.keySet().stream()
        .min(Integer::compareTo).orElse(0).longValue();
    Long maxTouchpoints = distribution.keySet().stream()
        .max(Integer::compareTo).orElse(0).longValue();
    
    return AttributionStatistics.TouchpointAnalysis.builder()
        .totalTouchpoints(totalTouchpoints)
        .averageTouchpointsPerDeal(avgTouchpoints)
        .minTouchpoints(minTouchpoints)
        .maxTouchpoints(maxTouchpoints)
        .touchpointDistribution(distribution)
        .build();
  }

  private AttributionStatistics.RevenueDistribution buildRevenueDistribution(MarketingCampaignId campaignId) {
    List<BigDecimal> revenues = attributionRepository
        .getAllAttributedRevenuesByCampaignId(campaignId);
    
    BigDecimal min = revenues.stream()
        .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    BigDecimal max = revenues.stream()
        .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    
    BigDecimal median = calculateMedian(revenues);
    
    Map<String, BigDecimal> revenueByModel = attributionRepository
        .calculateRevenueByModelByCampaignId(campaignId);
    
    return AttributionStatistics.RevenueDistribution.builder()
        .minAttributedRevenue(min)
        .maxAttributedRevenue(max)
        .medianAttributedRevenue(median)
        .revenueByModel(revenueByModel)
        .build();
  }

  private BigDecimal calculateMedian(List<BigDecimal> values) {
    if (values == null || values.isEmpty()) {
      return BigDecimal.ZERO;
    }
    
    List<BigDecimal> sorted = new ArrayList<>(values);
    Collections.sort(sorted);
    
    int size = sorted.size();
    if (size % 2 == 0) {
      return sorted.get(size / 2 - 1)
          .add(sorted.get(size / 2))
          .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    } else {
      return sorted.get(size / 2);
    }
  }
}