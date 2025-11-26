package at.backend.MarketingCompany.marketing.attribution.api.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CampaignAttributionRepository extends JpaRepository<CampaignAttributionModel, UUID> {
    List<CampaignAttributionModel> findByCampaign_Id(UUID campaignId);
    Page<CampaignAttributionModel> findByCampaign_Id(UUID campaignId, Pageable pageable);
    List<CampaignAttributionModel> findByDealEntity_Id(UUID dealId);

    boolean existsByDealEntity_Id(UUID dealId);
    boolean existsByCampaign_Id(UUID campaignId);

}