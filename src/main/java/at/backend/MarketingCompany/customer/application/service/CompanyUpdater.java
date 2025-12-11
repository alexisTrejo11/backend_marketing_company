package at.backend.MarketingCompany.customer.application.service;

import at.backend.MarketingCompany.customer.application.dto.command.CompanyCommands;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyName;

class CompanyUpdater {
    private final CustomerCompany company;
    private boolean hasChanges = false;
    
    CompanyUpdater(CustomerCompany company) {
        this.company = company;
    }
    
    CompanyUpdater updateName(CompanyName newName) {
        if (newName != null) {
            company.updateCompanyName(newName);
            hasChanges = true;
        }
        return this;
    }
    
    CompanyUpdater updateProfile(
        CompanyCommands.UpdateCompanyCommand.UpdateCompanyProfileCommand cmd,
        CompanyMapper mapper
    ) {
        if (cmd == null) return this;
        
        // Profile update logic here
        hasChanges = true;
        return this;
    }
    
    CompanyUpdater updateBilling(
        CompanyCommands.UpdateCompanyCommand.UpdateCompanyBillingCommand cmd
    ) {
        if (cmd != null && cmd.taxId() != null && !cmd.taxId().isBlank()) {
            // Billing update logic
            hasChanges = true;
        }
        return this;
    }
    
    boolean hasChanges() {
        return hasChanges;
    }
}