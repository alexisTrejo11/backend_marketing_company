package at.backend.MarketingCompany.MarketingCampaing.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.MarketingInteractionType;
import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.marketing.campaign.domain.MarketingCampaign;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import at.backend.MarketingCompany.marketing.interaction.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CampaignInteractionTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private MarketingCampaign mockCampaign;
    private CustomerModel mockCustomer;
    private InteractionSource interactionSource;
    private DeviceInfo deviceInfo;
    private GeoLocation geoLocation;
    private Map<String, String> properties;

    @BeforeEach
    void setUp() {
        mockCampaign = mock(MarketingCampaign.class);
        when(mockCampaign.isActive()).thenReturn(true);
        when(mockCampaign.calculateROI()).thenReturn(0.2);

        mockCustomer = mock(CustomerModel.class);
        when(mockCustomer.isBlocked()).thenReturn(false);

        interactionSource = InteractionSource.builder()
                .channel("Email")
                .medium("Newsletter")
                .campaignName("Summer Sale")
                .build();

        deviceInfo = DeviceInfo.builder()
                .deviceType("Mobile")
                .ipAddress("192.168.1.1")
                .build();

        geoLocation = GeoLocation.builder()
                .country("US")
                .region("CA")
                .city("Los Angeles")
                .build();

        properties = new HashMap<>();
        properties.put("utm_source", "google");
    }

    @Nested
    class ConstructorTests {

        @Test
        void constructor_WithInactiveCampaign_ThrowsException() {
            when(mockCampaign.isActive()).thenReturn(false);

            assertThatThrownBy(CampaignInteractionTest.this::buildCampaignInteraction)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("inactive campaign");
        }

        @Test
        void constructor_WithBlockedCustomer_ThrowsException() {
            when(mockCustomer.isBlocked()).thenReturn(true);

            assertThatThrownBy(CampaignInteractionTest.this::buildCampaignInteraction)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("blocked customer");
        }

        @Test
        void constructor_SetsCreatedAtAndUpdatedAt() {
            CampaignInteraction interaction = buildCampaignInteraction();
            assertThat(interaction.getCreatedAt()).isEqualTo(NOW);
            assertThat(interaction.getUpdatedAt()).isEqualTo(NOW);
        }
    }

    @Nested
    class SetConversionTests {

        @Test
        void setConversion_NullDealWithValue_ThrowsException() {
            CampaignInteraction interaction = buildCampaignInteraction();
            assertThatThrownBy(() -> interaction.setConversion(null, 100.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("provided when setting conversion");
        }

        @Test
        void setConversion_NegativeValue_ThrowsException() {
            CampaignInteraction interaction = buildCampaignInteraction();
            DealEntity dealEntity = mock(DealEntity.class);
            assertThatThrownBy(() -> interaction.setConversion(dealEntity, -50.0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("positive");
        }

        @Test
        void setConversion_ValidConversion_SetsFields() {
            CampaignInteraction interaction = buildCampaignInteraction();
            DealEntity dealEntity = mock(DealEntity.class);
            interaction.setConversion(dealEntity, 200.0);

            assertThat(interaction.getResultedDealEntity()).isEqualTo(dealEntity);
            assertThat(interaction.getConversionValue()).isEqualTo(200.0);
            assertThat(interaction.getUpdatedAt()).isAfter(NOW);
        }
    }

    @Nested
    class AddPropertyTests {

        @Test
        void addProperty_EmptyKey_ThrowsException() {
            CampaignInteraction interaction = buildCampaignInteraction();
            assertThatThrownBy(() -> interaction.addProperty("", "value"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("key cannot be empty");
        }

        @Test
        void addProperty_EmptyValue_ThrowsException() {
            CampaignInteraction interaction = buildCampaignInteraction();
            assertThatThrownBy(() -> interaction.addProperty("key", ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("value cannot be empty");
        }

        @Test
        void addProperty_ValidProperty_AddsToMap() {
            CampaignInteraction interaction = buildCampaignInteraction();
            interaction.addProperty("new_key", "new_value");

            assertThat(interaction.getProperties()).containsEntry("new_key", "new_value");
            assertThat(interaction.getProperties()).containsEntry("utm_source", "google");
            assertThat(interaction.getUpdatedAt()).isAfter(NOW);
        }
    }

    @Nested
    class CalculateAttributionTests {

        @Test
        void calculateAttribution_NonConversion_ReturnsZero() {
            CampaignInteraction interaction = buildCampaignInteraction();
            assertThat(interaction.calculateAttributionValue()).isZero();
        }

        @Test
        void calculateAttribution_ValidConversion_ReturnsCorrectValue() {
            CampaignInteraction interaction = buildCampaignInteraction();
            DealEntity dealEntity = mock(DealEntity.class);
            interaction.setConversion(dealEntity, 500.0);

            double result = interaction.calculateAttributionValue();
            assertThat(result).isEqualTo(500.0 * 0.2);
        }
    }

    private CampaignInteraction buildCampaignInteraction() {
        return new CampaignInteraction(
                CampaignInteractionId.generate(),
                mockCampaign,
                mockCustomer,
                MarketingInteractionType.AD_CLICK,
                NOW,
                new InteractionSource("Email", "Newsletter", "Summer Sale"),
                new DeviceInfo("Mobile", "192.168.1.1"),
                new GeoLocation("US", "CA", "Los Angeles"),
                properties,
                NOW
        );
    }
}