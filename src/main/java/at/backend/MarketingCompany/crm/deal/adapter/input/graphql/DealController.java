package at.backend.MarketingCompany.crm.deal.adapter.input.graphql;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.CompleteDealInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.CreateDealInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.GetDealsByCustomerInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.GetDealsByStatusInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.SignDealInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.request.UpdateDealServicesInput;
import at.backend.MarketingCompany.crm.deal.adapter.input.graphql.dto.response.DealResponse;
import at.backend.MarketingCompany.crm.deal.core.application.commands.CancelDealCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.CompleteDealCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.CreateDealCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.MarkDealAsPaidCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.SignDealCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.StartDealExecutionCommand;
import at.backend.MarketingCompany.crm.deal.core.application.commands.UpdateDealServicesCommand;
import at.backend.MarketingCompany.crm.deal.core.application.queries.GetAllDealsQuery;
import at.backend.MarketingCompany.crm.deal.core.application.queries.GetDealByIdQuery;
import at.backend.MarketingCompany.crm.deal.core.application.queries.GetDealsByStatusQuery;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.port.input.DealCommandService;
import at.backend.MarketingCompany.crm.deal.core.port.input.DealQueryService;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DealController {
	private final DealCommandService commandService;
	private final DealQueryService queryService;
	private final DealResponseMapper responseMapper;

	@QueryMapping
	public DealResponse deal(@Argument @Valid @NotBlank String id) {
		log.debug("Fetching deal by ID: {}", id);

		var query = GetDealByIdQuery.from(id);
		Deal deal = queryService.getDealById(query);

		return responseMapper.toResponse(deal);
	}

	@QueryMapping
	public PageResponse<DealResponse> getAllDeals(@Argument @Valid @NotNull PageInput input) {
		log.debug("Fetching all deals with pagination: {}", input);
		var pageQuery = new GetAllDealsQuery(input.toPageable());
		Page<Deal> dealsPage = queryService.getAllDeals(pageQuery);

		return responseMapper.toPagedResponse(dealsPage);
	}

	@QueryMapping
	public PageResponse<DealResponse> getDealsByCustomer(@Argument @Valid GetDealsByCustomerInput input) {
		log.debug("Fetching deals for customer: {}", input.customerId());
		var query = input.toQuery();
		Page<Deal> dealsPage = queryService.getDealsByCustomer(query);

		return responseMapper.toPagedResponse(dealsPage);
	}

	@QueryMapping
	public PageResponse<DealResponse> getDealsByStatus(@Argument @Valid GetDealsByStatusInput input) {
		log.debug("Fetching deals by status: {}", input.statuses());
		GetDealsByStatusQuery query = input.toQuery();
		Page<Deal> dealsPage = queryService.getDealsByStatus(query);

		return responseMapper.toPagedResponse(dealsPage);
	}

	@MutationMapping
	public DealResponse createDeal(@Valid @Argument CreateDealInput input) {
		log.info("Creating new deal for opportunity: {}", input.opportunityId());
		CreateDealCommand command = input.toCommand();
		Deal deal = commandService.createDeal(command);

		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse signDeal(@Valid @Argument SignDealInput input) {
		log.info("Signing deal: {}", input.dealId());
		SignDealCommand command = input.toCommand();
		Deal deal = commandService.signDeal(command);

		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse markDealAsPaid(@Argument String id) {
		log.info("Marking deal as paid: {}", id);
		var command = MarkDealAsPaidCommand.from(id);
		Deal deal = commandService.markDealAsPaid(command);

		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse startDealExecution(@Argument String dealId) {
		log.info("Starting deal execution: {}", dealId);

		var command = StartDealExecutionCommand.from(dealId);
		Deal deal = commandService.startDealExecution(command);

		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse completeDeal(@Valid @Argument CompleteDealInput input) {
		log.info("Completing deal: {}", input.dealId());

		var command = CompleteDealCommand.from(
				input.dealId(),
				input.endDate(),
				input.deliverables());

		Deal deal = commandService.completeDeal(command);
		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse cancelDeal(@Argument String id) {
		log.info("Cancelling deal: {}", id);
		var command = CancelDealCommand.from(id);
		Deal deal = commandService.cancelDeal(command);

		return responseMapper.toResponse(deal);
	}

	@MutationMapping
	public DealResponse updateDealServices(@Valid @Argument UpdateDealServicesInput input) {
		log.info("Updating services for deal: {}", input.dealId());

		var command = UpdateDealServicesCommand.from(
				input.dealId(),
				input.servicePackageIds());

		Deal deal = commandService.updateDealServices(command);
		return responseMapper.toResponse(deal);
	}
}
