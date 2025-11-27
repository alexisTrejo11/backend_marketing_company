package at.backend.MarketingCompany.MarketingCampaing.domain;

import at.backend.MarketingCompany.common.exceptions.BusinessLogicException;
import at.backend.MarketingCompany.common.exceptions.InvalidInputException;
import at.backend.MarketingCompany.crm.deal.repository.persistence.repository.JpaDealRepository;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignRepository;
import at.backend.MarketingCompany.marketing.campaign.domain.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.infrastructure.autoMappers.CampaignMappers;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionModel;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionRepository;
import at.backend.MarketingCompany.marketing.interaction.domain.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionDTO;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.CampaignInteractionInsertDTO;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.autoMappers.InteractionMappers;
import at.backend.MarketingCompany.marketing.interaction.api.service.CampaignInteractionServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignInteractionServiceTest {

    @InjectMocks
    private CampaignInteractionServiceImpl service;

    @Mock
    private CampaignInteractionRepository repository;

    @Mock
    private InteractionMappers interactionMappers;

    @Mock
    private JpaDealRepository jpaDealRepository;

    @Mock
    private MarketingCampaignRepository campaignRepository;

    @Mock
    private CampaignMappers campaignMappers;

    private CampaignInteractionInsertDTO insertDTO;
    private CampaignInteraction interaction;
    private CampaignInteractionModel interactionModel;
    private CampaignInteractionDTO interactionDTO;
    private UUID interactionId;
    private MarketingCampaign campaign;
    private CustomerModel customerModel;

    @BeforeEach
    void setUp() {
        interactionId = UUID.randomUUID();
        insertDTO = new CampaignInteractionInsertDTO();
        insertDTO.setInteractionDate(LocalDateTime.now().minusDays(1));
        interaction = new CampaignInteraction();
        interactionModel = new CampaignInteractionModel();
        interactionDTO = new CampaignInteractionDTO();

        customerModel = mock(CustomerModel.class);
        when(customerModel.isBlocked()).thenReturn(false);

        campaign = mock(MarketingCampaign.class);
        when(campaign.isActive()).thenReturn(true);

        interaction = mock(CampaignInteraction.class);
        when(interaction.getCampaign()).thenReturn(campaign);
        when(interaction.getCustomerModel()).thenReturn(customerModel);

    }

    @Test
    void create_ValidInteraction_Success() {
        when(interactionMappers.insertDTOToDomain(insertDTO)).thenReturn(interaction);
        when(interactionMappers.domainToModel(interaction)).thenReturn(interactionModel);
        when(interactionMappers.domainToDTO(interaction)).thenReturn(interactionDTO);

        CampaignInteractionDTO result = service.create(insertDTO);

        assertNotNull(result);
        verify(repository).save(interactionModel);
    }

    @Test
    void create_FutureInteractionDate_ThrowsException() {
        insertDTO.setInteractionDate(LocalDateTime.now().plusDays(1));
        assertThrows(InvalidInputException.class, () -> service.create(insertDTO));
    }

    @Test
    void delete_NonConvertedInteraction_Success() {
        when(repository.findById(interactionId)).thenReturn(Optional.of(interactionModel));

        CampaignInteraction domainInteraction = mock(CampaignInteraction.class);
        when(interactionMappers.modelToDomain(interactionModel)).thenReturn(domainInteraction);
        service.delete(interactionId);

        verify(repository).deleteById(interactionId);
    }

    @Test
    void delete_ConvertedInteraction_ThrowsException() {
        when(repository.findById(interactionId)).thenReturn(Optional.of(interactionModel));

        CampaignInteraction domainInteraction = mock(CampaignInteraction.class);
        when(interactionMappers.modelToDomain(interactionModel)).thenReturn(domainInteraction);
        when(domainInteraction.isConversion()).thenReturn(true);

        assertThrows(BusinessLogicException.class, () -> service.delete(interactionId));

        verify(repository, never()).deleteById(interactionId);
    }

    @Test
    void getById_ExistingInteraction_ReturnsDTO() {
        when(repository.findById(interactionId)).thenReturn(Optional.of(interactionModel));
        when(interactionMappers.modelToDomain(interactionModel)).thenReturn(interaction);
        when(interactionMappers.domainToDTO(interaction)).thenReturn(interactionDTO);

        CampaignInteractionDTO result = service.getById(interactionId);

        assertNotNull(result);
    }

    @Test
    void getById_NonExistingInteraction_ThrowsException() {
        when(repository.findById(interactionId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(interactionId));
    }

    @Test
    void getAll_ReturnsPageOfInteractions() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CampaignInteractionModel> page = new PageImpl<>(List.of(interactionModel));
        when(repository.findAll(pageable)).thenReturn(page);
        when(interactionMappers.modelToDTO(any())).thenReturn(interactionDTO);

        Page<CampaignInteractionDTO> result = service.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}

