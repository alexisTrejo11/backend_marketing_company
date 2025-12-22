package at.backend.MarketingCompany.marketing.assets.adapter.input.graphql.dto;

import java.time.LocalDateTime;

public record MarketingAssetResponse(
    Long id,
    Long campaignId,
    String assetType,
    String name,
    String description,
    String url,
    String version,
    Integer fileSizeKb,
    String mimeType,
    Integer viewsCount,
    Integer clicksCount,
    Integer conversionsCount,
    Double conversionRate,
    String status,
    Boolean isPrimaryAsset,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}