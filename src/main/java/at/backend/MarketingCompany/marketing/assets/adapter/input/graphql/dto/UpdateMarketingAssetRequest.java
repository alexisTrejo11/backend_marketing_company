package at.backend.MarketingCompany.marketing.assets.adapter.input.graphql.dto;

import jakarta.validation.constraints.Size;

public record UpdateMarketingAssetRequest(
    @Size(max = 200, message = "Asset name cannot exceed 200 characters")
    String name,

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,

    @Size(max = 500, message = "URL cannot exceed 500 characters")
    String url,

    Boolean isPrimaryAsset
) {}
