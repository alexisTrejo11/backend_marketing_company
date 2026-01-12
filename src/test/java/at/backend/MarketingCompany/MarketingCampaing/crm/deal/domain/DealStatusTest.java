package at.backend.MarketingCompany.MarketingCampaing.crm.deal.domain;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DealStatus")
class DealStatusTest {

    @ParameterizedTest
    @MethodSource("validTransitions")
    @DisplayName("should allow valid state transitions")
    void canTransitionTo_ValidTransitions_ShouldReturnTrue(DealStatus from, DealStatus to) {
        // When & Then
        assertThat(from.canTransitionTo(to)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidTransitions")
    @DisplayName("should reject invalid state transitions")
    void canTransitionTo_InvalidTransitions_ShouldReturnFalse(DealStatus from, DealStatus to) {
        // When & Then
        assertThat(from.canTransitionTo(to)).isFalse();
    }

    private static Stream<Arguments> validTransitions() {
        return Stream.of(
            Arguments.of(DealStatus.DRAFT, DealStatus.IN_NEGOTIATION),
            Arguments.of(DealStatus.DRAFT, DealStatus.CANCELLED),
            Arguments.of(DealStatus.IN_NEGOTIATION, DealStatus.SIGNED),
            Arguments.of(DealStatus.IN_NEGOTIATION, DealStatus.CANCELLED),
            Arguments.of(DealStatus.SIGNED, DealStatus.PAID),
            Arguments.of(DealStatus.SIGNED, DealStatus.CANCELLED),
            Arguments.of(DealStatus.PAID, DealStatus.IN_PROGRESS),
            Arguments.of(DealStatus.PAID, DealStatus.CANCELLED),
            Arguments.of(DealStatus.IN_PROGRESS, DealStatus.COMPLETED),
            Arguments.of(DealStatus.IN_PROGRESS, DealStatus.CANCELLED)
        );
    }

    private static Stream<Arguments> invalidTransitions() {
        return Stream.of(
            Arguments.of(DealStatus.COMPLETED, DealStatus.IN_NEGOTIATION),
            Arguments.of(DealStatus.CANCELLED, DealStatus.SIGNED),
            Arguments.of(DealStatus.SIGNED, DealStatus.DRAFT),
            Arguments.of(DealStatus.PAID, DealStatus.IN_NEGOTIATION),
            Arguments.of(DealStatus.COMPLETED, DealStatus.CANCELLED),
            Arguments.of(DealStatus.CANCELLED, DealStatus.COMPLETED)
        );
    }
}