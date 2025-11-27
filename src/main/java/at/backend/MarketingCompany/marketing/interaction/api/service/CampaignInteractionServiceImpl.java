package at.backend.MarketingCompany.marketing.interaction.api.service;

import at.backend.MarketingCompany.common.exceptions.*;
import at.backend.MarketingCompany.crm.deal.repository.persistence.repository.JpaDealRepository;
import at.backend.MarketingCompany.crm.deal.repository.persistence.model.DealEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.marketing.campaign.api.repository.MarketingCampaignRepository;
import at.backend.MarketingCompany.marketing.campaign.domain.MarketingCampaign;
import at.backend.MarketingCompany.marketing.campaign.infrastructure.autoMappers.CampaignMappers;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionModel;
import at.backend.MarketingCompany.marketing.interaction.api.repository.CampaignInteractionRepository;
import at.backend.MarketingCompany.marketing.interaction.domain.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.domain.InteractionSource;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.autoMappers.InteractionMappers;
import at.backend.MarketingCompany.marketing.interaction.infrastructure.DTOs.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CampaignInteractionServiceImpl implements CampaignInteractionService {

    private final CampaignInteractionRepository repository;
    private final InteractionMappers interactionMappers;
    private final JpaDealRepository jpaDealRepository;
    private final MarketingCampaignRepository campaignRepository;
    private final CampaignMappers campaignMappers;

    @Override
    @Transactional
    public CampaignInteractionDTO create(CampaignInteractionInsertDTO dto) {
        validate(dto);

        CampaignInteraction domain = interactionMappers.insertDTOToDomain(dto);
        validateCampaignAndCustomer(domain);

        CampaignInteractionModel model = interactionMappers.domainToModel(domain);
        repository.save(model);

        return interactionMappers.domainToDTO(domain);
    }

    @Override
    @Transactional
    public CampaignInteractionDTO update(UUID id, CampaignInteractionInsertDTO dto) {
        CampaignInteraction existing = getDomainById(id);
        validate(dto);

        updateDomain(existing, dto);
        validateCampaignAndCustomer(existing);

        CampaignInteractionModel updatedModel = interactionMappers.domainToModel(existing);
        repository.save(updatedModel);

        return interactionMappers.domainToDTO(existing);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        CampaignInteraction domain = getDomainById(id);
        if (domain.isConversion()) {
            throw new BusinessLogicException("Cannot delete converted interactions");
        }
        repository.deleteById(id);
    }

    @Override
    public Page<CampaignInteractionDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(interactionMappers::modelToDTO);
    }

    @Override
    public CampaignInteractionDTO getById(UUID id) {
        return interactionMappers.domainToDTO(getDomainById(id));
    }

    @Override
    public List<CampaignInteractionDTO> getByCampaignId(UUID campaignId, LocalDateTime from, LocalDateTime to) {
        return repository.findByCampaignIdAndInteractionDateBetween(campaignId, from, to).stream()
                .map(interactionMappers::modelToDTO)
                .toList();
    }

    @Override
    public List<CampaignInteractionDTO> getByCustomerId(UUID customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(interactionMappers::modelToDTO)
                .toList();
    }

    @Override
    public ConversionSummaryDTO getConversionSummary(UUID campaignId) {
        MarketingCampaign campaign = campaignMappers.modelToDomain(
                campaignRepository.findById(campaignId)
                        .orElseThrow(() -> new EntityNotFoundException("Campaign not found"))
        );

        List<CampaignInteraction> interactions = repository.findByCampaignId(campaignId).stream()
                .map(interactionMappers::modelToDomain)
                .filter(CampaignInteraction::isConversion)
                .toList();

        double totalValue = interactions.stream()
                .mapToDouble(i -> i.getConversionValue() != null ? i.getConversionValue() : 0)
                .sum();

        return ConversionSummaryDTO.builder()
                .campaignId(campaignId)
                .conversionCount(interactions.size())
                .totalConversionValue(totalValue)
                .averageConversionValue(interactions.isEmpty() ? 0 : totalValue / interactions.size())
                .build();
    }

    public void validate(CampaignInteractionInsertDTO dto) {
        if (dto.getInteractionDate().isAfter(LocalDateTime.now())) {
            throw new InvalidInputException("Interaction date cannot be in the future");
        }
        if (dto.getConversionValue() != null && dto.getConversionValue() <= 0) {
            throw new InvalidInputException("Conversion value must be positive");
        }
    }

    private void validateCampaignAndCustomer(CampaignInteraction domain) {
        MarketingCampaign campaign = domain.getCampaign();
        if (!campaign.isActive()) {
            throw new BusinessLogicException("Campaign is not active");
        }

        CustomerModel customerModel = domain.getCustomerModel();
        if (customerModel.isBlocked()) {
            throw new BusinessLogicException("CustomerModel is blocked");
        }
    }

    private void updateDomain(CampaignInteraction domain, CampaignInteractionInsertDTO dto) {
        domain.setDetails(dto.getDetails());
        domain.setInteractionDate(dto.getInteractionDate());
        domain.setSource(InteractionSource.builder()
                .channel(dto.getSourceChannel())
                .medium(dto.getSourceMedium())
                .campaignName(dto.getSourceCampaign())
                .build());

        if (dto.getResultedDealId() != null || dto.getConversionValue() != null) {
            assert dto.getResultedDealId() != null;
            DealEntity dealEntity = jpaDealRepository.findById(dto.getResultedDealId().toString())
                    .orElseThrow(() -> new EntityNotFoundException("DealEntity not found"));
            domain.setConversion(dealEntity, dto.getConversionValue());
        }
    }

    private CampaignInteraction getDomainById(UUID id) {
        return repository.findById(id)
                .map(interactionMappers::modelToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Interaction not found"));
    }
}