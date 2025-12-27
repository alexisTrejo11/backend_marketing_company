package at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.asset.adapter.input.graphql.mapper.AssetOutputMapper;
import at.backend.MarketingCompany.marketing.asset.core.application.command.CreateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetCommand;
import at.backend.MarketingCompany.marketing.asset.core.application.command.UpdateAssetPerformanceCommand;
import at.backend.MarketingCompany.marketing.asset.core.domain.entity.MarketingAsset;
import at.backend.MarketingCompany.marketing.asset.core.domain.valueobject.MarketingAssetId;
import at.backend.MarketingCompany.marketing.asset.core.port.input.AssetServicePort;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AssetMutationController {
	private final AssetServicePort assetServicePort;
	private final AssetOutputMapper assetMapper;

	@MutationMapping
	public AssetOutput createAsset(@Argument @Valid CreateAssetInput input) {
		log.debug("GraphQL Mutation: createAsset with name: {}", input.name());

		CreateAssetCommand command = input.toCommand();
		MarketingAsset asset = assetServicePort.createAsset(command);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public AssetOutput updateAsset(@Argument @Valid UpdateAssetRequestInput input) {
		log.debug("GraphQL Mutation: updateAsset with id: {}", input.id());

		UpdateAssetCommand command = input.toCommand();
		MarketingAsset asset = assetServicePort.updateAsset(command);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public AssetOutput updateAssetPerformance(@Argument @Valid UpdateAssetPerformanceInput input) {
		log.debug("GraphQL Mutation: updateAssetPerformance with id: {}", input.assetId());
		UpdateAssetPerformanceCommand command = input.toCommand();
		MarketingAsset asset = assetServicePort.updateAssetPerformance(command);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public AssetOutput markAssetAsPrimary(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: markAssetAsPrimary with id: {}", id);

		var assetId = new MarketingAssetId(id);
		MarketingAsset asset = assetServicePort.markAsPrimary(assetId);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public AssetOutput activateAsset(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: activateAsset with id: {}", id);

		var assetId = new MarketingAssetId(id);
		MarketingAsset asset = assetServicePort.activateAsset(assetId);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public AssetOutput archiveAsset(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: archiveAsset with id: {}", id);

		var assetId = new MarketingAssetId(id);
		MarketingAsset asset = assetServicePort.archiveAsset(assetId);

		return assetMapper.toOutput(asset);
	}

	@MutationMapping
	public boolean deleteAsset(@Argument @NotNull @Positive Long id) {
		log.debug("GraphQL Mutation: deleteAsset with id: {}", id);

		var assetId = new MarketingAssetId(id);
		assetServicePort.deleteAsset(assetId);

		return true;
	}
}