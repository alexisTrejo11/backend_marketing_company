package at.backend.MarketingCompany.crm.quote.adapter.input.graphql.dto.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record QuoteItemOutput(
    String id,
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
