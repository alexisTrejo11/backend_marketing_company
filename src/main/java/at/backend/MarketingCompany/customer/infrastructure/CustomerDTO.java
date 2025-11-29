package at.backend.MarketingCompany.customer.infrastructure;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    private String company;
    private String industry;
    private String brandVoice;
    private String brandColors;
    private String targetMarket;

    private Set<String> competitorUrls;
    private String socialMediaHandles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean hasSocialMediaHandles() {
        return socialMediaHandles != null && !socialMediaHandles.isBlank();
    }

    public boolean hasCompetitors() {
        return competitorUrls != null && !competitorUrls.isEmpty();
    }
}
