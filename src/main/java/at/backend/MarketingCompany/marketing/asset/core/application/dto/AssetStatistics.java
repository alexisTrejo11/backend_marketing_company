package at.backend.MarketingCompany.marketing.asset.core.application.dto;

import lombok.Builder;

import java.util.Map;

/**
 * Statistics for marketing assets
 */
@Builder
public record AssetStatistics(
    Long campaignId,
    String campaignName,
    Long totalAssets,
    Long activeAssets,
    Long archivedAssets,
    Long primaryAssets,
    Long totalViews,
    Long totalClicks,
    Long totalConversions,
    Double averageConversionRate,
    Double overallClickThroughRate,
    AssetTypeBreakdown typeBreakdown,
    TopPerformingAssets topPerformingAssets
) {
    
    @Builder
    public record AssetTypeBreakdown(
        Long images,
        Long videos,
        Long pdfs,
        Long infographics,
        Long presentations,
        Long whitepapers,
        Long caseStudies,
        Long webinars,
        Long ebooks,
        Long others
    ) {}
    
    @Builder
    public record TopPerformingAssets(
        Map<String, PerformanceMetrics> byViews,
        Map<String, PerformanceMetrics> byClicks,
        Map<String, PerformanceMetrics> byConversions
    ) {}
    
    @Builder
    public record PerformanceMetrics(
        Long assetId,
        String assetName,
        int views,
        int clicks,
        int conversions,
        Double conversionRate
    ) {}
}