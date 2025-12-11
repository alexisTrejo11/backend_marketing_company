package at.backend.MarketingCompany.crm.deal.repository.adapter;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.application.DealApplicationServiceImpl;
import at.backend.MarketingCompany.crm.deal.application.commands.*;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealByIdQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByCustomerQuery;
import at.backend.MarketingCompany.crm.deal.application.queries.GetDealsByStatusQuery;
import at.backend.MarketingCompany.crm.deal.repository.dto.input.*;
import at.backend.MarketingCompany.shared.dto.PageInput;
import at.backend.MarketingCompany.crm.deal.repository.dto.output.DealResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DealController {

    private final DealApplicationServiceImpl dealApplicationServiceImpl;
    private final DealGraphQLMapper dealGraphQLMapper;

    @QueryMapping
    public DealResponse deal(@Argument UUID id) {
        log.debug("Fetching deal by ID: {}", id);

        var query = GetDealByIdQuery.from(id);
        var dealResult = dealApplicationServiceImpl.handle(query);

        return dealGraphQLMapper.toGraphQLResponse(dealResult);
    }

    @QueryMapping
    public Page<DealResponse> deals(
            @Argument PageInput pageInput,
            @Argument DealFilterInput filter
    ) {
        log.debug("Fetching deals with filter: {}", filter);

        Pageable pageable = PageRequest.of(
                pageInput != null ? pageInput.page() : 0,
                pageInput != null ? pageInput.size() : 20
        );

        // TODO: Implement filtering logic in the service layer
        var deals = getAllDealsBasedOnFilter(filter);

        return dealGraphQLMapper.toPagedResponse(deals, pageable);
    }

    @QueryMapping
    public List<DealResponse> dealsByCustomer(@Argument UUID customerId) {
        log.debug("Fetching deals for customer: {}", customerId);

        var query = GetDealsByCustomerQuery.from(customerId);
        var deals = dealApplicationServiceImpl.handle(query);

        return deals.stream()
                .map(dealGraphQLMapper::toGraphQLResponse)
                .toList();
    }

    @QueryMapping
    public List<DealResponse> dealsByStatus(@Argument List<DealStatus> statuses) {
        log.debug("Fetching deals by status: {}", statuses);

        var query = new GetDealsByStatusQuery(statuses);
        var deals = dealApplicationServiceImpl.handle(query);

        return deals.stream()
                .map(dealGraphQLMapper::toGraphQLResponse)
                .toList();
    }


    @MutationMapping
    public DealResponse createDeal(@Valid @Argument CreateDealInput input) {
        log.info("Creating new deal for opportunity: {}", input.opportunityId());

        var command = CreateDealCommand.from(
                null, // customerCompanyId se obtendrá de la opportunity
                input.opportunityId(),
                input.servicePackageIds(),
                input.startDate()
        );

        var result = dealApplicationServiceImpl.handle(command);
        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse signDeal(@Valid @Argument SignDealInput input) {
        log.info("Signing deal: {}", input.dealId());

        var command = SignDealCommand.from(
                input.dealId().toString(),
                input.finalAmount(),
                input.terms(),
                input.campaignManagerId()
        );

        var result = dealApplicationServiceImpl.handle(command);
        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse markDealAsPaid(@Argument UUID dealId) {
        log.info("Marking deal as paid: {}", dealId);

        var command = MarkDealAsPaidCommand.from(dealId);
        var result = dealApplicationServiceImpl.handle(command);

        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse startDealExecution(@Argument UUID dealId) {
        log.info("Starting deal execution: {}", dealId);

        var command = StartDealExecutionCommand.from(dealId);
        var result = dealApplicationServiceImpl.handle(command);

        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse completeDeal(@Valid @Argument CompleteDealInput input) {
        log.info("Completing deal: {}", input.dealId());

        var command = CompleteDealCommand.from(
                input.dealId(),
                input.endDate(),
                input.deliverables()
        );

        var result = dealApplicationServiceImpl.handle(command);
        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse cancelDeal(@Argument UUID dealId) {
        log.info("Cancelling deal: {}", dealId);

        var command = CancelDealCommand.from(dealId);
        var result = dealApplicationServiceImpl.handle(command);

        return dealGraphQLMapper.toGraphQLResponse(result);
    }

    @MutationMapping
    public DealResponse updateDealServices(@Valid @Argument UpdateDealServicesInput input) {
        log.info("Updating services for deal: {}", input.dealId());

        var command = UpdateDealServicesCommand.from(
                input.dealId(),
                input.servicePackageIds()
        );

        var result = dealApplicationServiceImpl.handle(command);
        return dealGraphQLMapper.toGraphQLResponse(result);
    }


    private List<DealResponse> getAllDealsBasedOnFilter(DealFilterInput filter) {
        // TODO: Implement filtering logic based on the filter input
        return List.of();
    }
}