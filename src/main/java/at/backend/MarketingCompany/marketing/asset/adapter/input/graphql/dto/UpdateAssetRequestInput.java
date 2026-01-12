package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import jakarta.validation.constraints.*;

public record UpdateAssetRequestInput(
		@NotNull @Positive Long id,

		@Size(max = 200, message = "Asset name cannot exceed 200 characters")
		String name,

		@Size(max = 1000, message = "Description cannot exceed 1000 characters")
		String description,

		@Size(max = 500, message = "URL cannot exceed 500 characters")
		String url,

		Boolean isPrimaryAsset
) {

	public UpdateAssetCommand toCommand() {
		return UpdateAssetCommand.builder()
				.assetId(this.id != null ? new MarketingAssetId(this.id) : null)
				.name(this.name)
				.description(this.description)
				.url(this.url)
				.build();
	}
}
