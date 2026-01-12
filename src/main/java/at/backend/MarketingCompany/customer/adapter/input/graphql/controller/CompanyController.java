package at.backend.MarketingCompany.customer.adapter.input.graphql.controller;

import at.backend.MarketingCompany.customer.adapter.input.graphql.dto.CompanyMetrics;
import at.backend.MarketingCompany.customer.adapter.input.graphql.dto.CompanyResponse;
import at.backend.MarketingCompany.customer.adapter.input.graphql.mapper.CompanyResponseMapper;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.customer.core.application.dto.command.CompanyCommands.*;
import at.backend.MarketingCompany.customer.core.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.core.port.input.CustomerCompanyCommandHandler;
import at.backend.MarketingCompany.customer.core.port.input.CustomerCompanyQueryHandler;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
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
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'ANALYST')")
  public PageResponse<CompanyResponse> companies(@Argument PageInput pageInput) {
    var query = new GetAllCompaniesQuery(pageInput.toPageable());

    Page<CustomerCompany> companyPage = companyQueryHandler.getAllCompanies(query);

    return companyResponseMapper.toPageResponse(companyPage);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("isAuthenticated()")
  public CompanyResponse company(@Argument @NotNull @Positive Long id) {
    var query = new GetCompanyByIdQuery(new CustomerCompanyId(id));
    CustomerCompany company = companyQueryHandler.getCompanyById(query);
    return companyResponseMapper.toResponse(company);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("isAuthenticated()")
  public List<CompanyResponse> searchCompanies(@Argument String searchTerm, @Argument PageInput pageInput) {
    var query = new SearchCompaniesQuery(searchTerm, pageInput.toPageable());
    List<CustomerCompany> companies = companyQueryHandler.searchCompanies(query);
    return companyResponseMapper.toResponses(companies);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("isAuthenticated()")
  public List<CompanyResponse> companiesByIndustry(@Argument @Valid @NotBlank String industryCode) {
    var query = new GetCompaniesByIndustryQuery(industryCode);
    List<CustomerCompany> companies = companyQueryHandler.getCompaniesByIndustry(query);
    return companyResponseMapper.toResponses(companies);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public List<CompanyResponse> companiesByStatus(@Argument CompanyStatus status) {
    var query = new GetCompaniesByStatusQuery(status);
    List<CustomerCompany> companies = companyQueryHandler.getCompaniesByStatus(query);
    return companyResponseMapper.toResponses(companies);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'ANALYST')")
  public List<CompanyResponse> highValueCompanies(@Argument @Valid @NotNull Double minRevenue) {
    var query = new GetHighValueCompaniesQuery(minRevenue != null ? BigDecimal.valueOf(minRevenue) : null);
    List<CustomerCompany> companies = companyQueryHandler.getHighValueCompanies(query);
    return companyResponseMapper.toResponses(companies);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'ANALYST')")
  public List<CompanyResponse> companyStartups(@Argument @Valid @NotNull Integer startYearSince) {
    var query = new GetStartupsQuery(startYearSince);
    List<CustomerCompany> companies = companyQueryHandler.getStartups(query);
    return companyResponseMapper.toResponses(companies);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("isAuthenticated()")
  public boolean isCompanyActive(@Argument @NotBlank String id) {
    var query = IsCompanyActiveQuery.from(id);
    return companyQueryHandler.isCompanyActive(query);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'ANALYST')")
  public CompanyMetrics companyMetrics(@Argument String companyId) {
    var query = GetCompanyMetricsQuery.from(companyId);
    return companyQueryHandler.getCompanyMetrics(query);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public CompanyResponse createCompany(@Argument @Valid CreateCompanyCommand command) {
    var company = companyCommandHandler.createCompany(command);
    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES')")
  public CompanyResponse updateCompany(@Valid @Argument UpdateCompanyCommand command) {
    var company = companyCommandHandler.updateCompany(command);
    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public CompanyResponse activateCompany(@Argument @Valid @NotBlank String id, @Argument String activationNotes) {
    var command = ActivateCompanyCommand.from(id, activationNotes);

    var company = companyCommandHandler.activateCompany(command);

    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasRole('ADMIN')")
  public CompanyResponse blockCompany(@Argument @Valid BlockCompanyCommand command) {
    var company = companyCommandHandler.blockCompany(command);
    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public CompanyResponse deactivateCompany(@Argument @Valid DeactivateCompanyCommand command) {
    var company = companyCommandHandler.deactivateCompany(command);
    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  @GraphQLRateLimit("resource-mutation")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public CompanyResponse upgradeToEnterprise(@Argument @Valid @NotNull UpgradeToEnterpriseCommand command) {
    var company = companyCommandHandler.upgradeToEnterprise(command);
    return companyResponseMapper.toResponse(company);
  }

  @MutationMapping
  public boolean deleteCompany(@Argument @Valid @NotNull @Positive Long id) {
    var command = new DeleteCompanyCommand(new CustomerCompanyId(id));

    companyCommandHandler.deleteCompany(command);
    return true;
  }

  @MutationMapping
  public CompanyResponse addContactPerson(@Argument @Valid @NotNull AddContactPersonCommand command) {
    var company = companyCommandHandler.addContactPerson(command);
    return companyResponseMapper.toResponse(company);
  }
}
