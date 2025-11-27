package at.backend.MarketingCompany.MarketingCampaing.crm.deal.domain;

import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Value Objects")
class ValueObjectTest {

    @Nested
    @DisplayName("FinalAmount")
    class FinalAmountTest {

        @Test
        @DisplayName("should create with valid amount")
        void create_WithValidAmount_ShouldCreate() {
            // When
            var amount = new FinalAmount(new BigDecimal("1000.50"));

            // Then
            assertThat(amount.value()).isEqualByComparingTo("1000.50");
        }

        @Test
        @DisplayName("should create zero amount")
        void zero_ShouldCreateZeroAmount() {
            // When
            var zeroAmount = FinalAmount.zero();

            // Then
            assertThat(zeroAmount.value()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("should return true for positive amount")
        void isPositive_ForPositiveAmount_ShouldReturnTrue() {
            // Given
            var positiveAmount = new FinalAmount(new BigDecimal("100"));

            // When & Then
            assertThat(positiveAmount.isPositive()).isTrue();
        }

        @Test
        @DisplayName("should return false for zero amount")
        void isPositive_ForZeroAmount_ShouldReturnFalse() {
            // Given
            var zeroAmount = FinalAmount.zero();

            // When & Then
            assertThat(zeroAmount.isPositive()).isFalse();
        }

        @Test
        @DisplayName("should throw exception for null amount")
        void create_WithNullAmount_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> new FinalAmount(null))
                .isInstanceOf(DealValidationException.class)
                .hasMessageContaining("Final amount is required");
        }

        @Test
        @DisplayName("should throw exception for negative amount")
        void create_WithNegativeAmount_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> new FinalAmount(new BigDecimal("-100")))
                .isInstanceOf(DealValidationException.class)
                .hasMessageContaining("Final amount cannot be negative");
        }
    }

    @Nested
    @DisplayName("ContractPeriod")
    class ContractPeriodTest {

        @Test
        @DisplayName("should create with valid dates")
        void create_WithValidDates_ShouldCreate() {
            // Given
            var startDate = LocalDate.now();
            var endDate = startDate.plusDays(30);

            // When
            var period = new ContractPeriod(startDate, Optional.of(endDate));

            // Then
            assertThat(period.startDate()).isEqualTo(startDate);
            assertThat(period.endDate()).isEqualTo(endDate);
        }

        @Test
        @DisplayName("should create with null end date")
        void create_WithNullEndDate_ShouldCreate() {
            // Given
            var startDate = LocalDate.now();

            // When
            var period = new ContractPeriod(startDate, null);

            // Then
            assertThat(period.startDate()).isEqualTo(startDate);
            assertThat(period.endDate()).isNull();
        }

        @Test
        @DisplayName("should throw exception for null start date")
        void create_WithNullStartDate_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> new ContractPeriod(null, Optional.of(LocalDate.now())))
                .isInstanceOf(DealValidationException.class)
                .hasMessageContaining("Start date is required");
        }

        @Test
        @DisplayName("should throw exception when end date is before start date")
        void create_WithEndDateBeforeStart_ShouldThrowException() {
            // Given
            var startDate = LocalDate.now();
            var endDate = startDate.minusDays(1);

            // When & Then
            assertThatThrownBy(() -> new ContractPeriod(startDate, Optional.of(endDate)))
                .isInstanceOf(DealValidationException.class)
                .hasMessageContaining("End date must be after start date");
        }
    }

    @Nested
    @DisplayName("DealId")
    class DealIdTest {

        @Test
        @DisplayName("should create with valid UUID")
        void create_WithValidUUID_ShouldCreate() {
            // Given
            var uuid = java.util.UUID.randomUUID();

            // When
            var dealId = new DealId(uuid.toString());

            // Then
            assertThat(dealId.value()).isEqualTo(uuid);
        }

        @Test
        @DisplayName("should generate new DealId")
        void create_ShouldGenerateNewId() {
            // When
            var dealId = DealId.create();

            // Then
            assertThat(dealId.value()).isNotNull();
        }

        @Test
        @DisplayName("should throw exception for null UUID")
        void create_WithNullUUID_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> new DealId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Deal ID cannot be null");
        }
    }
}