package at.backend.MarketingCompany.crm.quote.application.service;

import at.backend.MarketingCompany.crm.deal.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.application.dto.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.GetQuoteByIdQuery;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteCreateCommand;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteQueryPort;
import at.backend.MarketingCompany.crm.quote.application.input.QuoteServicePort;
import at.backend.MarketingCompany.crm.quote.application.output.QuoteItemRepositoryPort;
import at.backend.MarketingCompany.crm.quote.application.output.QuoteRepositoryPort;
import at.backend.MarketingCompany.crm.quote.domain.exception.QuoteNotFoundException;
import at.backend.MarketingCompany.crm.quote.domain.model.*;
import at.backend.MarketingCompany.crm.quote.domain.service.QuoteDomainService;
import at.backend.MarketingCompany.crm.quote.domain.valueobject.*;
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
public class QuoteApplicationService implements QuoteServicePort, QuoteQueryPort {

  private final QuoteRepositoryPort quoteRepositoryPort;
  private final QuoteItemRepositoryPort quoteItemRepositoryPort;
  private final QuoteDomainService quoteDomainService;
  private final ServicePackageRepository servicePackageRepository;
  private final ExternalModuleValidator externalModuleValidator;

  @Override
  @Transactional(readOnly = true)
  public Page<Quote> handle(Pageable pageable) {
    return quoteRepositoryPort.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Quote handle(GetQuoteByIdQuery query) {
    return quoteRepositoryPort.findById(query.id())
        .orElseThrow(() -> new QuoteNotFoundException(query.id()));
  }

  @Override
  public Quote handle(QuoteCreateCommand command) {
    validateQuote(command);
    externalModuleValidator.validateCustomerExists(command.customerCompanyId());

    Quote quote = Quote.create(command.customerCompanyId(), command.validUntil());
    return quoteRepositoryPort.save(quote);
  }

  @Override
  public Quote handle(AddQuoteItemsCommand command) {
    Quote quote = quoteRepositoryPort.findById(command.quoteId())
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

    quoteItemRepositoryPort.bulkSave(items);
    return quoteRepositoryPort.save(quote);
  }

  @Override
  public Quote handle(QuoteItemId itemId) {
    QuoteItem item = quoteItemRepositoryPort.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("Quote item not found"));

    Quote quote = quoteRepositoryPort.findById(item.getQuoteId())
        .orElseThrow(() -> new QuoteNotFoundException(item.getQuoteId().value()));

    quote.removeItem(itemId);
    quoteItemRepositoryPort.delete(itemId);

    return quoteRepositoryPort.save(quote);

  }

  @Override
  public Quote handle(QuoteId quoteId) {
    Quote quote = quoteRepositoryPort.findById(quoteId)
        .orElseThrow(() -> new QuoteNotFoundException(quoteId));

    quoteRepositoryPort.delete(quote.getId());

    return quote;
  }

  private void validateQuote(QuoteCreateCommand input) {
    quoteDomainService.validateQuoteDates(input.validUntil());
  }
}
