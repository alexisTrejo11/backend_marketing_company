package at.backend.MarketingCompany.crm.servicePackage.adapter.output.repository;

import at.backend.MarketingCompany.crm.servicePackage.adapter.output.model.ServicePackageEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaServicePackageRepository extends JpaRepository<ServicePackageEntity, String> {

  List<ServicePackageEntity> findByIdIn(List<String> ids);
}
