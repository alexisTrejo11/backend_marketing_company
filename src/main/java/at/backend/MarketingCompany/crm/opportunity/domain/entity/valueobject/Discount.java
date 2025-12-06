package at.backend.MarketingCompany.crm.opportunity.domain.entity.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Discount(BigDecimal percentage) {
    
    public static final Discount ZERO = new Discount(BigDecimal.ZERO);
    
    public Discount {
        if (percentage == null) {
            throw new IllegalArgumentException("Discount percentage cannot be null");
        }
        if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        percentage = percentage.setScale(2, RoundingMode.HALF_UP);
    }
    
    public Amount calculateDiscountAmount(Amount baseAmount) {
        return baseAmount.multiply(this.percentage.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
    }
}