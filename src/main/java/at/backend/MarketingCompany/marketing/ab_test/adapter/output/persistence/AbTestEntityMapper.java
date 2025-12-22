package at.backend.MarketingCompany.marketing.ab_test.adapter.output.persistence;

import at.backend.MarketingCompany.marketing.campaign.adapter.output.persistence.entity.AbTestEntity;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.AbTest;
import org.springframework.stereotype.Component;

@Component
public class AbTestEntityMapper {

	public AbTestEntity toEntity(AbTest domain) {
		return null;
	}

	public AbTest toDomain(AbTestEntity entity) {
		return null;
	}
}
