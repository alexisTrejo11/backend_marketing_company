package at.backend.MarketingCompany.MarketingCampaing.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import at.backend.MarketingCompany.common.exceptions.InvalidInputException;
import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.api.controller.CampaignAttributionController;
import at.backend.MarketingCompany.marketing.attribution.api.service.CampaignAttributionService;
import at.backend.MarketingCompany.common.utils.InputValidator;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.CampaignAttributionDTO;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.CampaignAttributionInsertDTO;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.TouchPointDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CampaignAttributionControllerTest {

    @Mock
    private CampaignAttributionService campaignAttributionService;

    @Mock
    private InputValidator validator;

    @InjectMocks
    private CampaignAttributionController controller;

    private final UUID CAMPAIGN_ID = UUID.randomUUID();
    private final UUID ATTRIBUTION_ID = UUID.randomUUID();
    private final UUID DEAL_ID = UUID.randomUUID();
    private final Pageable PAGEABLE = Pageable.unpaged();

    @BeforeEach
    void setUp() {
    }

    @Nested
    class CreateTests {

        @Test
        void create_ValidInput_CallsService() {
            // Arrange
            CampaignAttributionInsertDTO input = createValidInsertDTO();
            CampaignAttributionDTO expectedDTO = createAttributionDTO();

            when(campaignAttributionService.create(any())).thenReturn(expectedDTO);

            // Act
            CampaignAttributionDTO result = controller.createAttribution(input);

            // Assert
            verify(validator).validate(input);
            verify(campaignAttributionService).create(input);
            assertThat(result).isEqualTo(expectedDTO);
        }

        @Test
        void create_InvalidInput_ThrowsException() {
            // Arrange
            CampaignAttributionInsertDTO invalidInput = CampaignAttributionInsertDTO.builder()
                    .dealId(null)
                    .campaignId(CAMPAIGN_ID)
                    .attributionModel(AttributionModel.FIRST_TOUCH)
                    .attributionPercentage(BigDecimal.valueOf(-10))
                    .attributedRevenue(BigDecimal.valueOf(-500))
                    .touchCount(-1)
                    .build();

            doThrow(new InvalidInputException("DealEntity ID cannot be null"))
                    .when(validator).validate(invalidInput);

            // Act & Assert
            assertThatThrownBy(() -> controller.createAttribution(invalidInput))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("DealEntity ID cannot be null");
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void update_ValidInput_CallsService() {
            // Arrange
            CampaignAttributionInsertDTO input = createValidInsertDTO();
            CampaignAttributionDTO expectedDTO = createAttributionDTO();

            when(campaignAttributionService.update(eq(ATTRIBUTION_ID), any())).thenReturn(expectedDTO);

            // Act
            CampaignAttributionDTO result = controller.updateAttribution(input, ATTRIBUTION_ID);

            // Assert
            verify(validator).validate(input);
            verify(campaignAttributionService).update(ATTRIBUTION_ID, input);
            assertThat(result).isEqualTo(expectedDTO);
        }

        @Test
        void update_InvalidInput_ThrowsException() {
            // Arrange
            CampaignAttributionInsertDTO invalidInput = CampaignAttributionInsertDTO.builder()
                    .dealId(null)
                    .campaignId(CAMPAIGN_ID)
                    .attributionModel(AttributionModel.FIRST_TOUCH)
                    .attributionPercentage(BigDecimal.valueOf(-10))
                    .attributedRevenue(BigDecimal.valueOf(-500))
                    .touchCount(-1)
                    .build();

            doThrow(new InvalidInputException("Name cannot be empty"))
                    .when(validator).validate(invalidInput);

            // Act & Assert
            assertThatThrownBy(() -> controller.updateAttribution(invalidInput, ATTRIBUTION_ID))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Name cannot be empty");
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void delete_ExistingAttribution_ReturnsTrue() {
            // Arrange
            doNothing().when(campaignAttributionService).delete(ATTRIBUTION_ID);

            // Act
            boolean result = controller.deleteAttribution(ATTRIBUTION_ID);

            // Assert
            verify(campaignAttributionService).delete(ATTRIBUTION_ID);
            assertThat(result).isTrue();
        }

        @Test
        void delete_NonExistingAttribution_ThrowsException() {
            // Arrange
            doThrow(new RuntimeException("Attribution not found"))
                    .when(campaignAttributionService).delete(ATTRIBUTION_ID);

            // Act & Assert
            assertThatThrownBy(() -> controller.deleteAttribution(ATTRIBUTION_ID))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Attribution not found");
        }
    }

    @Nested
    class QueryTests {

        @Test
        void getAllAttributions_ReturnsPage() {
            // Arrange
            Page<CampaignAttributionDTO> page = new PageImpl<>(List.of(createAttributionDTO()));
            when(campaignAttributionService.getAll(any(Pageable.class))).thenReturn(page);

            // Act
            Page<CampaignAttributionDTO> result = controller.getAllAttributions(PAGEABLE);

            // Assert
            verify(campaignAttributionService).getAll(PAGEABLE);
            assertThat(result).hasSize(1);
        }

        @Test
        void getAttributionById_ReturnsDTO() {
            // Arrange
            CampaignAttributionDTO expectedDTO = createAttributionDTO();
            when(campaignAttributionService.getById(ATTRIBUTION_ID)).thenReturn(expectedDTO);

            // Act
            CampaignAttributionDTO result = controller.getAttributionById(ATTRIBUTION_ID);

            // Assert
            verify(campaignAttributionService).getById(ATTRIBUTION_ID);
            assertThat(result).isEqualTo(expectedDTO);
        }

        @Test
        void getAttributionsByDealId_ReturnsList() {
            // Arrange
            List<CampaignAttributionDTO> expectedList = List.of(createAttributionDTO());
            when(campaignAttributionService.getAttributionsByDealId(DEAL_ID)).thenReturn(expectedList);

            // Act
            List<CampaignAttributionDTO> result = controller.getAttributionsByDealId(DEAL_ID);

            // Assert
            verify(campaignAttributionService).getAttributionsByDealId(DEAL_ID);
            assertThat(result).isEqualTo(expectedList);
        }
    }

    @Nested
    class TouchPointTests {

        @Test
        void addTouchPoint_ValidInput_CallsService() {
            // Arrange
            TouchPointDTO touchPoint = createTouchPointDTO();
            CampaignAttributionDTO expectedDTO = createAttributionDTO();

            when(campaignAttributionService.addTouchPoint(ATTRIBUTION_ID, touchPoint)).thenReturn(expectedDTO);

            // Act
            CampaignAttributionDTO result = controller.addTouchPoint(ATTRIBUTION_ID, touchPoint);

            // Assert
            verify(campaignAttributionService).addTouchPoint(ATTRIBUTION_ID, touchPoint);
            assertThat(result).isEqualTo(expectedDTO);
        }
    }

    private CampaignAttributionDTO createAttributionDTO() {
        return CampaignAttributionDTO.builder()
                .id(ATTRIBUTION_ID)
                .dealId(UUID.randomUUID())
                .campaignId(CAMPAIGN_ID)
                .attributionModel(AttributionModel.FIRST_TOUCH)
                .attributionPercentage(BigDecimal.valueOf(50.0))
                .attributedRevenue(BigDecimal.valueOf(1000))
                .firstTouchDate(LocalDateTime.now())
                .lastTouchDate(LocalDateTime.now().plusDays(1))
                .touchCount(3)
                .distribution(List.of())
                .build();
    }

    private CampaignAttributionInsertDTO createValidInsertDTO() {
        return CampaignAttributionInsertDTO.builder()
                .dealId(UUID.randomUUID())
                .campaignId(CAMPAIGN_ID)
                .attributionModel(AttributionModel.LAST_TOUCH)
                .attributionPercentage(BigDecimal.valueOf(75.0))
                .attributedRevenue(BigDecimal.valueOf(500))
                .touchCount(5)
                .build();
    }

    private TouchPointDTO createTouchPointDTO() {
        return new TouchPointDTO(LocalDateTime.now());
    }
}

