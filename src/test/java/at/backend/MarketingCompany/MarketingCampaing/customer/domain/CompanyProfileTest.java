package at.backend.MarketingCompany.MarketingCampaing.customer.domain;

import at.backend.MarketingCompany.customer.domain.valueobject.AnnualRevenue;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanyProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.domain.valueobject.Industry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Currency;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CompanyProfileTest {

    @Test
    void isStartup_FoundedWithin5Years_ShouldReturnTrue() {
        // Arrange
        CompanyProfile profile = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Services"))
                .size(CompanySize.SMALL)
                .revenue(new AnnualRevenue(new BigDecimal("100000"), Currency.getInstance("USD"), null))
                .foundingYear(Year.now().getValue() - 3)
                .build();

        // Assert
        assertTrue(profile.isStartup());
    }

    @Test
    void isStartup_FoundedMoreThan5YearsAgo_ShouldReturnFalse() {
        // Arrange
        CompanyProfile profile = CompanyProfile.builder()
                .industry(new Industry("TECH", "Technology", "Services"))
                .size(CompanySize.MEDIUM)
                .revenue(new AnnualRevenue(new BigDecimal("500000"), Currency.getInstance("USD"), null))
                .foundingYear(Year.now().getValue() - 10)
                .build();
    }
}