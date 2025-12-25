package at.backend.MarketingCompany.marketing.ab_test.adapter.input.graphql.dto;

import at.backend.MarketingCompany.marketing.ab_test.core.application.command.UpdateAbTestCommand;
import at.backend.MarketingCompany.marketing.ab_test.core.domain.valueobject.AbTestId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateAbTestInput(
		@NotNull @Positive Long testId,
		String hypothesis,
		BigDecimal confidenceLevel,
		Integer requiredSampleSize,
		LocalDateTime endDate
) {


	public UpdateAbTestCommand toCommand() {
		return new UpdateAbTestCommand(
				new AbTestId(this.testId),
				this.hypothesis,
				this.confidenceLevel,
				this.requiredSampleSize,
				this.endDate
		);
	}
}
