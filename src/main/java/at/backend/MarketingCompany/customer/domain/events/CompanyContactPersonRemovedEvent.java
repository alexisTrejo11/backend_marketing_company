package at.backend.MarketingCompany.customer.domain.events;

import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CompanyContactPersonRemovedEvent extends CompanyEvent {
    private final String contactPersonName;

    public CompanyContactPersonRemovedEvent(CustomerCompanyId companyId, String contactPersonName) {
        super(companyId);
        this.contactPersonName = contactPersonName;
    }
}
