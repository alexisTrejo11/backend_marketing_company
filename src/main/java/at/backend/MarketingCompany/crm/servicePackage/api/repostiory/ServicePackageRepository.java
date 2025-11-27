package at.backend.MarketingCompany.crm.servicePackage.api.repostiory;

import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicePackageRepository extends JpaRepository<ServicePackageEntity, Long> {

}
