package at.backend.MarketingCompany.MarketingCampaing.customer.domain;

import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CompanyNameTest {
    
    @Test
    void create_ValidName_ShouldCreate() {
        // Act
        CompanyName companyName = new CompanyName("Valid Company");
        
        // Assert
        assertEquals("Valid Company", companyName.value());
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void create_InvalidName_ShouldThrowException(String invalidName) {
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> new CompanyName(invalidName)
        );
        
        assertEquals("Company name cannot be null or empty", exception.getMessage());
    }
    
    @Test
    void create_NameTooShort_ShouldThrowException() {
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> new CompanyName("A")
        );
        
        assertEquals("Company name must be at least 2 characters", exception.getMessage());
    }
    
    @Test
    void create_NameTooLong_ShouldThrowException() {
        // Arrange
        String longName = "A".repeat(101);
        
        // Act & Assert
        CustomerDomainException exception = assertThrows(
                CustomerDomainException.class,
                () -> new CompanyName(longName)
        );
        
        assertEquals("Company name cannot exceed 100 characters", exception.getMessage());
    }
    
    @Test
    void getNormalized_ShouldTrimAndUpperCase() {
        // Arrange
        CompanyName companyName = new CompanyName("  Example Company  ");
        
        // Act
        String normalized = companyName.getNormalized();
        
        // Assert
        assertEquals("EXAMPLE COMPANY", normalized);
    }
    
    @Test
    void isSimilarTo_SimilarNames_ShouldReturnTrue() {
        // Arrange
        CompanyName companyName = new CompanyName("TechCorp");
        
        // Act & Assert
        assertTrue(companyName.isSimilarTo("TechCorp Solutions"));
        assertTrue(companyName.isSimilarTo("My TechCorp"));
        assertTrue(companyName.isSimilarTo("TECHCORP")); // Case insensitive
    }
    
    @Test
    void isSimilarTo_DifferentNames_ShouldReturnFalse() {
        // Arrange
        CompanyName companyName = new CompanyName("TechCorp");
        
        // Act & Assert
        assertFalse(companyName.isSimilarTo("Different Corp")); // TODO: Check
        assertTrue(companyName.isSimilarTo("Tech")); // Tech is a substring of TechCorp
    }
    
    @Test
    void equals_SameValue_ShouldBeEqual() {
        // Arrange
        CompanyName name1 = new CompanyName("Same Company");
        CompanyName name2 = new CompanyName("Same Company");
        
        // Act & Assert
        assertEquals(name1, name2);
        assertEquals(name1.hashCode(), name2.hashCode());
    }
    
    @Test
    void equals_DifferentValue_ShouldNotBeEqual() {
        // Arrange
        CompanyName name1 = new CompanyName("Company A");
        CompanyName name2 = new CompanyName("Company B");
        
        // Act & Assert
        assertNotEquals(name1, name2);
        assertNotEquals(name1.hashCode(), name2.hashCode());
    }
}