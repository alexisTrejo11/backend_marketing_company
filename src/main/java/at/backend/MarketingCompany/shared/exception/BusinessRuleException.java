package at.backend.MarketingCompany.shared.exception;

import java.util.Map;

public class BusinessRuleException extends DomainException {
    public BusinessRuleException(String message, String ruleCode) {
        super(message, "BUSINESS_RULE_VIOLATION", 
              Map.of("ruleCode", ruleCode));
    }
}
