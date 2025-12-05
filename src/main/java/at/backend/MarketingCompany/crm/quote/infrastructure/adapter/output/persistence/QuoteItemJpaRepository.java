package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence;

import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.output.persistence.entity.QuoteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteItemJpaRepository extends JpaRepository<QuoteItemEntity, String> {

}