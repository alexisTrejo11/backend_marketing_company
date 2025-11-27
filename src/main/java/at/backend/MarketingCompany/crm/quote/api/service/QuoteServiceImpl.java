package at.backend.MarketingCompany.crm.quote.api.service;

import at.backend.MarketingCompany.crm.opportunity.api.repository.OpportunityRepository;
import at.backend.MarketingCompany.crm.opportunity.domain.Opportunity;
import at.backend.MarketingCompany.crm.quote.infrastructure.DTOs.QuoteInput;
import at.backend.MarketingCompany.crm.quote.infrastructure.DTOs.QuoteItemInput;
import at.backend.MarketingCompany.crm.quote.infrastructure.autoMappers.QuoteMappers;
import at.backend.MarketingCompany.common.exceptions.BusinessLogicException;
import at.backend.MarketingCompany.crm.quote.api.repository.QuoteItemRepository;
import at.backend.MarketingCompany.crm.quote.api.repository.QuoteRepository;
import at.backend.MarketingCompany.crm.quote.domain.Quote;
import at.backend.MarketingCompany.crm.quote.domain.QuoteItem;
import at.backend.MarketingCompany.crm.servicePackage.api.repostiory.ServicePackageRepository;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerRepository;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    public final QuoteRepository quoteRepository;
    public final QuoteItemRepository quoteItemRepository;
    public final CustomerRepository customerRepository;
    public final OpportunityRepository opportunityRepository;
    public final ServicePackageRepository servicePackageRepository;
    public final QuoteMappers quoteMappers;

    @Override
    public Page<Quote> getAll(Pageable pageable) {
        return quoteRepository.findAll(pageable);
    }

    @Override
    public Quote getById(Long id) {
        return getQuote(id);
    }

    @Override
    @Transactional
    public Quote create(QuoteInput input) {
        Quote newQuote = quoteMappers.inputToEntity(input);

        newQuote.setCustomerModel(getCustomer(input.customerId()));
        newQuote.setOpportunity(getOpportunity(input.opportunityId()));

        List<QuoteItem> items = generateItems(newQuote, input.items());
        newQuote.setItems(items);

        calculateNumbers(newQuote);

        return quoteRepository.saveAndFlush(newQuote);
    }

    @Override
    @Transactional
    public Quote update(Long id, QuoteInput input) {
       return null;
    }

    @Override
    @Transactional
    public Quote addItem(Long id, QuoteItemInput input) {
         Quote existingQuote = quoteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quote not found"));

        List<QuoteItem> items = generateItems(existingQuote, Collections.singletonList(input));
        existingQuote.getItems().addAll(items);

        calculateNumbers(existingQuote);

        return quoteRepository.saveAndFlush(existingQuote);
    }

    @Override
    @Transactional
    public Quote deleteItem(Long itemId) {
        QuoteItem item = quoteItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Quote Item not found"));

        Quote quote = item.getQuote();

        quote.getItems().remove(item);

        calculateNumbers(quote);

        quoteRepository.saveAndFlush(quote);

        return quote;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Quote quote = getQuote(id);

        quoteRepository.delete(quote);
    }

    @Override
    public void validate(QuoteInput input) {
        LocalDate validUntil = input.validUntil();
        LocalDate validUntilLimit = LocalDate.now().plusMonths(10);

        if (validUntil.isAfter(validUntilLimit)) {
            throw new BusinessLogicException("The 'valid until' date exceeds the allowed limit. The maximum duration is 10 months from today.");
        }
    }

    private List<QuoteItem> generateItems(Quote createdQuote, List<QuoteItemInput> inputs) {
        List<Long> servicePackageIds = inputs.stream()
                .map(QuoteItemInput::servicePackageId)
                .toList();
        Map<String, ServicePackageEntity> servicePackages = servicePackageRepository.findAllById(servicePackageIds)
                .stream()
                .collect(Collectors.toMap(ServicePackageEntity::getId, servicePackage -> servicePackage));

        return inputs.stream()
                .map(input -> {
                    QuoteItem item =  new QuoteItem(input.discountPercentage(), createdQuote);

                    ServicePackageEntity servicePackageEntity = servicePackages.get(input.servicePackageId());
                    if (servicePackageEntity == null) {
                        throw new EntityNotFoundException("ServicePackageEntity not found for ID: " + input.servicePackageId());
                    }
                    item.setServicePackageEntity(servicePackageEntity);

                    calculateItemNumbers(item);

                    return item;
                }).toList();
    }


    private void calculateNumbers(Quote quote) {
        List<QuoteItem> items = quote.getItems();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        if (!items.isEmpty()) {
            for (var item : items) {
                calculateItemNumbers(item);

                subtotal = subtotal.add(item.getUnitPrice());
                discount = discount.add(item.getDiscount());
                total = total.add(item.getTotal());
            }
        }

        quote.setSubTotal(subtotal);
        quote.setDiscount(discount);
        quote.setTotalAmount(total);
    }

    private void calculateItemNumbers(QuoteItem item) {
        BigDecimal unitPrice = item.getServicePackageEntity().getPrice();
        item.setUnitPrice(unitPrice);

        BigDecimal discountPercentage = item.getDiscountPercentage();
        BigDecimal discount = unitPrice.multiply(discountPercentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal total = unitPrice.subtract(discount);

        item.setDiscount(discount);
        item.setTotal(total);
    }

    private Quote getQuote(Long id) {
        return quoteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quote not found"));
    }

    private CustomerModel getCustomer(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("CustomerModel not found"));
    }

    private Opportunity getOpportunity(Long opportunityId) {
        return opportunityRepository.findById(opportunityId).orElseThrow(() -> new EntityNotFoundException("Opportunity not found"));
    }

}
