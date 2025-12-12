package at.backend.MarketingCompany.customer.application.service;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.customer.application.dto.command.CompanyCommands.*;
import at.backend.MarketingCompany.customer.application.port.input.CustomerCompanyCommandHandler;
import at.backend.MarketingCompany.customer.application.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.exceptions.CompanyNotFoundException;
import at.backend.MarketingCompany.customer.domain.valueobject.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerCompanyCommandHandlerImpl implements CustomerCompanyCommandHandler {
    private final CustomerCompanyRepositoryPort companyRepository;
    private final CompanyMapper companyMapper;
    private final ContractFactory contractFactory;

    @Override
    public CustomerCompany handle(CreateCompanyCommand command) {
        log.info("Creating company: {} with {} contact persons",
                command.companyName(), command.contactPersons().size());

        Set<ContactPerson> contactPersons = companyMapper.toContactPersons(
                command.contactPersons()
        );
        log.info("Mapped {} contact persons for company: {}", contactPersons.size(), command.companyName());

        CompanyProfile companyProfile = companyMapper.toCompanyProfile(
                command.industry(),
                command.companySize(),
                command.employeeCount(),
                command.foundingYear(),
                command.missionStatement(),
                command.targetMarket(),
                command.keyProducts()
        );
        log.info("Created company profile for company: {}", command.companyName());

        CustomerCompany company = CustomerCompany.create(
                new CompanyName(command.companyName()),
                companyProfile,
                contactPersons
        );
        log.info("Initialized CustomerCompany entity for: {}", command.companyName());


        Optional.ofNullable(command.taxId())
                .filter(taxId -> !taxId.isBlank())
                .map(taxId -> BillingInformation.create(
                        taxId,
                        BillingInformation.PaymentMethod.INVOICE
                ))
                .ifPresent(company::setBillingInfo);
        log.info("Set billing information for company: {}", command.companyName());

        CustomerCompany savedCompany = companyRepository.save(company);
        log.info("Company created successfully with ID: {}", savedCompany.getId());
        return savedCompany;
    }

    @Override
    public CustomerCompany handle(UpdateCompanyCommand command) {
        log.info("Updating company: {}", command.id());

        CustomerCompany company = findCompanyOrThrow(command.id());

        CompanyUpdater updater = new CompanyUpdater(company);

        var name = companyMapper.toCompanyName(command.companyName());
        updater.updateName(name)
                .updateProfile(command.profileCommand(), companyMapper)
                .updateBilling(command.billingCommand());

        if (!updater.hasChanges()) {
            log.warn("No fields updated for company: {}", command.id());
        } else {
            log.info("Company {} updated successfully", command.id());
        }

        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(ActivateCompanyCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());
        company.activate();

        log.info("Company {} ({}) activated. Notes: {}",
                command.id(), company.getCompanyName(), command.activationNotes());
        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(BlockCompanyCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());
        company.block(command.reason());

        log.warn("Company {} ({}) blocked. Reason: {}",
                command.id(), company.getCompanyName(), command.reason());
        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(DeactivateCompanyCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());
        company.deactivate(command.reason());

        log.info("Company {} ({}) deactivated. Reason: {}",
                command.id(), company.getCompanyName(), command.reason());
        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(UpgradeToEnterpriseCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());

        Currency currency;
        String currencyCode = command.currency() != null ? command.currency() : "USD";
        try {
            currency = Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currencyCode);
        }

        AnnualRevenue revenue = new AnnualRevenue(
                command.annualRevenue(),
                currency,
                null
        );

        company.upgradeToEnterprise(revenue);

        log.info("Company {} ({}) upgraded to enterprise. Revenue: {} {}",
                command.id(), company.getCompanyName(),
                command.annualRevenue(), currency.getCurrencyCode());

        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(SignContractCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());

        ContractDetails contract = contractFactory.createContract(
                command.contractId(),
                command.contractType(),
                command.monthlyFee(),
                command.durationMonths(),
                command.autoRenewal()
        );

        company.signContract(contract);

        log.info("Contract {} signed for company {} ({}). Type: {}",
                command.contractId(), command.id(), company.getCompanyName(),
                contract.type());

        return companyRepository.save(company);
    }

    @Override
    public void handle(DeleteCompanyCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.id());

        log.info("Deleting company {} ({})", command.id(), company.getCompanyName());

        companyRepository.delete(command.id());

        log.info("Company {} deleted successfully", command.id());
    }

    @Override
    public CustomerCompany handle(AddContactPersonCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.companyId());

        ContactPerson contactPerson = companyMapper.toContactPerson(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.position(),
                command.department(),
                command.isDecisionMaker()
        );
        company.addContactPerson(contactPerson);

        log.info("Contact person {} {} added to company {} ({})",
                command.firstName(), command.lastName(),
                command.companyId(), company.getCompanyName());

        return companyRepository.save(company);
    }

    @Override
    public CustomerCompany handle(RemoveContactPersonCommand command) {
        CustomerCompany company = findCompanyOrThrow(command.companyId());

        boolean removed = company.removeContactPerson(Email.from(command.email()));
        if (removed) {
            log.info("Contact person {} removed from company {} ({})",
                    command.email(), command.companyId(), company.getCompanyName());
        } else {
            log.warn("Contact person {} not found in company {}",
                    command.email(), command.companyId());
        }

        return companyRepository.save(company);
    }

    private CustomerCompany findCompanyOrThrow(CustomerCompanyId id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }
}