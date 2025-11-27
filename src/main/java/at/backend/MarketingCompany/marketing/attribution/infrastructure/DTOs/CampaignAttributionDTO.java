package at.backend.MarketingCompany.marketing.attribution.infrastructure.DTOs;

import at.backend.MarketingCompany.common.utils.Enums.MarketingCampaign.AttributionModel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CampaignAttributionDTO {

    private UUID id;

    @NotNull(message = "DealEntity ID cannot be null")
    private String dealId;

    @NotNull(message = "Campaign ID cannot be null")
    private UUID campaignId;

    @NotNull(message = "Attribution model cannot be null")
    private AttributionModel attributionModel;

    @DecimalMin(value = "0.0", message = "Attribution percentage must be between 0 and 100")
    @DecimalMax(value = "100.0", message = "Attribution percentage must be between 0 and 100")
    private BigDecimal attributionPercentage;

    @DecimalMin(value = "0.0", message = "Attributed revenue must be greater than or equal to zero")
    private BigDecimal attributedRevenue;

    private LocalDateTime firstTouchDate;

    private LocalDateTime lastTouchDate;

    @Min(value = 0, message = "Touch count must be greater than or equal to zero")
    private Integer touchCount;

    List<CampaignAttributionDTO> distribution;
}
