package at.backend.MarketingCompany.crm.deal.v2.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDealRepository extends JpaRepository<DealEntity, UUID> {

}