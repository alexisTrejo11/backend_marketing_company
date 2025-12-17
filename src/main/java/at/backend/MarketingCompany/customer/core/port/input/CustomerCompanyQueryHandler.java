package at.backend.MarketingCompany.customer.core.port.input;

import at.backend.MarketingCompany.customer.core.application.dto.query.CompanyQueries.*;
import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.adapter.input.web.dto.CompanyMetrics;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerCompanyQueryHandler {
    CustomerCompany getCompanyById(GetCompanyByIdQuery query);
    Page<CustomerCompany> getAllCompanies(GetAllCompaniesQuery query);
    List<CustomerCompany> searchCompanies(SearchCompaniesQuery query);
    List<CustomerCompany> getCompaniesByIndustry(GetCompaniesByIndustryQuery query);
    List<CustomerCompany> getCompaniesByStatus(GetCompaniesByStatusQuery query);
    List<CustomerCompany> getHighValueCompanies(GetHighValueCompaniesQuery query);
    List<CustomerCompany> getStartups(GetStartupsQuery query);
    boolean isCompanyActive(IsCompanyActiveQuery query);
    CompanyMetrics getCompanyMetrics(GetCompanyMetricsQuery query);
}
