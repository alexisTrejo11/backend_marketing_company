package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.customer.application.port.ouput.CustomerRepositoryPort;
import at.backend.MarketingCompany.customer.domain.Customer;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerEntity;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.mapper.CustomerEntityMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerRepositoryPort {
  private final CustomerJpaRepository customerJpaRepository;
  private final CustomerEntityMapper customerMapper;

  @Override
  public Customer save(Customer customer) {
    CustomerEntity entity = customerMapper.toEntity(customer);
    CustomerEntity savedEntity = customerJpaRepository.save(entity);
    return customerMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Customer> findById(CustomerId id) {
    return customerJpaRepository.findById(id.value())
        .map(customerMapper::toDomain);
  }

  @Override
  public Page<Customer> findAll(Pageable pageable) {
    return customerJpaRepository.findAll(pageable)
        .map(customerMapper::toDomain);
  }

  @Override
  public void delete(CustomerId id) {
    customerJpaRepository.deleteById(id.value());
  }

  @Override
  public boolean existsById(CustomerId id) {
    return customerJpaRepository.existsById(id.value());
  }
}
