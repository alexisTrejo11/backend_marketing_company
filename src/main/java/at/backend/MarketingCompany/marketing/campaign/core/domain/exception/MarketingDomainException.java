package at.backend.MarketingCompany.marketing.campaign.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class MarketingDomainException extends DomainException {
	private static final String ERROR_CODE = "MARKETING_DOMAIN_ERROR";
	public MarketingDomainException(String message) {
		super(message, ERROR_CODE);
	}

	public MarketingDomainException(String message, Map<String, Object> details) {
		super(message, ERROR_CODE, details);
	}

	public MarketingDomainException(String message, Throwable cause) {
		super(message, ERROR_CODE, cause);
	}
}