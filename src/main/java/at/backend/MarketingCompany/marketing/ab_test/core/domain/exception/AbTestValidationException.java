package at.backend.MarketingCompany.marketing.ab_test.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

public class AbTestValidationException extends DomainException {

	public AbTestValidationException(String message) {
		super(message, "MKT_AB_TEST_VALIDATION_ERROR");
	}

}
