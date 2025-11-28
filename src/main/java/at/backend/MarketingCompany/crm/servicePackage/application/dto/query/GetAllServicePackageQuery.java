package at.backend.MarketingCompany.crm.servicePackage.application.dto.query;


import org.springframework.data.domain.Pageable;

public record GetAllServicePackageQuery(Pageable pageable) {}
