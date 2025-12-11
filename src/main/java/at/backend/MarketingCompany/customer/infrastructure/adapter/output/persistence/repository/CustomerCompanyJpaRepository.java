package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.repository;


import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCompanyJpaRepository extends JpaRepository<CustomerCompanyEntity, String> {

    Optional<CustomerCompanyEntity> findByTaxId(String taxId);

    List<CustomerCompanyEntity> findByIndustryCode(String industryCode);

    List<CustomerCompanyEntity> findByCompanyNameContainingIgnoreCase(String name);

    List<CustomerCompanyEntity> findByStatus(CompanyStatus status);

    Page<CustomerCompanyEntity> findByCompanySize(CompanySize size, Pageable pageable);

    @Query("SELECT c FROM CustomerCompanyEntity c WHERE " +
            "c.annualRevenueAmount >= :minRevenue AND c.status = 'ACTIVE'")
    List<CustomerCompanyEntity> findHighValueCompanies(@Param("minRevenue") BigDecimal minRevenue);

    @Query("SELECT c FROM CustomerCompanyEntity c WHERE " +
            "c.foundingYear >= :startYear AND c.isStartup = true")
    List<CustomerCompanyEntity> findRecentStartups(@Param("startYear") Integer startYear);

    @Query("SELECT c FROM CustomerCompanyEntity c WHERE " +
            "c.contractDetails.contractEndDate BETWEEN :startDate AND :endDate " +
            "AND c.contractDetails.isActive = true")
    List<CustomerCompanyEntity> findCompaniesWithExpiringContracts(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM CustomerCompanyEntity c WHERE " +
            "c.companySize IN :sizes AND c.status = 'ACTIVE'")
    List<CustomerCompanyEntity> findByCompanySizes(@Param("sizes") List<CompanySize> sizes);

    boolean existsByTaxId(String taxId);

    @Query("SELECT DISTINCT c.industryCode FROM CustomerCompanyEntity c")
    List<String> findAllIndustryCodes();

    @Query("SELECT c FROM CustomerCompanyEntity c WHERE " +
            "(c.companyName LIKE %:searchTerm% OR " +
            "c.legalName LIKE %:searchTerm% OR " +
            "c.primaryContact.email LIKE %:searchTerm%)")
    Page<CustomerCompanyEntity> searchCompanies(@Param("searchTerm") String searchTerm, Pageable pageable);
}
