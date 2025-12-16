package at.backend.MarketingCompany.crm.quote.core.application.dto;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

import java.math.BigDecimal;

public record QuoteItemCreateCommand(
    ServicePackageId servicePackageId,
    BigDecimal discountPercentage) {
}
