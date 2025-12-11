package at.backend.MarketingCompany.customer.domain.valueobject;

import java.math.BigDecimal;
import java.util.Currency;

public record AnnualRevenue(
    BigDecimal amount,
    Currency currency,
    RevenueRange range
) {

    public AnnualRevenue(BigDecimal amount, Currency currency) {
        this(amount, currency, calculateRange(amount));
    }

    public enum RevenueRange {
        UNDER_100K,
        BETWEEN_100K_1M,
        BETWEEN_1M_10M,
        BETWEEN_10M_100M,
        OVER_100M
    }
    
    public AnnualRevenue {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Revenue amount must be positive");
        }
        if (currency == null) {
            currency = Currency.getInstance("USD");
        }
        if (range == null) {
            range = calculateRange(amount);
        }
    }


    
    private static RevenueRange calculateRange(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Cannot calculate range for null amount in AnnualRevenue");
        }

        if (amount.compareTo(new BigDecimal("100000")) < 0) return RevenueRange.UNDER_100K;
        if (amount.compareTo(new BigDecimal("1000000")) < 0) return RevenueRange.BETWEEN_100K_1M;
        if (amount.compareTo(new BigDecimal("10000000")) < 0) return RevenueRange.BETWEEN_1M_10M;
        if (amount.compareTo(new BigDecimal("100000000")) < 0) return RevenueRange.BETWEEN_10M_100M;
        return RevenueRange.OVER_100M;
    }
    
    public boolean isHighValue() {
        return range == RevenueRange.BETWEEN_10M_100M 
            || range == RevenueRange.OVER_100M;
    }
}