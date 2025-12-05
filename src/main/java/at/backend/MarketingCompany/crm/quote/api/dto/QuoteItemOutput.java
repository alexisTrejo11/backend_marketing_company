package at.backend.MarketingCompany.crm.quote.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QuoteItemOutput(
    String id,
    String quoteId,
    String servicePackageId,
    BigDecimal unitPrice,
    BigDecimal total,
    BigDecimal discountPercentage,
    BigDecimal discount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}