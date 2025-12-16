package at.backend.MarketingCompany.crm.deal.core.domain.respository;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DealRepository {

  Deal save(Deal deal);

  Optional<Deal> findById(DealId dealId);

  Page<Deal> findByCustomer(CustomerCompanyId customerCompanyId, Pageable pageable);

  Page<Deal> findAll(Pageable pageable);

  Page<Deal> findByStatuses(Set<DealStatus> statuses, Pageable pageable);

  void delete(Deal deal);

  boolean existsById(DealId dealId);
}
