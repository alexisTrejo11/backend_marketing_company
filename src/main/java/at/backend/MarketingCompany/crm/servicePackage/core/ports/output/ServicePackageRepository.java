package at.backend.MarketingCompany.crm.servicePackage.core.ports.output;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ServicePackageRepository {
  ServicePackage save(ServicePackage servicePackage);

  Optional<ServicePackage> findById(ServicePackageId id);

  List<ServicePackage> findByIdIn(List<ServicePackageId> ids);

  Page<ServicePackage> findAll(Pageable pageable);

  void delete(ServicePackage servicePackage);

  boolean existsById(ServicePackageId id);
}
