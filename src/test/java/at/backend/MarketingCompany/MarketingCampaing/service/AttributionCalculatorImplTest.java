package at.backend.MarketingCompany.MarketingCampaing.service;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.api.service.AttributionCalculator;
import at.backend.MarketingCompany.marketing.attribution.api.service.AttributionCalculatorImpl;
import at.backend.MarketingCompany.marketing.attribution.domain.*;
import at.backend.MarketingCompany.marketing.attribution.domain.HelperHandlers.*;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.CampaignAttributionDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AttributionCalculatorImplTest {

    @Test
    void testInitialCalculation() {
        // Arrange
        CampaignAttribution attribution = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.FIRST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AttributionCalculator calculator = new AttributionCalculatorImpl();

        // Act
        CampaignAttribution result = calculator.initialCalculation(attribution);

        // Assert
        assertEquals(BigDecimal.valueOf(100), result.getPercentage().value());
        assertEquals(attribution.getRevenue(), result.getRevenue());
    }

    @Test
    void testRecalculateForModel_LastTouch() {
        // Arrange
        CampaignAttribution attribution = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.LAST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AttributionCalculator calculator = new AttributionCalculatorImpl();

        // Act
        CampaignAttribution result = calculator.recalculateForModel(attribution);

        // Assert
        assertEquals(BigDecimal.valueOf(50), result.getPercentage().value());
        assertEquals(attribution.getRevenue(), result.getRevenue());
    }

    @Test
    void testRecalculateForModel_UnsupportedModel() {
        // Arrange
        CampaignAttribution attribution = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.TIME_DECAY)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        AttributionCalculator calculator = new AttributionCalculatorImpl();

        // Act & Assert
        UnsupportedOperationException exception = assertThrows(
                UnsupportedOperationException.class,
                () -> calculator.recalculateForModel(attribution)
        );
        assertEquals("Calculation not implemented for TIME_DECAY", exception.getMessage());
    }

    @Test
    void testAdjustForNewTouch() {
        // Arrange
        LocalDateTime initialTouch = LocalDateTime.now();
        TouchTimeline timeline = TouchTimeline.builder()
                .firstTouch(initialTouch)
                .lastTouch(initialTouch)
                .touchCount(1)
                .touches(List.of(initialTouch))
                .build();

        CampaignAttribution attribution = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.FIRST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(timeline)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CampaignAttribution updatedAttribution = attribution.addTouch(LocalDateTime.now());

        AttributionCalculator calculator = new AttributionCalculatorImpl();

        // Act
        CampaignAttribution result = calculator.adjustForNewTouch(updatedAttribution);

        // Assert
        BigDecimal expectedPercentage = BigDecimal.valueOf(50).setScale(2);
        assertEquals(expectedPercentage, result.getPercentage().value());
        assertEquals(attribution.getRevenue(), result.getRevenue());
    }

    @Test
    void testCalculateRevenueDistribution() {
        // Arrange
        CampaignAttribution attribution1 = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.FIRST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.valueOf(50)))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CampaignAttribution attribution2 = CampaignAttribution.builder()
                .id(AttributionId.of(UUID.randomUUID()))
                .dealId(DealId.of(String.valueOf(UUID.randomUUID())))
                .campaignId(CampaignId.of(UUID.randomUUID()))
                .model(AttributionModel.LAST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.valueOf(50)))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(500)))
                .timeline(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<CampaignAttribution> attributions = List.of(attribution1, attribution2);
        AttributionCalculator calculator = new AttributionCalculatorImpl();

        // Act
        CampaignAttributionDTO result = calculator.calculateRevenueDistribution(attributions);

        // Assert
        assertEquals(BigDecimal.valueOf(1500), result.getAttributedRevenue());
        assertEquals(2, result.getDistribution().size());
    }
}