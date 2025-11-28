package at.backend.MarketingCompany.crm.servicePackage.v2.infrastructure.persistence.repository;

import at.backend.MarketingCompany.crm.servicePackage.v2.infrastructure.persistence.model.ServicePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaServicePackageRepository extends JpaRepository<ServicePackageEntity, String> {
}
