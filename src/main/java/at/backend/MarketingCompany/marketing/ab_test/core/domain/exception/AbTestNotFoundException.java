package at.backend.MarketingCompany.marketing.ab_test.core.domain.exception;

import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class AbTestNotFoundException extends NotFoundException {
	public AbTestNotFoundException(String resource, String identifier) {
		super(resource, identifier);
	}
}
