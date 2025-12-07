package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerEntity;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, String> {

}
