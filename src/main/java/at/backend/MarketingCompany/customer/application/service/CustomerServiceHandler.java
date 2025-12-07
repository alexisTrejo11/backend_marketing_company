package at.backend.MarketingCompany.customer.application.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import at.backend.MarketingCompany.customer.application.dto.command.*;
import at.backend.MarketingCompany.customer.application.dto.query.*;
import at.backend.MarketingCompany.customer.application.port.input.CustomerCommandHandler;
import at.backend.MarketingCompany.customer.application.port.input.CustomerQueryHandler;
import at.backend.MarketingCompany.customer.application.port.ouput.CustomerRepositoryPort;
import at.backend.MarketingCompany.customer.domain.Customer;
import at.backend.MarketingCompany.customer.domain.excpetions.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceHandler implements CustomerQueryHandler, CustomerCommandHandler {
  private final CustomerRepositoryPort customerRepository;

  @Override
  public Customer handle(CustomerCreateCommand command) {
    var createParams = command.toParams();
    Customer.create(createParams);

    Customer customer = Customer.create(createParams);
    return customerRepository.save(customer);
  }

  @Override
  public Customer handle(CustomerUpdateCommand command) {
    Customer customer = customerRepository.findById(command.id())
        .orElseThrow(() -> new CustomerNotFoundException(command.id()));

    if (command.personalInfo() != null) {
      customer.updatePersonalInfo(command.personalInfo());
    }

    if (command.businessProfile() != null) {
      customer.updateBusinessProfile(command.businessProfile());
    }
    if (command.contactDetails() != null) {
      customer.updateContactDetails(command.contactDetails());
    }

    return customerRepository.save(customer);
  }

  @Override
  public Customer handle(ActivateCustomerCommand command) {
    Customer customer = customerRepository.findById(command.id())
        .orElseThrow(() -> new CustomerNotFoundException(command.id()));

    customer.activate();

    return customerRepository.save(customer);

  }

  @Override
  public Customer handle(BlockCustomerCommand command) {
    Customer customer = customerRepository.findById(command.id())
        .orElseThrow(() -> new CustomerNotFoundException(command.id()));

    customer.block();

    return customerRepository.save(customer);
  }

  @Override
  public Customer handle(DeleteCustomerCommand command) {
    Customer customer = customerRepository.findById(command.id())
        .orElseThrow(() -> new CustomerNotFoundException(command.id()));

    customerRepository.delete(customer.getId());

    return customer;
  }

  @Override
  public Page<Customer> handle(GetAllCustomersQuery query) {
    return customerRepository.findAll(query.pageable());
  }

  @Override
  public Customer handle(GetCustomerByIdQuery query) {
    return customerRepository.findById(query.id())
        .orElseThrow(() -> new CustomerNotFoundException(query.id()));

  }

  @Override
  public boolean handle(IsCustomerBlockedQuery query) {
    Customer customer = customerRepository.findById(query.id())
        .orElseThrow(() -> new CustomerNotFoundException(query.id()));
    return customer.isBlocked();
  }

  @Override
  public boolean handle(HasSocialMediaHandlesQuery query) {
    Customer customer = customerRepository.findById(query.id())
        .orElseThrow(() -> new CustomerNotFoundException(query.id()));
    return customer.hasSocialMediaHandles();
  }

  @Override
  public boolean handle(HasCompetitorsQuery query) {
    Customer customer = customerRepository.findById(query.id())
        .orElseThrow(() -> new CustomerNotFoundException(query.id()));

    return customer.hasCompetitors();
  }

}
