package at.backend.MarketingCompany.marketing.ab_test.core.domain.exception;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import at.backend.MarketingCompany.shared.exception.NotFoundException;

public class AbTestNotFoundException extends NotFoundException {
	public AbTestNotFoundException(AbTestId testId) {
		super("AB Test", testId.asString());
	}
}
