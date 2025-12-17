package at.backend.MarketingCompany.MarketingCampaing.customer.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

class ContractDetailsTest {
    
    @Test
    void isActive_CurrentDateBetweenStartAndEnd_ShouldReturnTrue() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(11),
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertTrue(contract.isActive());
    }
    
    @Test
    void isActive_BeforeStartDate_ShouldReturnFalse() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().plusDays(1), // Starts tomorrow
                LocalDate.now().plusYears(1),
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertFalse(contract.isActive());
    }
    
    @Test
    void isActive_AfterEndDate_ShouldReturnFalse() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().minusYears(1),
                LocalDate.now().minusDays(1), // Ended yesterday
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertFalse(contract.isActive());
    }
    
    @Test
    void isExpiringSoon_ExpiresWithin30Days_ShouldReturnTrue() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().minusYears(1),
                LocalDate.now().plusDays(15), // Ends in 15 days
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertTrue(contract.isExpiringSoon());
    }
    
    @Test
    void isExpiringSoon_ExpiresAfter30Days_ShouldReturnFalse() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().minusYears(1),
                LocalDate.now().plusDays(45), // Ends in 45 days
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertFalse(contract.isExpiringSoon());
    }
    
    @Test
    void isExpiringSoon_NotActive_ShouldReturnFalse() {
        // Arrange
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now().minusYears(2),
                LocalDate.now().minusYears(1), // Already expired
                new BigDecimal("1000"),
                ContractDetails.ContractType.ANNUAL,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertFalse(contract.isExpiringSoon());
    }
    
    @ParameterizedTest
    @EnumSource(ContractDetails.ContractType.class)
    void create_AllContractTypes_ShouldCreate(ContractDetails.ContractType type) {
        // Act
        ContractDetails contract = new ContractDetails(
                "CONTRACT-001",
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                new BigDecimal("1000"),
                type,
                Set.of("Service A"),
                true
        );
        
        // Assert
        assertEquals(type, contract.type());
    }
}