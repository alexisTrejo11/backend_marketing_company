package at.backend.MarketingCompany.customer.application.port.input;

import at.backend.MarketingCompany.customer.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CompanyMetrics;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerCompanyQueryHandler {
    CustomerCompany handle(GetCompanyByIdQuery query);
    Page<CustomerCompany> handle(GetAllCompaniesQuery query);
    List<CustomerCompany> handle(SearchCompaniesQuery query);
    List<CustomerCompany> handle(GetCompaniesByIndustryQuery query);
    List<CustomerCompany> handle(GetCompaniesByStatusQuery query);
    List<CustomerCompany> handle(GetHighValueCompaniesQuery query);
    List<CustomerCompany> handle(GetStartupsQuery query);
    List<CustomerCompany> handle(GetCompaniesWithExpiringContractsQuery query);
    boolean handle(IsCompanyActiveQuery query);
    boolean handle(HasActiveContractQuery query);
    CompanyMetrics handle(GetCompanyMetricsQuery query);
}
