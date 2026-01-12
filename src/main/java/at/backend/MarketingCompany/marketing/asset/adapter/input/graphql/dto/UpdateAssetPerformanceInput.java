package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetPerformanceCommand;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Input DTO for updating asset performance metrics
 */
public record UpdateAssetPerformanceInput(
    @NotNull(message = "Asset ID is required")
    @Positive(message = "Asset ID must be positive")
    Long assetId,

    @PositiveOrZero(message = "Views count must be zero or positive")
    Integer viewsCount,

    @PositiveOrZero(message = "Clicks count must be zero or positive")
    Integer clicksCount,

    @PositiveOrZero(message = "Conversions count must be zero or positive")
    Integer conversionsCount
) {
    public UpdateAssetPerformanceCommand toCommand() {
        return new UpdateAssetPerformanceCommand(
            new MarketingAssetId(assetId),
            viewsCount,
            clicksCount,
            conversionsCount
        );
    }
}