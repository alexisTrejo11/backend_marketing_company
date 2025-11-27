package at.backend.MarketingCompany.MarketingCampaing.domain;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.domain.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.domain.HelperHandlers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CampaignAttributionTest {

    private CampaignAttribution attribution;
    private DealId dealId;
    private CampaignId campaignId;
    private TouchTimeline timeline;

    @BeforeEach
    void setUp() {
        dealId = DealId.of(String.valueOf(UUID.randomUUID()));
        campaignId = CampaignId.of(UUID.randomUUID());
        LocalDateTime now = LocalDateTime.now();
        timeline = new TouchTimeline(now, now.plusHours(1), 1, List.of(now));
        attribution = CampaignAttribution.create(dealId, campaignId, AttributionModel.FIRST_TOUCH, timeline);
    }

    @Test
    void shouldCreateCampaignAttributionWithFirstTouchModel() {
        assertNotNull(attribution);
        assertEquals(AttributionModel.FIRST_TOUCH, attribution.getModel());
        assertEquals(BigDecimal.valueOf(100), attribution.getPercentage().value());
    }

    @Test
    void shouldAddTouchToAttribution() {
        LocalDateTime touchTime = LocalDateTime.now().plusMinutes(10);
        attribution = attribution.addTouch(touchTime);

        assertTrue(attribution.getTimeline().getTouches().contains(touchTime));
    }

    @Test
    void shouldRecalculatePercentageForModelForNewModel() {
        attribution = attribution.recalculateForModel(AttributionModel.LAST_TOUCH);

        assertEquals(AttributionModel.LAST_TOUCH, attribution.getModel());
        assertEquals(BigDecimal.valueOf(50), attribution.getPercentage().value());
    }

    @Test
    void shouldThrowExceptionForUnsupportedAttributionModel() {
        AttributionModel unsupportedModel = AttributionModel.POSITION_BASED;

        Exception exception = assertThrows(UnsupportedOperationException.class, () -> CampaignAttribution.calculatePercentageForModel(unsupportedModel));
        assertEquals("Calculation not implemented for " + unsupportedModel, exception.getMessage());
    }
}
