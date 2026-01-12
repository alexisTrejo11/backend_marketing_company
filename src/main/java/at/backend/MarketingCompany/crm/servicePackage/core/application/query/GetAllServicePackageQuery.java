package at.backend.MarketingCompany.crm.servicePackage.core.application.query;

import org.springframework.data.domain.Pageable;

public record GetAllServicePackageQuery(Pageable pageable) {
}
