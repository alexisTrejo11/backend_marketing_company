package at.backend.MarketingCompany.crm.deal.infrastructure.DTOs;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DealInput(
        @NotNull(message = "Opportunity ID cannot be null")
        Long opportunityId,

        @NotNull(message = "Service Package ID cannot be null")
        List<Long> servicePackageIds,

        @NotNull(message = "DealEntity status cannot be null")
        DealStatus dealStatus,

        @NotNull(message = "Final amount cannot be null")
        @Digits(integer = 15, fraction = 2, message = "Final amount must be a valid decimal with up to 15 digits and 2 decimal places")
        @PositiveOrZero(message = "Final amount must be zero or positive")
        BigDecimal finalAmount,

        @NotNull(message = "Start date cannot be null")
        @FutureOrPresent(message = "Start date must be in the present or future")
        LocalDate startDate,

        @Future(message = "End date must be in the future")
        LocalDate endDate,

        @Positive(message = "Campaign Manager Id must be positive")
        Long campaignManagerId,

        @Size(max = 5000, message = "Deliverables description cannot exceed 5000 characters")
        String deliverables,

        @Size(max = 5000, message = "Terms description cannot exceed 5000 characters")
        String terms
) {}
