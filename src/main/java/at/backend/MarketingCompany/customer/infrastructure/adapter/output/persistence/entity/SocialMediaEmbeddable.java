package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import at.backend.MarketingCompany.customer.domain.valueobject.SocialMediaHandles;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class SocialMediaEmbeddable {

    @Column(name = "twitter_handle", length = 50)
    private String twitterHandle;

    @Column(name = "instagram_handle", length = 50)
    private String instagramHandle;

    @Column(name = "facebook_url", length = 255)
    private String facebookUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "youtube_handle", length = 50)
    private String youtubeHandle;

    @Column(name = "tiktok_handle", length = 50)
    private String tiktokHandle;

    @Column(name = "other_social_media", columnDefinition = "TEXT")
    private String otherSocialMedia; // JSON string for other platforms

    public static SocialMediaEmbeddable fromDomain(SocialMediaHandles handles)  {
        if (handles == null || !handles.hasHandles()) {
            return null;
        }

        SocialMediaEmbeddable embeddable = new SocialMediaEmbeddable();

        handles.getTwitterHandle().ifPresent(embeddable::setTwitterHandle);
        handles.getInstagramHandle().ifPresent(embeddable::setInstagramHandle);
        handles.getFacebookUrl().ifPresent(embeddable::setFacebookUrl);
        handles.getLinkedinUrl().ifPresent(embeddable::setLinkedinUrl);
        handles.getYoutubeHandle().ifPresent(embeddable::setYoutubeHandle);
        handles.getTiktokHandle().ifPresent(embeddable::setTiktokHandle);


        Map<String, String> otherPlatforms = handles.getAllHandles().entrySet().stream()
                .filter(entry -> !isStandardPlatform(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!otherPlatforms.isEmpty()) {
            try {
                embeddable.setOtherSocialMedia(new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsString(otherPlatforms));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return embeddable;
    }

    public SocialMediaHandles toDomain() {
        Map<String, String> handles = new HashMap<>();

        if (twitterHandle != null && !twitterHandle.isBlank()) {
            handles.put("twitter", twitterHandle);
        }
        if (instagramHandle != null && !instagramHandle.isBlank()) {
            handles.put("instagram", instagramHandle);
        }
        if (facebookUrl != null && !facebookUrl.isBlank()) {
            handles.put("facebook", facebookUrl);
        }
        if (linkedinUrl != null && !linkedinUrl.isBlank()) {
            handles.put("linkedin", linkedinUrl);
        }
        if (youtubeHandle != null && !youtubeHandle.isBlank()) {
            handles.put("youtube", youtubeHandle);
        }
        if (tiktokHandle != null && !tiktokHandle.isBlank()) {
            handles.put("tiktok", tiktokHandle);
        }

        if (otherSocialMedia != null && !otherSocialMedia.isBlank()) {
            try {
                Map<String, String> otherPlatforms = new com.fasterxml.jackson.databind.ObjectMapper()
                        .readValue(otherSocialMedia, new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {});
                handles.putAll(otherPlatforms);
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(SocialMediaEmbeddable.class)
                        .warn("Failed to parse otherSocialMedia JSON: {}", otherSocialMedia, e);
            }
        }

        return SocialMediaHandles.fromMap(handles);
    }

    private static boolean isStandardPlatform(String platform) {
        return Set.of("twitter", "instagram", "facebook", "linkedin", "youtube", "tiktok")
                .contains(platform.toLowerCase());
    }
}
