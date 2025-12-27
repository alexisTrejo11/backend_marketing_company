package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AssetOutput(
    Long id,
    Long campaignId,
    String assetType,
    String name,
    String description,
    String url,
    Integer version,
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