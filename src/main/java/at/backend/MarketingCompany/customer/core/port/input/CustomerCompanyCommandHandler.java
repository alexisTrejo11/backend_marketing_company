package at.backend.MarketingCompany.customer.core.port.input;

import at.backend.MarketingCompany.customer.core.application.dto.command.CompanyCommands.*;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;


public interface CustomerCompanyCommandHandler {
    CustomerCompany createCompany(CreateCompanyCommand command);
    CustomerCompany updateCompany(UpdateCompanyCommand command);
    CustomerCompany activateCompany(ActivateCompanyCommand command);
    CustomerCompany blockCompany(BlockCompanyCommand command);
    CustomerCompany deactivateCompany(DeactivateCompanyCommand command);
    CustomerCompany upgradeToEnterprise(UpgradeToEnterpriseCommand command);
    CustomerCompany signContract(SignContractCommand command);
    void deleteCompany(DeleteCompanyCommand command);
    CustomerCompany addContactPerson(AddContactPersonCommand command);
    CustomerCompany removeContactPerson(RemoveContactPersonCommand command);
}
