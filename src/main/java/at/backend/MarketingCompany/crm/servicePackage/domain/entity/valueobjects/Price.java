package at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects;

import at.backend.MarketingCompany.shared.domain.exceptions.MissingFieldException;
import at.backend.MarketingCompany.crm.servicePackage.domain.exceptions.PriceException;

import java.math.BigDecimal;

public record Price(BigDecimal amount) {
    public Price {
        if (amount == null) {
            throw new MissingFieldException("Price", "amount");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException("Price cannot be negative");
        }
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new PriceException("Price must not exceed 1,000,000");
        }
    }

    public static Price of(BigDecimal amount) {
        return new Price(amount);
    }
}