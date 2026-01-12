package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.asset.core.application.command.CreateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import jakarta.validation.constraints.*;

public record CreateAssetInput(
    @NotNull(message = "Campaign ID is required")
    @Positive(message = "Campaign ID must be positive")
    Long campaignId,

    @NotNull(message = "Asset type is required")
    AssetType assetType,

    @NotBlank(message = "Asset name is required")
    @Size(max = 200, message = "Asset name cannot exceed 200 characters")
    String name,

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    String description,

    @NotBlank(message = "URL is required")
    @Size(max = 500, message = "URL cannot exceed 500 characters")
    String url,

    @Size(max = 20, message = "Version cannot exceed 20 characters")
    String version,

    @Positive(message = "File size must be positive")
    Integer fileSizeKb,

    @Size(max = 100, message = "MIME type cannot exceed 100 characters")
    String mimeType
) {

    public CreateAssetCommand toCommand() {
        return new CreateAssetCommand(
            campaignId !=  null ? new MarketingCampaignId(campaignId) : null,
            assetType,
            name,
            description,
            url,
            version,
            fileSizeKb,
            mimeType
        );
    }
}
