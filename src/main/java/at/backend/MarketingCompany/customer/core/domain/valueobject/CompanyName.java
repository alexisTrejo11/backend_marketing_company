package at.backend.MarketingCompany.customer.core.domain.valueobject;

import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;

public record CompanyName(String value) {
    public CompanyName {
        if (value == null || value.isBlank()) {
            throw new CustomerDomainException("Company name cannot be null or empty");
        }
        if (value.length() < 2) {
            throw new CustomerDomainException("Company name must be at least 2 characters");
        }
        if (value.length() > 100) {
            throw new CustomerDomainException("Company name cannot exceed 100 characters");
        }
    }
    
    public String getNormalized() {
        return value.trim().toUpperCase();
    }
    
    public boolean isSimilarTo(String otherName) {
        return getNormalized().contains(otherName.toUpperCase()) 
            || otherName.toUpperCase().contains(getNormalized());
    }
}