package at.backend.MarketingCompany.marketing.interaction.adapter.input.graphql.dto.input;

import java.time.LocalDateTime;
import java.util.List;

import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;

public record InteractionFilterInput(
        Long campaignId,
        Long customerId,
        Long channelId,
        List<MarketingInteractionType> interactionTypes,
        Boolean isConversion,
        String dateFrom,
        String dateTo,
        String utmSource,
        String utmMedium,
        String utmCampaign,
        String deviceType,
        String countryCode
        ) {

    public InteractionQuery buildInteractionQuery() {

        return new InteractionQuery(
                campaignId,
                customerId,
                channelId,
                interactionTypes,
                isConversion,
                dateFrom != null ? LocalDateTime.parse(dateFrom) : null,
                dateTo != null ? LocalDateTime.parse(dateTo) : null,
                utmSource,
                utmMedium,
                utmCampaign,
                deviceType,
                countryCode
        );
    }
}
