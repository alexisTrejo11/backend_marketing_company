package at.backend.MarketingCompany.MarketingCampaing.service;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import at.backend.MarketingCompany.marketing.attribution.api.repository.CampaignAttributionModel;
import at.backend.MarketingCompany.marketing.attribution.api.repository.CampaignAttributionRepository;
import at.backend.MarketingCompany.marketing.attribution.api.service.AttributionCalculator;
import at.backend.MarketingCompany.marketing.attribution.api.service.CampaignAttributionServiceImpl;
import at.backend.MarketingCompany.marketing.attribution.domain.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.domain.HelperHandlers.*;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.*;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.automappers.AttributionMappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignAttributionServiceImplTest {

    @Mock
    private CampaignAttributionRepository attributionRepository;

    @Mock
    private AttributionCalculator calculator;

    @Mock
    private AttributionMappers mappers;

    @InjectMocks
    private CampaignAttributionServiceImpl service;

    private final UUID TEST_ID = UUID.randomUUID();
    private final UUID TEST_CAMPAIGN_ID = UUID.randomUUID();
    private final UUID TEST_DEAL_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetById_Success() {
        // Arrange
        CampaignAttribution domain = createSampleDomain();
        CampaignAttributionModel model = new CampaignAttributionModel();
        CampaignAttributionDTO expectedDto = createSampleDTO();

        when(attributionRepository.findById(TEST_ID)).thenReturn(Optional.of(model));
        when(mappers.modelToDomain(model)).thenReturn(domain);
        when(mappers.domainToDTO(domain)).thenReturn(expectedDto);

        // Act
        CampaignAttributionDTO result = service.getById(TEST_ID);

        // Assert
        assertEquals(expectedDto, result);
        verify(attributionRepository).findById(TEST_ID);
        verify(mappers).modelToDomain(model);
        verify(mappers).domainToDTO(domain);
    }

    @Test
    void testCreate_Successful() {
        // Arrange
        CampaignAttributionInsertDTO insertDTO = createSampleInsertDTO();
        CampaignAttribution domain = createSampleDomain();
        CampaignAttribution calculatedDomain = createSampleDomainWithPercentage();
        CampaignAttributionModel model = new CampaignAttributionModel();

        when(mappers.insertDTOToDomain(insertDTO)).thenReturn(domain);
        when(calculator.initialCalculation(domain)).thenReturn(calculatedDomain);
        when(attributionRepository.existsByCampaign_Id(any())).thenReturn(true);
        when(attributionRepository.existsByDealEntity_Id(any())).thenReturn(true);
        when(mappers.domainToModel(calculatedDomain)).thenReturn(model);
        when(mappers.domainToDTO(calculatedDomain)).thenReturn(createSampleDTO());

        // Act
        CampaignAttributionDTO result = service.create(insertDTO);

        // Assert
        assertNotNull(result);
        verify(mappers).insertDTOToDomain(insertDTO);
        verify(calculator).initialCalculation(domain);
        verify(attributionRepository).save(model);
    }

    @Test
    void testAddTouchPoint_Success() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        TouchPointDTO touch = new TouchPointDTO(now.plusHours(1));
        CampaignAttribution domain = createSampleDomain();
        CampaignAttribution updatedDomain = createSampleDomainWithTouches();
        CampaignAttribution adjustedDomain = createSampleDomainWithPercentage();

        CampaignAttribution domainSpy = spy(domain);

        when(attributionRepository.findById(TEST_ID)).thenReturn(Optional.of(new CampaignAttributionModel()));
        when(mappers.modelToDomain(any())).thenReturn(domainSpy);
        when(domainSpy.addTouch(touch.getTimestamp())).thenReturn(updatedDomain);
        when(calculator.adjustForNewTouch(updatedDomain)).thenReturn(adjustedDomain);
        when(mappers.domainToDTO(adjustedDomain)).thenReturn(createSampleDTO());

        // Act
        CampaignAttributionDTO result = service.addTouchPoint(TEST_ID, touch);

        // Assert
        assertNotNull(result);
        verify(attributionRepository).findById(TEST_ID);
        verify(calculator).adjustForNewTouch(updatedDomain);
        verify(attributionRepository).save(any());
    }


    @Test
    void testGetRevenueDistribution_Success() {
        // Arrange
        List<CampaignAttributionModel> models = List.of(new CampaignAttributionModel());
        List<CampaignAttribution> domains = Collections.singletonList(createSampleDomainWithPercentage());
        CampaignAttributionDTO expectedDto = createSampleRevenueDistributionDTO();

        when(attributionRepository.findByCampaign_Id(TEST_CAMPAIGN_ID)).thenReturn(models);
        when(mappers.modelToDomain(any())).thenReturn(domains.getFirst());
        when(calculator.calculateRevenueDistribution(domains)).thenReturn(expectedDto);

        // Act
        CampaignAttributionDTO result = service.getRevenueDistribution(TEST_CAMPAIGN_ID);

        // Assert
        assertEquals(expectedDto, result);
        verify(attributionRepository).findByCampaign_Id(TEST_CAMPAIGN_ID);
        verify(calculator).calculateRevenueDistribution(domains);
    }

    @Test
    void testRecalculateAllForCampaign_Success() {
        // Arrange
        List<CampaignAttributionModel> models = List.of(new CampaignAttributionModel());
        CampaignAttribution domain = createSampleDomain();
        CampaignAttribution recalculatedDomain = createSampleDomainWithPercentage();

        when(attributionRepository.findByCampaign_Id(TEST_CAMPAIGN_ID)).thenReturn(models);
        when(mappers.modelToDomain(any())).thenReturn(domain);
        when(calculator.recalculateForModel(domain)).thenReturn(recalculatedDomain);
        when(mappers.domainToModel(recalculatedDomain)).thenReturn(new CampaignAttributionModel());

        // Act
        service.recalculateAllForCampaign(TEST_CAMPAIGN_ID);

        // Assert
        verify(attributionRepository).saveAll(anyList());
        verify(calculator, times(models.size())).recalculateForModel(domain);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        CampaignAttributionModel model = new CampaignAttributionModel();
        CampaignAttribution domainModel = new CampaignAttribution();

        when(attributionRepository.findById(TEST_ID)).thenReturn(Optional.of(model));
        when(mappers.modelToDomain(model)).thenReturn(domainModel);

        // Act
        service.delete(TEST_ID);

        // Assert
        verify(attributionRepository).deleteById(TEST_ID);
    }

    @Test
    void testValidate_InvalidInput() {
        // Arrange
        CampaignAttributionInsertDTO invalidDto = new CampaignAttributionInsertDTO();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.validate(invalidDto));
    }

    private CampaignAttribution createSampleDomain() {
        return CampaignAttribution.builder()
                .id(AttributionId.of(TEST_ID))
                .campaignId(CampaignId.of(TEST_CAMPAIGN_ID))
                .dealId(DealId.of("TEST_DEAL_ID"))
                .model(AttributionModel.FIRST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(TouchTimeline.create())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private CampaignAttribution createSampleDomainWithTouches() {
        return CampaignAttribution.builder()
                .id(AttributionId.of(TEST_ID))
                .campaignId(CampaignId.of(TEST_CAMPAIGN_ID))
                .dealId(DealId.of(String.valueOf(TEST_DEAL_ID)))
                .model(AttributionModel.FIRST_TOUCH)
                .percentage(new AttributionPercentage(BigDecimal.ZERO))
                .revenue(new AttributedRevenue(BigDecimal.valueOf(1000)))
                .timeline(TouchTimeline.builder()
                        .firstTouch(LocalDateTime.now())
                        .lastTouch(LocalDateTime.now())
                        .touches(List.of(LocalDateTime.now()))
                        .touchCount(1)
                        .build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private CampaignAttribution createSampleDomainWithPercentage() {
        return CampaignAttribution.builder()
                .id(AttributionId.of(TEST_ID))
                .campaignId(CampaignId.of(TEST_CAMPAIGN_ID))
                .dealId(DealId.of(String.valueOf(TEST_DEAL_ID)))
                .percentage(new AttributionPercentage(BigDecimal.valueOf(50)))
                .build();
    }


    private CampaignAttributionInsertDTO createSampleInsertDTO() {
        return CampaignAttributionInsertDTO.builder()
                .campaignId(TEST_CAMPAIGN_ID)
                .dealId(String.valueOf(TEST_DEAL_ID))
                .build();
    }

    private CampaignAttributionDTO createSampleDTO() {
        return CampaignAttributionDTO.builder()
                .id(TEST_ID)
                .campaignId(TEST_CAMPAIGN_ID)
                .dealId("TEST_DEAL_ID")
                .attributionPercentage(BigDecimal.valueOf(50))
                .build();
    }

    private CampaignAttributionDTO createSampleRevenueDistributionDTO() {
        return CampaignAttributionDTO.builder()
                .attributedRevenue(BigDecimal.valueOf(1000))
                .distribution(Collections.singletonList(createSampleDTO()))
                .build();
    }
}
