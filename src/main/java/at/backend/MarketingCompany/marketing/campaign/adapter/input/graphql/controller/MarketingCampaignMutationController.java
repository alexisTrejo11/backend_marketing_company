package at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.controller;

import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.dto.*;
import at.backend.MarketingCompany.marketing.campaign.adapter.input.graphql.mapper.MarketingOutputMapper;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.AddCampaignSpendingCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.CreateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.command.UpdateCampaignCommand;
import at.backend.MarketingCompany.marketing.campaign.core.application.query.CampaignQuery;
import at.backend.MarketingCompany.marketing.campaign.core.domain.models.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.CampaignStatus;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignCommandServicePort;
import at.backend.MarketingCompany.marketing.campaign.core.ports.input.CampaignQueryServicePort;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MarketingCampaignMutationController {
	private final CampaignCommandServicePort commandServicePort;
  private final MarketingOutputMapper campaignMapper;

  @MutationMapping
  public CampaignOutput createCampaign(@Argument @Valid CreateCampaignInput input) {
    log.debug("GraphQL Mutation: createCampaign with name: {}", input.name());
    
    CreateCampaignCommand command = input.toCommand();
    MarketingCampaign campaign = commandServicePort.createCampaign(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput updateCampaign(@Argument @Valid UpdateCampaignInput input) {
    log.debug("GraphQL Mutation: updateCampaign with id: {}", input.id());
    
    UpdateCampaignCommand command = input.toCommand();
    MarketingCampaign campaign = commandServicePort.updateCampaign(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput launchCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: launchCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.launchCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput pauseCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: pauseCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.pauseCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput resumeCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: resumeCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    MarketingCampaign campaign = commandServicePort.resumeCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput completeCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: completeCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
	  MarketingCampaign campaign = commandServicePort.completeCampaign(campaignId);

		return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput cancelCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: cancelCampaign with id: {}", id);

	  var campaignId = new MarketingCampaignId(id);
	  MarketingCampaign campaign = commandServicePort.cancelCampaign(campaignId);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput addCampaignSpending(@Argument @Valid @NotNull AddSpendingInput input) {
    log.debug("GraphQL Mutation: addCampaignSpending with id: {}, amount: {}", 
        input.campaignId(), input.amount());

    var command = new AddCampaignSpendingCommand(
        new MarketingCampaignId(input.campaignId()),
        BigDecimal.valueOf(input.amount()),
        input.description()
    );
    
    MarketingCampaign campaign = commandServicePort.addSpending(command);
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public CampaignOutput updateCampaignRevenue(@Argument @Valid UpdateRevenueInput input) {
    log.debug("GraphQL Mutation: updateCampaignRevenue with id: {}, revenue: {}", 
        input.campaignId(), input.revenue());
    
    MarketingCampaign campaign = commandServicePort.updateAttributedRevenue(
        new MarketingCampaignId(input.campaignId()),
        BigDecimal.valueOf(input.revenue())
    );
    
    return campaignMapper.toOutput(campaign);
  }

  @MutationMapping
  public boolean deleteCampaign(@Argument @NotNull @Positive Long id) {
    log.debug("GraphQL Mutation: deleteCampaign with id: {}", id);

		var campaignId = new MarketingCampaignId(id);
    commandServicePort.deleteCampaign(campaignId);
    
    return true;
  }
}