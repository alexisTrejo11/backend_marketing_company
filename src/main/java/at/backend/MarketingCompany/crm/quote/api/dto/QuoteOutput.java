package at.backend.MarketingCompany.crm.quote.api.dto;

import at.backend.MarketingCompany.crm.quote.domain.valueobject.QuoteStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record QuoteOutput(
    Long id,
    String customerId,
    String opportunityId,
    LocalDate validUntil,
    BigDecimal subTotal,
    BigDecimal discount,
    BigDecimal totalAmount,
    QuoteStatus status,
    List<QuoteItemOutput> items,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}