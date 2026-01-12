package at.backend.MarketingCompany.crm.quote.core.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.crm.deal.core.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.core.application.commands.AddQuoteItemsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.CreateQuoteCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.DeleteQuoteCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsAcceptedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsRejectedCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.MarkQuoteAsSentCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.QuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.RemoveQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteDetailsCommand;
import at.backend.MarketingCompany.crm.quote.core.application.commands.UpdateQuoteItemCommand;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteBusinessRuleExcepption;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteNotFoundException;
import at.backend.MarketingCompany.crm.quote.core.domain.model.Quote;
import at.backend.MarketingCompany.crm.quote.core.domain.model.QuoteItem;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.port.input.QuoteCommandInputPort;
import at.backend.MarketingCompany.crm.quote.core.port.output.QuoteItemRepository;
import at.backend.MarketingCompany.crm.quote.core.port.output.QuoteRepository;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.core.ports.output.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteCommandService implements QuoteCommandInputPort {
  private final QuoteRepository quoteRepository;
  private final QuoteItemRepository quoteItemRepository;
  private final ServicePackageRepository servicePackageRepository;
  private final ExternalModuleValidator externalModuleValidator;

  @Override
  @Transactional
  public Quote createQuote(CreateQuoteCommand command) {
    log.info("Creating new quote for customer: {}", command.customerCompanyId());

    externalModuleValidator.validateCustomerExists(command.customerCompanyId());

    Quote quote = Quote.create(
        command.customerCompanyId(),
        command.validUntil(),
        command.notes(),
        command.termsAndConditions());

    if (command.opportunityId() != null) {
      quote.associateWithOpportunity(command.opportunityId());
    }

    Quote savedQuote = quoteRepository.save(quote);
    log.info("Quote created successfully with ID: {}", savedQuote.getId());

    return savedQuote;
  }

  @Override
  @Transactional
  public Quote addQuoteItems(AddQuoteItemsCommand command) {
    log.info("Adding items to quote: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    // Validate that quote can be modified
    if (!quote.canBeModified()) {
      throw new QuoteBusinessRuleExcepption("Quote cannot be modified in its current state");
    }

    // Fetch service packages
    List<ServicePackageId> servicePackageIds = command.items().stream()
        .map(QuoteItemCommand::servicePackageId)
        .toList();

    Map<ServicePackageId, ServicePackage> servicePackages = servicePackageRepository
        .findByIdIn(servicePackageIds);

    // Create quote items
    List<QuoteItem> items = command.items().stream()
        .map(itemCommand -> {
          ServicePackage servicePackage = servicePackages.get(itemCommand.servicePackageId());
          if (servicePackage == null) {
            throw new QuoteBusinessRuleExcepption("Service package not found: " + itemCommand.servicePackageId());
          }

          return QuoteItem.create(
              quote.getId(),
              itemCommand.servicePackageId(),
              Amount.create(servicePackage.getPrice().amount()),
              Discount.random());
        })
        .toList();

    // Add items to quote
    quote.addItems(items);

    // Save items
    quoteItemRepository.bulkSave(items);

    // Save quote
    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Added {} items to quote {}", items.size(), command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote updateQuoteItem(UpdateQuoteItemCommand command) {
    log.info("Updating item {} in quote: {}", command.itemId(), command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.updateItem(
        command.itemId(),
        command.newUnitPrice(),
        command.newDiscount());

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Item {} updated in quote {}", command.itemId(), command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote removeQuoteItem(RemoveQuoteItemCommand command) {
    log.info("Removing item {} from quote: {}", command.itemId(), command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.removeItem(command.itemId());
    quoteItemRepository.delete(command.itemId());

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Item {} removed from quote {}", command.itemId(), command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote markQuoteAsSent(MarkQuoteAsSentCommand command) {
    log.info("Marking quote as SENT: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.markAsSent();

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Quote {} marked as SENT", command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote markQuoteAsAccepted(MarkQuoteAsAcceptedCommand command) {
    log.info("Marking quote as ACCEPTED: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.markAsAccepted();

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Quote {} marked as ACCEPTED", command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote markQuoteAsRejected(MarkQuoteAsRejectedCommand command) {
    log.info("Marking quote as REJECTED: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.markAsRejected(command.rejectionReason());

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Quote {} marked as REJECTED", command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public Quote updateQuoteDetails(UpdateQuoteDetailsCommand command) {
    log.info("Updating quote details: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    if (command.validUntil() != null) {
      quote.updateValidUntil(command.validUntil());
    }

    if (command.notes() != null) {
      quote.updateNotes(command.notes());
    }

    if (command.termsAndConditions() != null) {
      quote.updateTermsAndConditions(command.termsAndConditions());
    }

    if (command.opportunityId() != null) {
      quote.associateWithOpportunity(command.opportunityId());
    }

    Quote updatedQuote = quoteRepository.save(quote);
    log.info("Quote details updated for: {}", command.quoteId());

    return updatedQuote;
  }

  @Override
  @Transactional
  public void deleteQuote(DeleteQuoteCommand command) {
    log.info("Deleting quote: {}", command.quoteId());

    Quote quote = quoteRepository.findById(command.quoteId())
        .orElseThrow(() -> new QuoteNotFoundException("Quote not found: " + command.quoteId()));

    quote.softDelete();
    quoteRepository.delete(quote.getId());

    log.info("Quote {} deleted successfully", command.quoteId());
  }

  @Override
  @Transactional
  public void markExpiredQuotes() {
    log.info("Marking expired quotes");

    List<Quote> expiredQuotes = quoteRepository.findExpiredQuotes();

    for (Quote quote : expiredQuotes) {
      try {
        quote.markAsExpired();
        quoteRepository.save(quote);
        log.debug("Quote {} marked as expired", quote.getId());
      } catch (Exception e) {
        log.error("Error marking quote {} as expired: {}", quote.getId(), e.getMessage());
      }
    }

    log.info("Marked {} quotes as expired", expiredQuotes.size());
  }
}
