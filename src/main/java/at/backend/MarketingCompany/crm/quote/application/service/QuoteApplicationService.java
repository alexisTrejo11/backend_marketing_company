package at.backend.MarketingCompany.crm.quote.application.service;

import at.backend.MarketingCompany.crm.deal.application.ExternalModuleValidator;
import at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteCommand;
import at.backend.MarketingCompany.crm.quote.application.dto.QuoteItemCommand;
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
    public Page<Quote> getAllQuotes(Pageable pageable) {
        return quoteRepositoryPort.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Quote getQuoteById(QuoteId id) {
        Quote quote = quoteRepositoryPort.findById(id)
            .orElseThrow(() -> new QuoteNotFoundException(id));
    }
    
    @Override
    public Quote createQuote(QuoteCommand command) {
        validateQuote(command);
        externalModuleValidator.validateCustomerExists(command.customerId());

        Quote quote = Quote.create(command.customerId(), command.validUntil());
        return quoteRepositoryPort.save(quote);
    }

    @Override
    public Quote addQuoteItem(QuoteId quoteId, QuoteItemCommand command) {
        Quote quote = quoteRepositoryPort.findById(quoteId)
            .orElseThrow(() -> new QuoteNotFoundException(quoteId));
        
        // Get service package
        var servicePackage = servicePackageRepository.findById(command.servicePackageId())
            .orElseThrow(() -> new IllegalArgumentException("Service package not found"));
        
        Amount unitPrice = new Amount(servicePackage.getPrice().amount());
        Discount discountPercentage = new Discount(input.discountPercentage());
        
        QuoteItem item = quoteDomainService.createQuoteItem(
            quote,
            command.servicePackageId(),
            unitPrice,
            discountPercentage
        );
        
        quote.addItem(item);

        quoteItemRepositoryPort.save(item);
        return quoteRepositoryPort.save(quote);
    }
    
    @Override
    public Quote removeQuoteItem(QuoteItemId itemId) {
        QuoteItem item = quoteItemRepositoryPort.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Quote item not found"));
        
        Quote quote = quoteRepositoryPort.findById(item.getQuoteId())
            .orElseThrow(() -> new QuoteNotFoundException(item.getQuoteId().value()));
        
        quote.removeItem(itemId);
        quoteItemRepositoryPort.delete(itemId);
        
        return quoteRepositoryPort.save(quote);
        
    }
    
    @Override
    public Quote deleteQuote(QuoteId quoteId) {
        Quote quote = quoteRepositoryPort.findById(quoteId)
            .orElseThrow(() -> new QuoteNotFoundException(quoteId));
        
        quoteRepositoryPort.delete(quote.getId());

        return quote;
    }
    
    private void validateQuote(QuoteCommand input) {
        quoteDomainService.validateQuoteDates(input.validUntil());
    }
}