package at.backend.MarketingCompany.marketing.ab_test.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class AbTestException extends DomainException {
	public AbTestException(String message, String errorCode) {
		super(message, errorCode);
	}

	public AbTestException(String message) {
		super(message, "MKT_AB_TEST_ERROR");
	}

	public AbTestException(String message, String errorCode, Map<String, Object> details) {
		super(message, errorCode, details);
	}

	public AbTestException(String message, String errorCode, Throwable cause) {
		super(message, errorCode, cause);
	}
}
