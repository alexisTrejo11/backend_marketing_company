package at.backend.MarketingCompany.crm.quote.core.domain.exception;

public class QuoteValidationException extends RuntimeException {
  public QuoteValidationException(String message) {
    super(message);
  }
}
