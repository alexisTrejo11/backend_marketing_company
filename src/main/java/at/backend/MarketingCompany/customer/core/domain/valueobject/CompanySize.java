package at.backend.MarketingCompany.customer.core.domain.valueobject;

public enum CompanySize {
    MICRO("1-9 employees"),
    SMALL("10-49 employees"),
    MEDIUM("50-249 employees"),
    LARGE("250-999 employees"),
    ENTERPRISE("1000+ employees"),
    UNKNOWN("Not specified");

    CompanySize(String description) {
    }
    
    public boolean isSMB() {
        return this == MICRO || this == SMALL || this == MEDIUM;
    }
    
    public boolean isEnterprise() {
        return this == LARGE || this == ENTERPRISE;
    }
    
    public static CompanySize fromEmployeeCount(int employees) {
        if (employees <= 0) return UNKNOWN;
        if (employees <= 9) return MICRO;
        if (employees <= 49) return SMALL;
        if (employees <= 249) return MEDIUM;
        if (employees <= 999) return LARGE;
        return ENTERPRISE;
    }
}