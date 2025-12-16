package at.backend.MarketingCompany.customer.core.domain.valueobject;

import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public record ContractDetails(
    String contractId,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal monthlyFee,
    ContractType type,
    Set<String> includedServices,
    boolean autoRenewal
) {

    @Getter
    public enum ContractType {
        MONTHLY(1),
        QUARTERLY(3),
        ANNUAL(12),
        ENTERPRISE(24),
        CUSTOM(0);

        private final int defaultDurationMonths;

        ContractType(int defaultDurationMonths) {
            this.defaultDurationMonths = defaultDurationMonths;
        }

    }
    
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return startDate != null 
            && endDate != null
            && !today.isBefore(startDate)
            && !today.isAfter(endDate);
    }
    
    public boolean isExpiringSoon() {
        return isActive() 
            && LocalDate.now().plusDays(30).isAfter(endDate);
    }

    public static ContractDetails of(
            String contractId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal monthlyFee,
            ContractType type,
            Set<String> includedServices,
            boolean autoRenewal
    ) {
        return new ContractDetails(
                contractId,
                startDate,
                endDate,
                monthlyFee,
                type,
                includedServices,
                autoRenewal
        );
    }

    public static ContractDetails create(
            String contractId,
            LocalDate startDate,
            BigDecimal monthlyFee,
            ContractType type,
            int durationMonths
    ) {
        Objects.requireNonNull(startDate, "Start date cannot be null");

        LocalDate endDate = startDate.plusMonths(durationMonths);

        return new ContractDetails(
                contractId,
                startDate,
                endDate,
                monthlyFee,
                type,
                Collections.emptySet(),
                false
        );
    }

    private static void validate(
            String contractId,
            LocalDate startDate,
            LocalDate endDate,
            BigDecimal monthlyFee,
            ContractType type,
            Set<String> includedServices
    ) {
        if (contractId == null || contractId.isBlank()) {
            throw new CustomerDomainException("Contract ID cannot be null or empty");
        }

        if (startDate == null) {
            throw new CustomerDomainException("Start date cannot be null");
        }

        if (endDate == null) {
            throw new CustomerDomainException("End date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new CustomerDomainException(
                    "End date cannot be before start date: " + startDate + " -> " + endDate
            );
        }

        if (monthlyFee == null || monthlyFee.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomerDomainException("Monthly fee must be positive");
        }

        if (type == null) {
            throw new CustomerDomainException("Contract type cannot be null");
        }
    }
}