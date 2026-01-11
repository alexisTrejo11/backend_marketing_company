package at.backend.MarketingCompany.crm.quote.core.domain.exception;

public class QuoteValidationException extends QutoteDomainException {
  public QuoteValidationException(String message) {
    super(message, "QUOTE_VALIDATION_ERROR");
  }
}
