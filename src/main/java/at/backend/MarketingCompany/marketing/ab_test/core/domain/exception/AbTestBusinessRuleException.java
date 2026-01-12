package at.backend.MarketingCompany.marketing.ab_test.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class AbTestBusinessRuleException extends DomainException {

	public AbTestBusinessRuleException(String message) {
		super(message, "MRK_AB_TEST_BUSINESS_RULE_VIOLATION");
	}

}
