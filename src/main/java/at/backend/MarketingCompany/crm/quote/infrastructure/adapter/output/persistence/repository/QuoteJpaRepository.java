package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.repository;

import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, String> {

}
