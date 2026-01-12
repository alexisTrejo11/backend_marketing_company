package at.backend.MarketingCompany.marketing.asset.core.application.service;

import at.backend.MarketingCompany.marketing.asset.core.application.command.CreateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetPerformanceCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.dto.AssetStatistics;
import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.asset.core.port.input.AssetServicePort;
import at.backend.MarketingCompany.marketing.asset.core.port.output.AssetRepositoryPort;
import at.backend.MarketingCompany.marketing.asset.core.port.output.AssetStatisticsRepositoryPort;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetService implements AssetServicePort {
    private final AssetRepositoryPort assetRepository;
    private final AssetStatisticsRepositoryPort statisticsRepository;
    private final CampaignRepositoryPort campaignRepository;

    @Override
    @Transactional
    public MarketingAsset createAsset(CreateAssetCommand command) {
        log.debug("Creating asset for campaign: {}", command.campaignId().getValue());
        
        // Validate campaign exists
        MarketingCampaign campaign = campaignRepository.findById(command.campaignId())
            .orElseThrow(() -> new BusinessRuleException("Campaign not found"));
        
        // Create asset
        MarketingAsset asset = MarketingAsset.create(
            command.campaignId(),
            command.assetType(),
            command.name(),
            command.description(),
            command.url()
        );

        if (command.fileSizeKb() != null) {
            asset.setFileSizeKb(command.fileSizeKb());
        }
        if (command.mimeType() != null) {
            asset.setMimeType(command.mimeType());
        }
        
        MarketingAsset savedAsset = assetRepository.save(asset);
        log.info("Asset created successfully: {}", savedAsset.getId().getValue());
        
        return savedAsset;
    }

    @Override
    @Transactional
    public MarketingAsset updateAsset(UpdateAssetCommand command) {
        log.debug("Updating asset: {}", command.assetId().getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(command.assetId());
        
        // Update fields
        if (command.assetType() != null) {
            asset.setAssetType(command.assetType());
        }
        if (command.name() != null) {
            asset.setName(command.name());
        }
        if (command.description() != null) {
            asset.setDescription(command.description());
        }
        if (command.url() != null) {
            asset.setUrl(command.url());
        }

        if (command.fileSizeKb() != null) {
            asset.setFileSizeKb(command.fileSizeKb());
        }
        if (command.mimeType() != null) {
            asset.setMimeType(command.mimeType());
        }
        
        MarketingAsset updatedAsset = assetRepository.save(asset);
        log.info("Asset updated successfully: {}", command.assetId().getValue());
        
        return updatedAsset;
    }

    @Override
    @Transactional
    public MarketingAsset updateAssetPerformance(UpdateAssetPerformanceCommand command) {
        log.debug("Updating asset performance: {}", command.assetId().getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(command.assetId());
        
        if (command.viewsCount() != null) {
            asset.setViewsCount(command.viewsCount());
        }
        if (command.clicksCount() != null) {
            asset.setClicksCount(command.clicksCount());
        }
        if (command.conversionsCount() != null) {
            asset.setConversionsCount(command.conversionsCount());
        }
        
        // Recalculate conversion rate
        asset.updateConversionRate();
        
        MarketingAsset updatedAsset = assetRepository.save(asset);
        log.info("Asset performance updated: {}", command.assetId().getValue());
        
        return updatedAsset;
    }

    @Override
    @Transactional
    public MarketingAsset markAsPrimary(MarketingAssetId assetId) {
        log.debug("Marking asset as primary: {}", assetId.getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(assetId);
        asset.markAsPrimary();
        
        MarketingAsset updatedAsset = assetRepository.save(asset);
        log.info("Asset marked as primary: {}", assetId.getValue());
        
        return updatedAsset;
    }

    @Override
    @Transactional
    public MarketingAsset activateAsset(MarketingAssetId assetId) {
        log.debug("Activating asset: {}", assetId.getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(assetId);
        asset.activate();
        
        MarketingAsset updatedAsset = assetRepository.save(asset);
        log.info("Asset activated: {}", assetId.getValue());
        
        return updatedAsset;
    }

    @Override
    @Transactional
    public MarketingAsset archiveAsset(MarketingAssetId assetId) {
        log.debug("Archiving asset: {}", assetId.getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(assetId);
        asset.archive();
        
        MarketingAsset updatedAsset = assetRepository.save(asset);
        log.info("Asset archived: {}", assetId.getValue());
        
        return updatedAsset;
    }

    @Override
    @Transactional
    public void deleteAsset(MarketingAssetId assetId) {
        log.debug("Deleting asset: {}", assetId.getValue());
        
        MarketingAsset asset = findAssetByIdOrThrow(assetId);
        
        if (asset.isPrimaryAsset()) {
            throw new BusinessRuleException(
                "Cannot delete primary asset. Please unmark as primary first."
            );
        }
        
        asset.softDelete();
        assetRepository.save(asset);
        
        log.info("Asset deleted successfully: {}", assetId.getValue());
    }

    // ========== QUERY OPERATIONS ==========

    @Override
    @Transactional(readOnly = true)
    public MarketingAsset getAssetById(MarketingAssetId assetId) {
        return findAssetByIdOrThrow(assetId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingAsset> searchAssets(AssetQuery query, Pageable pageable) {
        if (query.isEmpty()) {
            return assetRepository.findAll(pageable);
        }
        
        return assetRepository.findByFilters(query, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingAsset> getAssetsByCampaign(
            MarketingCampaignId campaignId, 
            Pageable pageable) {
        return assetRepository.findByCampaignId(campaignId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingAsset> getAssetsByCampaignAndType(
            MarketingCampaignId campaignId,
            AssetType assetType,
            Pageable pageable) {
        return assetRepository.findByCampaignIdAndAssetType(
            campaignId, 
            assetType, 
            pageable
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingAsset> getAssetsByStatus(AssetStatus status, Pageable pageable) {
        return assetRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingAsset> getPrimaryAssetsByCampaign(
            MarketingCampaignId campaignId,
            Pageable pageable) {
        return assetRepository.findPrimaryAssetsByCampaignId(campaignId, pageable);
    }

    // ========== ANALYTICS OPERATIONS ==========

    @Override
    @Transactional(readOnly = true)
    public AssetStatistics getAssetStatistics(MarketingCampaignId campaignId) {
        log.debug("Getting asset statistics for campaign: {}", campaignId.getValue());
        
        Long totalAssets = statisticsRepository.countByCampaignId(campaignId);
        Long activeAssets = statisticsRepository.countByCampaignIdAndStatus(
            campaignId, 
            AssetStatus.ACTIVE
        );
        Long archivedAssets = statisticsRepository.countByCampaignIdAndStatus(
            campaignId, 
            AssetStatus.ARCHIVED
        );
        Long primaryAssets = statisticsRepository.countPrimaryAssetsByCampaignId(campaignId);
        
        Long totalViews = statisticsRepository.sumViewsByCampaignId(campaignId);
        Long totalClicks = statisticsRepository.sumClicksByCampaignId(campaignId);
        Long totalConversions = statisticsRepository.sumConversionsByCampaignId(campaignId);
        
        Double averageConversionRate = calculateAverageConversionRate(
            totalConversions, 
            totalClicks
        );
        Double clickThroughRate = calculateClickThroughRate(totalClicks, totalViews);
        
        AssetStatistics.AssetTypeBreakdown typeBreakdown = 
            buildTypeBreakdown(campaignId);
        AssetStatistics.TopPerformingAssets topPerformers = 
            buildTopPerformingAssets(campaignId);
        
        MarketingCampaign campaign = campaignRepository.findById(campaignId).orElse(null);
        
        return AssetStatistics.builder()
            .campaignId(campaignId.getValue())
            .campaignName(campaign != null ? campaign.getName().value() : null)
            .totalAssets(totalAssets)
            .activeAssets(activeAssets)
            .archivedAssets(archivedAssets)
            .primaryAssets(primaryAssets)
            .totalViews(totalViews)
            .totalClicks(totalClicks)
            .totalConversions(totalConversions)
            .averageConversionRate(averageConversionRate)
            .overallClickThroughRate(clickThroughRate)
            .typeBreakdown(typeBreakdown)
            .topPerformingAssets(topPerformers)
            .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalAssetsByCampaign(MarketingCampaignId campaignId) {
        return statisticsRepository.countByCampaignId(campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalClicksByCampaign(MarketingCampaignId campaignId) {
        return statisticsRepository.sumClicksByCampaignId(campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageConversionRate(MarketingCampaignId campaignId) {
        Long totalConversions = statisticsRepository.sumConversionsByCampaignId(campaignId);
        Long totalClicks = statisticsRepository.sumClicksByCampaignId(campaignId);
        
        return calculateAverageConversionRate(totalConversions, totalClicks);
    }


    private MarketingAsset findAssetByIdOrThrow(MarketingAssetId assetId) {
        return assetRepository.findById(assetId)
            .orElseThrow(() -> new BusinessRuleException(
                "Marketing asset not found with id: " + assetId.getValue()
            ));
    }

    private Double calculateAverageConversionRate(Long conversions, Long clicks) {
        if (clicks == null || clicks == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf(conversions)
            .divide(BigDecimal.valueOf(clicks), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }

    private Double calculateClickThroughRate(Long clicks, Long views) {
        if (views == null || views == 0) {
            return 0.0;
        }
        return BigDecimal.valueOf(clicks)
            .divide(BigDecimal.valueOf(views), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .doubleValue();
    }

    private AssetStatistics.AssetTypeBreakdown buildTypeBreakdown(
            MarketingCampaignId campaignId) {
        Map<String, Long> typeCounts = statisticsRepository.countByAssetTypeByCampaignId(campaignId);
        
        return AssetStatistics.AssetTypeBreakdown.builder()
            .images(typeCounts.getOrDefault("IMAGE", 0L))
            .videos(typeCounts.getOrDefault("VIDEO", 0L))
            .pdfs(typeCounts.getOrDefault("PDF", 0L))
            .infographics(typeCounts.getOrDefault("INFOGRAPHIC", 0L))
            .presentations(typeCounts.getOrDefault("PRESENTATION", 0L))
            .whitepapers(typeCounts.getOrDefault("WHITEPAPER", 0L))
            .caseStudies(typeCounts.getOrDefault("CASE_STUDY", 0L))
            .webinars(typeCounts.getOrDefault("WEBINAR", 0L))
            .ebooks(typeCounts.getOrDefault("EBOOK", 0L))
            .others(typeCounts.getOrDefault("OTHER", 0L))
            .build();
    }

    private AssetStatistics.TopPerformingAssets buildTopPerformingAssets(
            MarketingCampaignId campaignId) {
        
        List<MarketingAsset> topByViews = assetRepository
            .findTopAssetsByViews(campaignId, 5);
        List<MarketingAsset> topByClicks = assetRepository
            .findTopAssetsByClicks(campaignId, 5);
        List<MarketingAsset> topByConversions = assetRepository
            .findTopAssetsByConversions(campaignId, 5);
        
        Map<String, AssetStatistics.PerformanceMetrics> byViews = new HashMap<>();
        Map<String, AssetStatistics.PerformanceMetrics> byClicks = new HashMap<>();
        Map<String, AssetStatistics.PerformanceMetrics> byConversions = new HashMap<>();
        
        topByViews.forEach(asset -> byViews.put(
            asset.getName(), 
            buildPerformanceMetrics(asset)
        ));
        
        topByClicks.forEach(asset -> byClicks.put(
            asset.getName(), 
            buildPerformanceMetrics(asset)
        ));
        
        topByConversions.forEach(asset -> byConversions.put(
            asset.getName(), 
            buildPerformanceMetrics(asset)
        ));
        
        return AssetStatistics.TopPerformingAssets.builder()
            .byViews(byViews)
            .byClicks(byClicks)
            .byConversions(byConversions)
            .build();
    }

    private AssetStatistics.PerformanceMetrics buildPerformanceMetrics(
            MarketingAsset asset) {
        return AssetStatistics.PerformanceMetrics.builder()
            .assetId(asset.getId().getValue())
            .assetName(asset.getName())
            .views(asset.getViewsCount())
            .clicks(asset.getClicksCount())
            .conversions(asset.getConversionsCount())
            .conversionRate(asset.getConversionRate())
            .build();
    }
}