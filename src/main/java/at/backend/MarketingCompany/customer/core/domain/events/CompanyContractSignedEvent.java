package at.backend.MarketingCompany.customer.core.domain.events;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CompanyContractSignedEvent extends CompanyEvent {
    private final String contractId;
    private final BigDecimal contractValue;
    private final String contractType;
    private final long durationMonths;
    
    public CompanyContractSignedEvent(CustomerCompanyId companyId,
                                      String contractId,
                                      BigDecimal contractValue,
                                      String contractType,
                                      long durationMonths) {
        super(companyId);
        this.contractId = contractId;
        this.contractValue = contractValue;
        this.contractType = contractType;
        this.durationMonths = durationMonths;
    }
}
