package at.backend.MarketingCompany.customer.core.port.ouput;

import java.util.List;
import java.util.Optional;

import at.backend.MarketingCompany.customer.core.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
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

	List<CustomerCompany> findRecentStartups(int sinceYear);

	Page<CustomerCompany> searchCompanies(String searchTerm, Pageable pageable);
}
