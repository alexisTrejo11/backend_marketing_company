package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.controller;

import at.backend.MarketingCompany.customer.application.dto.command.CompanyCommands.*;
import at.backend.MarketingCompany.customer.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.application.port.input.CustomerCompanyCommandHandler;
import at.backend.MarketingCompany.customer.application.port.input.CustomerCompanyQueryHandler;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.*;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyMetrics;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyResponse;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.mapper.CompanyResponseMapper;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CompanyController {
    private final CustomerCompanyCommandHandler companyCommandHandler;
    private final CustomerCompanyQueryHandler companyQueryHandler;
    private final CompanyResponseMapper companyResponseMapper;

    @QueryMapping
    public PageResponse<CompanyResponse> getAllCompanies(@Argument PageInput pageInput) {
        var query = new GetAllCompaniesQuery(pageInput.toPageable());

        var companies = companyQueryHandler.handle(query);

        return companyResponseMapper.toPageResponse(companies);
    }

    @QueryMapping
    public CompanyResponse getCompanyById(@Argument @NotBlank String id) {
        var query = new GetCompanyByIdQuery(new CustomerCompanyId(id));

        var company = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponse(company);
    }

    @QueryMapping
    public List<CompanyResponse> searchCompanies(@Argument String searchTerm, @Argument PageInput pageInput) {
        var query = new SearchCompaniesQuery(searchTerm, pageInput.toPageable());

        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public List<CompanyResponse> getCompaniesByIndustry(@Argument @Valid @NotBlank String industryCode) {
        var query = new GetCompaniesByIndustryQuery(industryCode);

        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public List<CompanyResponse> getCompaniesByStatus(@Argument CompanyStatus status) {
        var query = new GetCompaniesByStatusQuery(status);

        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public List<CompanyResponse> getHighValueCompanies(@Argument @Valid @NotNull Double minRevenue) {
        var query = new GetHighValueCompaniesQuery(minRevenue != null ? BigDecimal.valueOf(minRevenue) : null);


        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public List<CompanyResponse> getStartups(@Argument @Valid @NotNull Integer startYearSince) {
        var query = new GetStartupsQuery(startYearSince);
        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public List<CompanyResponse> getCompaniesWithExpiringContracts(@Argument Integer daysThreshold) {
        var query = new GetCompaniesWithExpiringContractsQuery(daysThreshold);
        var companies = companyQueryHandler.handle(query);
        return companyResponseMapper.toResponses(companies);
    }

    @QueryMapping
    public boolean isCompanyActive(@Argument @NotBlank String id) {
        var query = IsCompanyActiveQuery.from(id);
        return companyQueryHandler.handle(query);
    }

    @QueryMapping
    public boolean hasActiveContract(@Argument @NotBlank String id) {
        var query = HasActiveContractQuery.from(id);

        return companyQueryHandler.handle(query);
    }

    @QueryMapping
    public CompanyMetrics getCompanyMetrics(@Argument String companyId) {
        var query = GetCompanyMetricsQuery.from(companyId);
        return companyQueryHandler.handle(query);
    }

    @MutationMapping
    public CompanyResponse createCompany(@Argument @Valid CreateCompanyCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse updateCompany(@Valid @Argument UpdateCompanyCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse activateCompany(@Argument @Valid @NotBlank String id, @Argument String activationNotes) {
        var command = ActivateCompanyCommand.from(id, activationNotes);

        var company = companyCommandHandler.handle(command);

        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse blockCompany(@Argument @Valid BlockCompanyCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse deactivateCompany(@Argument @Valid DeactivateCompanyCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse upgradeToEnterprise(@Argument @Valid @NotNull UpgradeToEnterpriseCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public CompanyResponse signContract(@Argument @Valid @NotNull SignContractCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }

    @MutationMapping
    public boolean deleteCompany(@Argument @Valid @NotBlank String id) {
        var command = DeleteCompanyCommand.builder()
                .id(new CustomerCompanyId(id))
                .build();

        companyCommandHandler.handle(command);
        return true;
    }

    @MutationMapping
    public CompanyResponse addContactPerson(@Argument @Valid @NotNull AddContactPersonCommand command) {
        var company = companyCommandHandler.handle(command);
        return companyResponseMapper.toResponse(company);
    }
}