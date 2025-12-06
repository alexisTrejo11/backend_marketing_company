package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteStatus;
import lombok.Builder;

@Builder
public record QuoteOutput(
    String id,
    String customerId,
    String opportunityId,
    LocalDate validUntil,
    BigDecimal subTotal,
    BigDecimal discount,
    BigDecimal totalAmount,
    QuoteStatus status,
    List<QuoteItemOutput> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deletedAt,
    Integer version) {
}
