package at.backend.MarketingCompany.crm.quote.adapter.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteItemEntity;

public interface QuoteItemJpaRepository extends JpaRepository<QuoteItemEntity, String> {

}
