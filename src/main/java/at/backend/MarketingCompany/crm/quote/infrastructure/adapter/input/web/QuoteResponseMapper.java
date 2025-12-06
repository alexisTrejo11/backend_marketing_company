package at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.common.PageResponse;
import at.backend.MarketingCompany.crm.quote.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto.QuoteItemOutput;
import at.backend.MarketingCompany.crm.quote.infrastructure.adapter.input.web.dto.QuoteOutput;

@Component
public class QuoteResponseMapper {

  public QuoteOutput toResponse(Quote quote) {
    if (quote == null) {
      return null;
    }

    return QuoteOutput.builder()
        .id(quote.getId().toString())
        .customerId(quote.getCustomerId().toString())
        .opportunityId(
            quote.getOpportunityId() != null ? quote.getOpportunityId().toString() : null)
        .validUntil(quote.getValidUntil())
        .subTotal(quote.getSubTotal().value())
        .discount(quote.getDiscount().percentage())
        .totalAmount(quote.getTotalAmount().value())
        .status(quote.getStatus())
        .items(
            quote.getItems().stream()
                .map(this::toItemResponse)
                .toList())
        .createdAt(quote.getCreatedAt())
        .updatedAt(quote.getUpdatedAt())
        .deletedAt(quote.getDeletedAt())
        .version(quote.getVersion())
        .build();
  }

  public PageResponse<QuoteOutput> toResponsePage(Page<Quote> quotePage) {
    if (quotePage == null) {
      return PageResponse.empty();
    }
    return PageResponse.of(quotePage.map(this::toResponse));
  }

  public QuoteItemOutput toItemResponse(QuoteItem item) {
    if (item == null) {
      return null;
    }
    return QuoteItemOutput.builder()
        .id(item.getId().toString())
        .quoteId(item.getQuoteId().toString())
        .unitPrice(item.getUnitPrice().value())
        .createdAt(item.getCreatedAt())
        .updatedAt(item.getUpdatedAt())
        .deletedAt(item.getDeletedAt())
        .version(item.getVersion())
        .build();

  }
}
