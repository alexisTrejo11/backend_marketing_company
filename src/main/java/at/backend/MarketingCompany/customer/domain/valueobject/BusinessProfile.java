package at.backend.MarketingCompany.customer.domain.valueobject;

import lombok.Builder;

import java.util.Set;

@Builder
public record BusinessProfile(
    String company,
    String industry,
    String brandVoice,
    String brandColors,
    String targetMarket,
    Set<String> competitorUrls,
    SocialMediaHandles socialMediaHandles) {

  public boolean hasCompetitors() {
    return competitorUrls != null && !competitorUrls.isEmpty();
  }

  public boolean hasSocialMediaHandles() {
    return socialMediaHandles != null && !socialMediaHandles.hasHandles();
  }
}
