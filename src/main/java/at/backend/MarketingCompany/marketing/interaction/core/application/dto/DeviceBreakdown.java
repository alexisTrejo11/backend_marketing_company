package at.backend.MarketingCompany.marketing.interaction.core.application.dto;

import lombok.Builder;

@Builder
public record DeviceBreakdown(
    Long desktop,
    Long mobile,
    Long tablet,
    Long unknown) {
}
