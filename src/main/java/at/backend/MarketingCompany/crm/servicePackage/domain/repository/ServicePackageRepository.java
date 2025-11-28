package at.backend.MarketingCompany.crm.servicePackage.domain.repository;

import at.backend.MarketingCompany.crm.servicePackage.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServicePackageRepository {
    ServicePackage save(ServicePackage servicePackage);
    Optional<ServicePackage> findById(ServicePackageId id);
    Page<ServicePackage> findAll(Pageable pageable);
    void delete(ServicePackage servicePackage);
    boolean existsById(ServicePackageId id);
}
