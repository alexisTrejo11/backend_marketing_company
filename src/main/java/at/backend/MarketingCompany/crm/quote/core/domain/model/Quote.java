package at.backend.MarketingCompany.crm.quote.core.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.Amount;
import at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject.OpportunityId;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteBusinessRuleExcepption;
import at.backend.MarketingCompany.crm.quote.core.domain.exception.QuoteValidationException;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.Discount;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteItemId;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteReconstructParams;
import at.backend.MarketingCompany.crm.quote.core.domain.valueobject.QuoteStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Quote extends BaseDomainEntity<QuoteId> {
  private CustomerCompanyId customerCompanyId;
  private OpportunityId opportunityId;
  private LocalDate validUntil;
  private QuoteStatus status;
  private final List<QuoteItem> items;
  private String notes;
  private String termsAndConditions;

  private Quote() {
    this.items = new ArrayList<>();
    this.status = QuoteStatus.DRAFT;
  }

  public static Quote reconstruct(QuoteReconstructParams params) {
    log.debug("Reconstructing quote with ID: {}", params.id());

    Quote quote = new Quote();
    quote.id = params.id();
    quote.customerCompanyId = params.customerCompanyId();
    quote.opportunityId = params.opportunityId();
    quote.validUntil = params.validUntil();
    quote.items.addAll(params.items() != null ? params.items() : new ArrayList<>());
    quote.status = params.status() != null ? params.status() : QuoteStatus.DRAFT;
    quote.notes = params.notes();
    quote.termsAndConditions = params.termsAndConditions();
    quote.createdAt = params.createdAt();
    quote.updatedAt = params.updatedAt();
    quote.deletedAt = params.deletedAt();
    quote.version = params.version();

    quote.validateState();

    log.debug("Quote reconstructed successfully: {}", quote.id);
    return quote;
  }

  public static Quote create(CustomerCompanyId customerCompanyId, LocalDate validUntil,
      String notes, String termsAndConditions) {
    log.info("Creating new quote for customer: {}", customerCompanyId);

    validateCreationParams(customerCompanyId, validUntil);

    Quote quote = new Quote();
    quote.id = QuoteId.generate();
    quote.customerCompanyId = customerCompanyId;
    quote.validUntil = validUntil;
    quote.status = QuoteStatus.DRAFT;
    quote.notes = notes;
    quote.termsAndConditions = termsAndConditions;
    quote.createdAt = java.time.LocalDateTime.now();
    quote.updatedAt = java.time.LocalDateTime.now();
    quote.version = 1;

    quote.validateState();

    log.info("Quote created successfully with ID: {}", quote.id);
    return quote;
  }

  public void addItem(QuoteItem item) {
    validateCanModify();
    validateItem(item);

    this.items.add(item);
    recalculateTotals();
    updateTimestamp();

    log.debug("Item added to quote {}: {}", id, item.getId());
  }

  public void addItems(List<QuoteItem> items) {
    validateCanModify();

    if (items == null || items.isEmpty()) {
      throw new QuoteValidationException("Items cannot be null or empty");
    }

    for (QuoteItem item : items) {
      validateItem(item);
      this.items.add(item);
    }

    recalculateTotals();
    updateTimestamp();

    log.debug("{} items added to quote {}", items.size(), id);
  }

  public void removeItem(QuoteItemId itemId) {
    validateCanModify();

    boolean removed = this.items.removeIf(item -> item != null && item.getId().equals(itemId));

    if (removed) {
      recalculateTotals();
      updateTimestamp();
      log.debug("Item removed from quote {}: {}", id, itemId);
    }
  }

  public void updateItem(QuoteItemId itemId, Amount newUnitPrice, Discount newDiscount) {
    validateCanModify();

    QuoteItem item = findItemById(itemId);
    if (item != null) {
      item.updateUnitPrice(newUnitPrice);
      item.updateDiscount(newDiscount);
      recalculateTotals();
      updateTimestamp();
      log.debug("Item updated in quote {}: {}", id, itemId);
    }
  }

  public void markAsSent() {
    validateCanSend();
    validate(); // Ensure quote is valid before sending

    this.status = QuoteStatus.SENT;
    updateTimestamp();

    log.info("Quote {} marked as SENT", id);
  }

  public void markAsAccepted() {
    if (this.status != QuoteStatus.SENT) {
      throw new QuoteBusinessRuleExcepption("Only SENT quotes can be accepted");
    }

    this.status = QuoteStatus.ACCEPTED;
    updateTimestamp();

    log.info("Quote {} marked as ACCEPTED", id);
  }

  public void markAsRejected(String rejectionReason) {
    if (this.status != QuoteStatus.SENT) {
      throw new QuoteBusinessRuleExcepption("Only SENT quotes can be rejected");
    }

    this.status = QuoteStatus.REJECTED;
    this.notes = (this.notes != null ? this.notes + "\n" : "") +
        "Rejected: " + (rejectionReason != null ? rejectionReason : "No reason provided");
    updateTimestamp();

    log.info("Quote {} marked as REJECTED with reason: {}", id, rejectionReason);
  }

  public void markAsExpired() {
    if (isExpired()) {
      this.status = QuoteStatus.EXPIRED;
      updateTimestamp();
      log.info("Quote {} marked as EXPIRED", id);
    }
  }

  public void updateValidUntil(LocalDate newValidUntil) {
    validateCanModify();

    if (newValidUntil == null) {
      throw new QuoteValidationException("Valid until date cannot be null");
    }

    if (newValidUntil.isBefore(LocalDate.now())) {
      throw new QuoteValidationException("Valid until date cannot be in the past");
    }

    this.validUntil = newValidUntil;
    updateTimestamp();

    log.debug("Valid until date updated for quote {}: {}", id, newValidUntil);
  }

  public void updateNotes(String notes) {
    this.notes = notes;
    updateTimestamp();
    log.debug("Notes updated for quote {}", id);
  }

  public void updateTermsAndConditions(String termsAndConditions) {
    this.termsAndConditions = termsAndConditions;
    updateTimestamp();
    log.debug("Terms and conditions updated for quote {}", id);
  }

  public void associateWithOpportunity(OpportunityId opportunityId) {
    this.opportunityId = opportunityId;
    updateTimestamp();
    log.debug("Quote {} associated with opportunity {}", id, opportunityId);
  }

  public void validate() {
    validateState();

    if (validUntil == null) {
      throw new QuoteValidationException("Valid until date cannot be null");
    }

    if (isExpired()) {
      throw new QuoteBusinessRuleExcepption("Quote has expired");
    }

    if (items.isEmpty()) {
      throw new QuoteValidationException("Quote must have at least one item");
    }

    for (QuoteItem item : items) {
      if (item == null || item.getUnitPrice() == null) {
        throw new QuoteValidationException("Quote item is incomplete");
      }
    }

    log.debug("Quote {} validated successfully", id);
  }

  public boolean isExpired() {
    return validUntil != null && validUntil.isBefore(LocalDate.now());
  }

  public boolean canBeSent() {
    return status == QuoteStatus.DRAFT && !isExpired() && !items.isEmpty();
  }

  public boolean canBeModified() {
    return status == QuoteStatus.DRAFT && !isExpired();
  }

  public boolean isAccepted() {
    return status == QuoteStatus.ACCEPTED;
  }

  public boolean isRejected() {
    return status == QuoteStatus.REJECTED;
  }

  public boolean isSent() {
    return status == QuoteStatus.SENT;
  }

  public boolean isDraft() {
    return status == QuoteStatus.DRAFT;
  }

  public Amount getSubTotal() {
    if (items.isEmpty()) {
      return Amount.ZERO;
    }
    return Amount.calculateSubTotal(items);
  }

  public Discount getDiscount() {
    if (items == null || items.isEmpty()) {
      return Discount.ZERO;
    }
    return Discount.calculate(items);
  }

  public Amount getTotalAmount() {
    return Amount.calculateTotal(items);
  }

  private void validateCanModify() {
    if (!canBeModified()) {
      throw new QuoteBusinessRuleExcepption("Quote cannot be modified in its current state");
    }
  }

  private void validateCanSend() {
    if (!canBeSent()) {
      throw new QuoteBusinessRuleExcepption("Quote cannot be sent in its current state");
    }
  }

  private void validateState() {
    if (customerCompanyId == null) {
      throw new QuoteValidationException("Customer company ID is required");
    }

    if (validUntil == null) {
      throw new QuoteValidationException("Valid until date is required");
    }

    if (status == null) {
      throw new QuoteValidationException("Quote status is required");
    }
  }

  private static void validateCreationParams(CustomerCompanyId customerCompanyId, LocalDate validUntil) {
    if (customerCompanyId == null) {
      throw new QuoteValidationException("Customer company ID cannot be null");
    }

    if (validUntil == null) {
      throw new QuoteValidationException("Valid until date cannot be null");
    }

    if (validUntil.isBefore(LocalDate.now())) {
      throw new QuoteValidationException("Valid until date cannot be in the past");
    }

    if (validUntil.isAfter(LocalDate.now().plusYears(2))) {
      throw new QuoteValidationException("Valid until date must be within 2 years");
    }
  }

  private void validateItem(QuoteItem item) {
    if (item == null) {
      throw new QuoteValidationException("Item cannot be null");
    }

    if (item.getId() == null || !item.getId().getValue().equals(this.id.getValue())) {
      throw new QuoteValidationException("Item must be associated with this quote");
    }
  }

  private QuoteItem findItemById(QuoteItemId itemId) {
    return items.stream()
        .filter(item -> item != null && item.getId().equals(itemId))
        .findFirst()
        .orElseThrow(() -> new QuoteValidationException("Item not found: " + itemId));
  }

  private void recalculateTotals() {
    // Total recalculation is handled by getters
    updateTimestamp();
  }

  private void updateTimestamp() {
    this.updatedAt = java.time.LocalDateTime.now();
    this.version++;
  }

  @Override
  public void softDelete() {
    if (isSent() || isAccepted()) {
      throw new QuoteBusinessRuleExcepption("Cannot delete a sent or accepted quote");
    }
    super.softDelete();
    log.info("Quote {} soft deleted", id);
  }
}
