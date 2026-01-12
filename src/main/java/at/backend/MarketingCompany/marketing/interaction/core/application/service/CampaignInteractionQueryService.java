package at.backend.MarketingCompany.marketing.interaction.core.application.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.exception.MarketingDomainException;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.application.query.InteractionQuery;
import at.backend.MarketingCompany.marketing.interaction.core.domain.entity.CampaignInteraction;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.port.input.InteractionQueryInputPort;
import at.backend.MarketingCompany.marketing.interaction.core.port.output.InteractionRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampaignInteractionQueryService implements InteractionQueryInputPort {
  private final InteractionRepositoryPort interactionRepository;

  @Override
  @Transactional(readOnly = true)
  public CampaignInteraction getInteractionById(CampaignInteractionId interactionId) {
    return findInteractionByIdOrThrow(interactionId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> searchInteractions(
      InteractionQuery query,
      Pageable pageable) {

    if (query.isEmpty()) {
      return interactionRepository.findAll(pageable);
    }

    return interactionRepository.findByFilters(
        pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByCampaign(
      MarketingCampaignId campaignId,
      Pageable pageable) {

    return interactionRepository.findByCampaignId(campaignId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByCustomer(CustomerCompanyId customerId, Pageable pageable) {
    return interactionRepository.findByCustomerId(customerId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getConversionsByCampaign(
      MarketingCampaignId campaignId,
      Pageable pageable) {

    return interactionRepository.findByCampaignIdAndIsConversion(
        campaignId,
        true,
        pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByChannel(MarketingChannelId channelId, Pageable pageable) {
    return interactionRepository.findByChannelId(channelId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByDateRange(
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {

    return interactionRepository.findByDateRange(startDate, endDate, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CampaignInteraction> getInteractionsByUtm(
      String utmSource,
      String utmMedium,
      String utmCampaign,
      Pageable pageable) {

    return interactionRepository.findByUtmParameters(
        utmSource,
        utmMedium,
        utmCampaign,
        pageable);
  }

  private CampaignInteraction findInteractionByIdOrThrow(CampaignInteractionId interactionId) {
    return interactionRepository.findById(interactionId)
        .orElseThrow(() -> new MarketingDomainException(
            "Interaction not found with id: " + interactionId.getValue()));
  }
}
