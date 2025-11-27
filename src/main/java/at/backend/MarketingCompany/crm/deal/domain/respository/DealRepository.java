package at.backend.MarketingCompany.crm.deal.domain.respository;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.Deal;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.CustomerId;

import java.util.List;
import java.util.Optional;

public interface DealRepository {
    
    Deal save(Deal deal);
    
    Optional<Deal> findById(DealId dealId);
    
    List<Deal> findByCustomer(CustomerId customerId);
    
    List<Deal> findByStatuses(List<DealStatus> statuses);
    
    void delete(Deal deal);
    
    boolean existsById(DealId dealId);
}