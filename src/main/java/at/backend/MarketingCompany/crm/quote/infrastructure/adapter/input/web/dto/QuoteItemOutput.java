package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record QuoteItemOutput(
    String id,
    String quoteId,
    String servicePackageId,
    BigDecimal unitPrice,
    BigDecimal total,
    BigDecimal discountPercentage,
    BigDecimal discount,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
