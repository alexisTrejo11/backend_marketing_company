package at.backend.MarketingCompany.crm.quote.core.application.service;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.application.dto.*;
import at.backend.MarketingCompany.crm.quote.core.application.input.*;
import at.backend.MarketingCompany.crm.quote.core.application.output.QuoteItemRepository;
import at.backend.MarketingCompany.crm.quote.core.application.output.QuoteRepository;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteNotFoundException;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.service.QuoteDomainService;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.*;
import at.backend.MarketingCompany.crm.servicePackage.domain.repository.ServicePackageRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuoteApplicationService implements QuoteCommandService, QuoteQueryService {
  private final QuoteRepository quoteRepository;
  private final QuoteItemRepository quoteItemRepository;
  private final QuoteDomainService quoteDomainService;
  private final ServicePackageRepository servicePackageRepository;
  private final ExternalModuleValidator externalModuleValidator;

  @Override
  @Transactional(readOnly = true)
  public Page<Quote> getAllQuotes(Pageable pageable) {
    return quoteRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Quote getQuoteById(GetQuoteByIdQuery query) {
    return quoteRepository.findById(query.id())
        .orElseThrow(() -> new QuoteNotFoundException(query.id()));
  }

  @Override
  public Quote createQuote(QuoteCreateCommand command) {
    validateQuote(command);
    externalModuleValidator.validateCustomerExists(command.customerCompanyId());

    Quote quote = Quote.create(command.customerCompanyId(), command.validUntil());
    return quoteRepository.save(quote);
  }

  @Override
  public Quote addQuoteItems(AddQuoteItemsCommand command) {
    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException(command.quoteId()));

    var servicePackages = servicePackageRepository.findByIdIn(command.getServicePackageIds());

    // TODO: Optimize lookup
    List<QuoteItem> items = new ArrayList<>();
    for (var servicePackage : servicePackages) {
      Amount unitPrice = new Amount(servicePackage.getPrice().amount());

      var itemCommand = command.input().stream()
          .filter(item -> item.servicePackageId() != null && item.servicePackageId().equals(servicePackage.getId()))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException(
              "Quote item command not found for service package: " + servicePackage.getId().value()));

      QuoteItem item = quoteDomainService.createQuoteItem(
          quote,
          servicePackage.getId(),
          unitPrice,
          new Discount(itemCommand.discountPercentage()));
      items.add(item);
    }

    quote.addItems(items);

    quoteItemRepository.bulkSave(items);
    return quoteRepository.save(quote);
  }

  @Override
  public Quote deleteQuoteItem(QuoteItemId itemId) {
    QuoteItem item = quoteItemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("Quote item not found"));

    Quote quote = quoteRepository.findById(item.getQuoteId())
        .orElseThrow(() -> new QuoteNotFoundException(item.getQuoteId().value()));

    quote.removeItem(itemId);
    quoteItemRepository.delete(itemId);

    return quoteRepository.save(quote);

  }

  @Override
  public Quote deleteQuote(QuoteId quoteId) {
    Quote quote = quoteRepository.findById(quoteId)
        .orElseThrow(() -> new QuoteNotFoundException(quoteId));

    quoteRepository.delete(quote.getId());

    return quote;
  }

  private void validateQuote(QuoteCreateCommand input) {
    quoteDomainService.validateQuoteDates(input.validUntil());
  }
}
