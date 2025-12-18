package at.backend.MarketingCompany.account.user.core.domain.entity.valueobject;

import at.backend.MarketingCompany.shared.domain.NumericId;


public class UserId extends NumericId {
	public UserId(Long value) {
		super(value);
	}

	public static UserId of(String id) {
		return NumericId.fromString(id, UserId::new);
	}

	// Database will generate the ID
	public static UserId generate() {
		return new UserId(0L);
	}
}