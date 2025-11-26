package at.backend.MarketingCompany.crm.deal.v2.domain.interfaces;

import at.backend.MarketingCompany.crm.deal.v2.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.v2.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.customer.domain.ValueObjects.CustomerId;

import java.util.List;
import java.util.Optional;

public interface DealRepository {
    
    Deal save(Deal deal);
    
    Optional<Deal> findById(DealId dealId);
    
    List<Deal> findByCustomer(CustomerId customerId);
    
    List<Deal> findByStatuses(List<String> statuses);
    
    void delete(Deal deal);
    
    boolean existsById(DealId dealId);
}