package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import lombok.Builder;

@Builder
public record CustomerResponse(
    String id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String company,
    String industry,
    String brandVoice,
    String brandColors,
    String targetMarket,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Set<String> competitorUrls,
    Map<String, String> socialMediaHandles,
    String status,
    boolean blocked,
    boolean hasSocialMediaHandles,
    boolean hasCompetitors) {
}
