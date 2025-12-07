package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.common.PageResponse;
import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.customer.application.dto.command.ActivateCustomerCommand;
import at.backend.MarketingCompany.customer.application.dto.command.BlockCustomerCommand;
import at.backend.MarketingCompany.customer.application.dto.command.DeleteCustomerCommand;
import at.backend.MarketingCompany.customer.application.dto.query.GetAllCustomersQuery;
import at.backend.MarketingCompany.customer.application.dto.query.GetCustomerByIdQuery;
import at.backend.MarketingCompany.customer.application.dto.query.HasCompetitorsQuery;
import at.backend.MarketingCompany.customer.application.dto.query.HasSocialMediaHandlesQuery;
import at.backend.MarketingCompany.customer.application.dto.query.IsCustomerBlockedQuery;
import at.backend.MarketingCompany.customer.application.port.input.CustomerCommandHandler;
import at.backend.MarketingCompany.customer.application.port.input.CustomerQueryHandler;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CustomerInput;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CustomerResponse;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CustomerUpdateInput;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.mapper.CustomerResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@Controller
@RequiredArgsConstructor
public class CustomerController {
  private final CustomerCommandHandler customerCommandHandler;
  private final CustomerQueryHandler customerQueryHandler;
  private final CustomerResponseMapper customerResponseMapper;

  @QueryMapping
  public PageResponse<CustomerResponse> getAllCustomers(@Argument PageInput pageInput) {
    var query = new GetAllCustomersQuery(pageInput.toPageable());

    var customers = customerQueryHandler.handle(query);

    return customerResponseMapper.toPageResponse(customers);
  }

  public CustomerResponse getCustomerById(@Argument String id) {
    var query = GetCustomerByIdQuery.of(id);

    var customer = customerQueryHandler.handle(query);

    return customerResponseMapper.toResponse(customer);
  }

  @QueryMapping
  public boolean isCustomerBlocked(@Argument String id) {
    var query = IsCustomerBlockedQuery.of(id);
    return customerQueryHandler.handle(query);
  }

  @QueryMapping
  public boolean hasSocialMediaHandles(@Argument String id) {
    var query = HasSocialMediaHandlesQuery.of(id);
    return customerQueryHandler.handle(query);
  }

  @QueryMapping
  public boolean hasCompetitors(@Argument String id) {
    var query = HasCompetitorsQuery.of(id);
    return customerQueryHandler.handle(query);
  }

  @MutationMapping
  public CustomerResponse createCustomer(@Argument @Valid CustomerInput input) {
    var command = input.toCommand();
    var customer = customerCommandHandler.handle(command);
    return customerResponseMapper.toResponse(customer);
  }

  @MutationMapping
  public CustomerResponse updateCustomer(
      @Argument String id,
      @Argument @Valid CustomerUpdateInput input) {

    var command = input.toCommand(id);

    var customer = customerCommandHandler.handle(command);

    return customerResponseMapper.toResponse(customer);
  }

  @MutationMapping
  public boolean deleteCustomer(@Argument @NotBlank String id) {
    var command = DeleteCustomerCommand.of(id);
    customerCommandHandler.handle(command);

    return true;
  }

  @MutationMapping
  public boolean blockCustomer(@Argument @NotBlank String id) {
    BlockCustomerCommand command = BlockCustomerCommand.of(id);
    customerCommandHandler.handle(command);
    return true;
  }

  @MutationMapping
  public boolean activateCustomer(@Argument String id) {
    var command = ActivateCustomerCommand.of(id);
    customerCommandHandler.handle(command);

    return true;
  }
}
