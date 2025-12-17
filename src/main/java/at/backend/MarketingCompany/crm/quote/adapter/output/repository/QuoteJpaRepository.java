package at.backend.MarketingCompany.crm.quote.adapter.output.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteEntity;

public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, Long> {

}
