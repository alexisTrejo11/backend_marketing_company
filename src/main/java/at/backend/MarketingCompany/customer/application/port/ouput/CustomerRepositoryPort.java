package at.backend.MarketingCompany.customer.application.port.ouput;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import at.backend.MarketingCompany.customer.domain.Customer;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;

public interface CustomerRepositoryPort {
  Customer save(Customer customer);

  Optional<Customer> findById(CustomerId id);

  Page<Customer> findAll(Pageable pageable);

  void delete(CustomerId id);

  boolean existsById(CustomerId id);
}
