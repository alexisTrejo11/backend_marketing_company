package at.backend.MarketingCompany.crm.quote.core.domain.exception;

public class QuoteBusinessRuleExcepption extends QutoteDomainException {
  public QuoteBusinessRuleExcepption(String message) {
    super(message, "QUOTE_BUSINESS_RULE_ERROR");
  }
}
