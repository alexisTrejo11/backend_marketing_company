package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.mapper.AssetOutputMapper;
import at.backend.MarketingCompany.marketing.asset.core.application.dto.AssetStatistics;
import at.backend.MarketingCompany.marketing.asset.core.application.query.AssetQuery;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetStatus;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.AssetType;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.asset.core.port.input.AssetServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AssetQueryController {
	private final AssetServicePort assetServicePort;
	private final AssetOutputMapper assetMapper;

	@QueryMapping
	public AssetOutput asset(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Query: asset with id: {}", id);

		var assetId = new MarketingAssetId(id);
		MarketingAsset asset = assetServicePort.getAssetById(assetId);

		return assetMapper.toOutput(asset);
	}

	@QueryMapping
	public PageResponse<AssetOutput> searchAssets(
			@Argument AssetFilterInput filter,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: searchAssets with filter");

		AssetQuery query = filter != null ? filter.toQuery() : new AssetQuery();
		Pageable pageable = pageInput.toPageable();

		Page<MarketingAsset> assetPage = assetServicePort.searchAssets(query, pageable);

		return assetMapper.toPageOutput(assetPage);
	}

	@QueryMapping
	public PageResponse<AssetOutput> assetsByCampaign(
			@Argument @NotNull @Positive Long campaignId,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: assetsByCampaign with campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		Pageable pageable = pageInput.toPageable();

		Page<MarketingAsset> assetPage = assetServicePort.getAssetsByCampaign(
				marketingCampaignId,
				pageable
		);

		return assetMapper.toPageOutput(assetPage);
	}

	@QueryMapping
	public PageResponse<AssetOutput> assetsByCampaignAndType(
			@Argument @NotNull @Positive Long campaignId,
			@Argument @NotNull AssetType assetType,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: assetsByCampaignAndType with campaignId: {}, type: {}",
				campaignId, assetType);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		Pageable pageable = pageInput.toPageable();

		Page<MarketingAsset> assetPage = assetServicePort.getAssetsByCampaignAndType(
				marketingCampaignId,
				assetType,
				pageable
		);

		return assetMapper.toPageOutput(assetPage);
	}

	@QueryMapping
	public PageResponse<AssetOutput> assetsByStatus(
			@Argument @NotNull AssetStatus status,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: assetsByStatus with status: {}", status);

		Pageable pageable = pageInput.toPageable();
		Page<MarketingAsset> assetPage = assetServicePort.getAssetsByStatus(status, pageable);

		return assetMapper.toPageOutput(assetPage);
	}

	@QueryMapping
	public PageResponse<AssetOutput> primaryAssetsByCampaign(
			@Argument @NotNull @Positive Long campaignId,
			@Argument @Valid PageInput pageInput) {
		log.debug("GraphQL Query: primaryAssetsByCampaign with campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		Pageable pageable = pageInput.toPageable();

		Page<MarketingAsset> assetPage = assetServicePort.getPrimaryAssetsByCampaign(
				marketingCampaignId,
				pageable
		);

		return assetMapper.toPageOutput(assetPage);
	}

	@QueryMapping
	public AssetStatistics assetStatistics(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: assetStatistics for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return assetServicePort.getAssetStatistics(marketingCampaignId);
	}

	@QueryMapping
	public Long totalAssetsByCampaign(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: totalAssetsByCampaign for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return assetServicePort.getTotalAssetsByCampaign(marketingCampaignId);
	}

	@QueryMapping
	public Long totalClicksByCampaign(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: totalClicksByCampaign for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return assetServicePort.getTotalClicksByCampaign(marketingCampaignId);
	}

	@QueryMapping
	public Double averageConversionRateByCampaign(@Argument @NotNull @Positive Long campaignId) {
		log.debug("GraphQL Query: averageConversionRateByCampaign for campaignId: {}", campaignId);

		var marketingCampaignId = new MarketingCampaignId(campaignId);
		return assetServicePort.getAverageConversionRate(marketingCampaignId);
	}
}