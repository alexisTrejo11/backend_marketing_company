package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.mapper;

import at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto.AssetOutput;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AssetOutputMapper {

	public AssetOutput toOutput(MarketingAsset asset) {
		if (asset == null) return null;

		return AssetOutput.builder()
				.id(asset.getId().getValue())
				.campaignId(asset.getCampaignId().getValue())
				.assetType(asset.getAssetType().name())
				.name(asset.getName())
				.description(asset.getDescription())
				.url(asset.getUrl())
				.version(asset.getVersion())
				.fileSizeKb(asset.getFileSizeKb())
				.mimeType(asset.getMimeType())
				.viewsCount(asset.getViewsCount())
				.clicksCount(asset.getClicksCount())
				.conversionsCount(asset.getConversionsCount())
				.conversionRate(asset.getConversionRate())
				.status(asset.getStatus().name())
				.isPrimaryAsset(asset.isPrimaryAsset())
				.createdAt(asset.getCreatedAt())
				.updatedAt(asset.getUpdatedAt())
				.build();
	}


	public PageResponse<AssetOutput> toPageOutput(Page<MarketingAsset> assetPage) {
		if (assetPage == null) return PageResponse.empty();

		return PageResponse.of(assetPage.map(this::toOutput));
	}
}
