package at.backend.MarketingCompany.crm.quote.application.dto;

import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

import java.math.BigDecimal;

public record QuoteItemCommand(
    ServicePackageId servicePackageId,
    BigDecimal discountPercentage
) {}