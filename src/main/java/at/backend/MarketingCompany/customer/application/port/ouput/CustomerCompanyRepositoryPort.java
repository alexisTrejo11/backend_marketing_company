package at.backend.MarketingCompany.customer.application.port.ouput;

import java.util.List;
import java.util.Optional;

import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerCompanyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerCompanyRepositoryPort {
    CustomerCompany save(CustomerCompany customerCompany);

    Optional<CustomerCompany> findById(CustomerCompanyId id);

    Page<CustomerCompany> findAll(Pageable pageable);

    void delete(CustomerCompanyId id);

    boolean existsById(CustomerCompanyId id);

    List<CustomerCompany> findByIndustry(String industryCode);

    List<CustomerCompany> findByCompanyNameContaining(String name);

    List<CustomerCompany> findByStatus(CompanyStatus status);

    Page<CustomerCompany> findByCompanySize(CompanySize size, Pageable pageable);

    List<CustomerCompany> findHighValueClients();

    Optional<CustomerCompany> findByTaxId(String taxId);

    List<CustomerCompany> findCompaniesWithExpiringContracts();

    List<CustomerCompany> findRecentStartups(int sinceYear);

    Page<CustomerCompany> searchCompanies(String searchTerm, Pageable pageable);

    boolean existsByTaxId(String taxId);
}
