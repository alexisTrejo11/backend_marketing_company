package at.backend.MarketingCompany.marketing.attribution.api.service;

import at.backend.MarketingCompany.marketing.attribution.api.repository.CampaignAttributionModel;
import at.backend.MarketingCompany.marketing.attribution.domain.CampaignAttribution;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.TouchPointDTO;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.automappers.AttributionMappers;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.CampaignAttributionDTO;
import at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs.CampaignAttributionInsertDTO;
import at.backend.MarketingCompany.marketing.attribution.api.repository.CampaignAttributionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignAttributionServiceImpl implements CampaignAttributionService {
    private final CampaignAttributionRepository attributionRepository;
    private final AttributionCalculator calculator;
    private final AttributionMappers mappers;

    @Override
    public Page<CampaignAttributionDTO> getAll(Pageable pageable) {
        return attributionRepository.findAll(pageable)
                .map(mappers::modelToDTO);
    }

    @Override
    public CampaignAttributionDTO getById(UUID id) {
        CampaignAttribution attribution = getDomainAttribution(id);

        return mappers.domainToDTO(attribution);
    }

    @Override
    public Page<CampaignAttributionDTO> getAttributionsByCampaign(UUID campaignId, Pageable pageable) {
        return attributionRepository.findByCampaign_Id(campaignId, pageable)
                .map(mappers::modelToDTO);
    }

    @Override
    public List<CampaignAttributionDTO> getAttributionsByDealId(UUID dealId) {
        return attributionRepository.findByDealEntity_Id(dealId)
                .stream()
                .map(mappers::modelToDTO)
                .toList();
    }

    @Override
    public List<CampaignAttributionDTO> getAttributionsByCampaignId(UUID campaignId) {
        return attributionRepository.findByCampaign_Id(campaignId)
                .stream()
                .map(mappers::modelToDTO)
                .toList();    }


    @Override
    @Transactional
    public CampaignAttributionDTO create(CampaignAttributionInsertDTO dto) {
        CampaignAttribution attribution = mappers.insertDTOToDomain(dto);

        validateRelationships(attribution);
        attribution = calculator.initialCalculation(attribution);

        save(attribution);

        return mappers.domainToDTO(attribution);
    }

    @Override
    @Transactional
    public CampaignAttributionDTO update(UUID id, CampaignAttributionInsertDTO insertDTO) {
        CampaignAttribution attribution = getDomainAttribution(id);

        // UnMutable mappers.updateDomain(attribution, insertDTO);
        CampaignAttribution updatedAttribution = calculator.recalculateForModel(attribution);
        save(updatedAttribution);

        return mappers.domainToDTO(updatedAttribution);
    }


    @Override
    @Transactional
    public void delete(UUID id) {
        getDomainAttribution(id);

        attributionRepository.deleteById(id);
    }

    @Override
    public void validate(CampaignAttributionInsertDTO input) {
        if (input.getDealId() == null || input.getCampaignId() == null) {
            throw new IllegalArgumentException("DealEntity ID and Campaign ID cannot be null");
        }
    }

    @Override
    @Transactional
    public CampaignAttributionDTO addTouchPoint(UUID attributionId, TouchPointDTO touch) {
        CampaignAttribution attribution = getDomainAttribution(attributionId);
        var updated = attribution.addTouch(touch.getTimestamp());

        updated = calculator.adjustForNewTouch(updated);
        save(updated);

        return mappers.domainToDTO(updated);
    }

    @Override
    @Transactional
    public void recalculateAllForCampaign(UUID campaignId) {
        List<CampaignAttributionModel> recalculatedModels = attributionRepository.findByCampaign_Id(campaignId)
                .stream()
                .map(mappers::modelToDomain)
                .map(calculator::recalculateForModel)
                .map(mappers::domainToModel)
                .toList();

        attributionRepository.saveAll(recalculatedModels);
    }

    /*
      public BigDecimal getCampaignAttributedRevenue(String campaignId) {
        return attributionRepository.calculateTotalRevenueForCampaign(CampaignAttribution.CampaignId.of(campaignId));
    }
     */

    @Override
    public CampaignAttributionDTO getRevenueDistribution(UUID campaignId) {
        List<CampaignAttribution> attributions = attributionRepository.findByCampaign_Id(campaignId)
                .stream()
                .map(mappers::modelToDomain)
                .toList();

        if (attributions.isEmpty()) {
            throw new EntityNotFoundException("No attributions found for campaign: " + campaignId);
        }

        return calculator.calculateRevenueDistribution(attributions);
    }

    private CampaignAttribution getDomainAttribution(UUID id) {
        return attributionRepository.findById(id)
                .map(mappers::modelToDomain)
                .orElseThrow(() -> new EntityNotFoundException("Attribution not found: " + id));
    }

    private void validateRelationships(CampaignAttribution attribution) {
        if (!attributionRepository.existsByCampaign_Id(attribution.getCampaignId().getValue())) {
            throw new EntityNotFoundException("Invalid campaign ID: " + attribution.getCampaignId());
        }

        if (!attributionRepository.existsByDealEntity_Id(attribution.getDealId().getValue())) {
            throw new EntityNotFoundException("Invalid dealEntity ID: " + attribution.getDealId());
        }
    }

    private void save(CampaignAttribution attribution) {
        CampaignAttributionModel model = mappers.domainToModel(attribution);
        attributionRepository.save(model);
    }
}
