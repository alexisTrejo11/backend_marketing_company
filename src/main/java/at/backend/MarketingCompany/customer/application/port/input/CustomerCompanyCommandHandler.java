package at.backend.MarketingCompany.customer.application.port.input;

import at.backend.MarketingCompany.customer.application.dto.command.*;
import at.backend.MarketingCompany.customer.application.dto.command.CompanyCommands.*;

import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;


public interface CustomerCompanyCommandHandler {
    CustomerCompany handle(CreateCompanyCommand command);
    CustomerCompany handle(UpdateCompanyCommand command);
    CustomerCompany handle(ActivateCompanyCommand command);
    CustomerCompany handle(BlockCompanyCommand command);
    CustomerCompany handle(DeactivateCompanyCommand command);
    CustomerCompany handle(UpgradeToEnterpriseCommand command);
    CustomerCompany handle(SignContractCommand command);
    void handle(DeleteCompanyCommand command);
    CustomerCompany handle(AddContactPersonCommand command);
    CustomerCompany handle(RemoveContactPersonCommand command);
}
