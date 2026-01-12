package at.backend.MarketingCompany.crm.quote.adapter.output.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.backend.MarketingCompany.crm.quote.adapter.output.entity.QuoteEntity;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;

public interface QuoteJpaRepository extends JpaRepository<QuoteEntity, Long> {

  public List<QuoteEntity> findByOpportunityId(Long opportunityId);

  @Query("SELECT q FROM QuoteEntity q WHERE q.validUntil < CURRENT_DATE")
  public List<QuoteEntity> findExpiredQuotes();

  @Query("SELECT q FROM QuoteEntity q WHERE q.customerCompany.id = :customerCompanyId")
  public Page<QuoteEntity> findByCustomerCompanyId(@Param("customerCompanyId") Long customerCompanyId,
      Pageable pageable);

  public Page<QuoteEntity> findByStatus(QuoteStatus status, Pageable pageable);
}
