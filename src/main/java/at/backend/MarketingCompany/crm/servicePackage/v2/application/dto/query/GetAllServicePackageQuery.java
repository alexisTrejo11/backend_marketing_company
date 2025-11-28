package at.backend.MarketingCompany.crm.servicePackage.v2.application.dto.query;


import org.springframework.data.domain.Pageable;

public record GetAllServicePackageQuery(Pageable pageable) {}
