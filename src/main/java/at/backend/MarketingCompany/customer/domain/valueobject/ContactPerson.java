package at.backend.MarketingCompany.customer.domain.valueobject;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;

public record ContactPerson(
    PersonName name,
    Email email,
    PhoneNumber phone,
    String position,
    Department department,
    boolean isDecisionMaker,
    boolean isPrimaryContact
) {
    
    public enum Department {
        SALES,
        MARKETING,
        IT,
        FINANCE,
        HR,
        OPERATIONS,
        EXECUTIVE,
        OTHER
    }
    
    public ContactPerson {
        if (name == null) {
            throw new IllegalArgumentException("Name is required for contact person");
        }
        if (email == null && phone == null) {
            throw new IllegalArgumentException("At least one contact method is required");
        }
        if (position == null || position.isBlank()) {
            position = "Not specified";
        }
        if (department == null) {
            department = Department.OTHER;
        }
    }
    
    public String getFullName() {
        return name.getFullName();
    }
    
    public boolean isExecutive() {
        return position.toLowerCase().contains("ceo") 
            || position.toLowerCase().contains("cto")
            || position.toLowerCase().contains("cfo")
            || department == Department.EXECUTIVE;
    }
}