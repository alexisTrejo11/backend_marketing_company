package at.backend.MarketingCompany.MarketingCampaing.customer.domain;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.core.domain.valueobject.ContactPerson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class ContactPersonTest {
    
    @Test
    void create_ValidContact_ShouldCreate() {
        // Act
        ContactPerson contact = new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john.doe@example.com"),
                new PhoneNumber("+1234567890"),
                "CEO",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        );
        
        // Assert
        assertEquals("John Doe", contact.getFullName());
        assertEquals("john.doe@example.com", contact.email().value());
        assertEquals("CEO", contact.position());
        assertEquals(ContactPerson.Department.EXECUTIVE, contact.department());
        assertTrue(contact.isDecisionMaker());
        assertTrue(contact.isPrimaryContact());
        assertTrue(contact.isExecutive());
    }
    
    @Test
    void create_NoPosition_ShouldUseDefault() {
        // Act
        ContactPerson contact = new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john.doe@example.com"),
                new PhoneNumber("+1234567890"),
                null, // Sin posición
                null, // Sin departamento
                false,
                false
        );
        
        // Assert
        assertEquals("Not specified", contact.position());
        assertEquals(ContactPerson.Department.OTHER, contact.department());
    }
    
    @Test
    void create_NoContactMethods_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ContactPerson(
                        new PersonName("John", "Doe"),
                        null, // Sin email
                        null, // Sin teléfono
                        "CEO",
                        ContactPerson.Department.EXECUTIVE,
                        true,
                        true
                )
        );
        
        assertEquals("At least one contact method is required", exception.getMessage());
    }
    
    @ParameterizedTest
    @EnumSource(ContactPerson.Department.class)
    void create_AllDepartments_ShouldCreate(ContactPerson.Department department) {
        // Act
        ContactPerson contact = new ContactPerson(
                new PersonName("John", "Doe"),
                new Email("john.doe@example.com"),
                null,
                "Manager",
                department,
                false,
                false
        );
        
        // Assert
        assertEquals(department, contact.department());
    }
    
    @Test
    void isExecutive_ExecutivePositions_ShouldReturnTrue() {
        // Test CEO
        ContactPerson ceo = new ContactPerson(
                new PersonName("John", "CEO"),
                new Email("ceo@example.com"),
                null,
                "Chief Executive Officer",
                ContactPerson.Department.EXECUTIVE,
                true,
                true
        );
        assertTrue(ceo.isExecutive());
        
        // Test CTO
        ContactPerson cto = new ContactPerson(
                new PersonName("Jane", "CTO"),
                new Email("cto@example.com"),
                null,
                "Chief Technology CEO",
                ContactPerson.Department.IT,
                true,
                false
        );
        assertTrue(cto.isExecutive());
        
        // Test CFO
        ContactPerson cfo = new ContactPerson(
                new PersonName("Bob", "CFO"),
                new Email("cfo@example.com"),
                null,
                "Chief Financial CFO",
                ContactPerson.Department.FINANCE,
                true,
                false
        );
        assertTrue(cfo.isExecutive());
    }
    
    @Test
    void isExecutive_NonExecutivePosition_ShouldReturnFalse() {
        ContactPerson manager = new ContactPerson(
                new PersonName("John", "Manager"),
                new Email("manager@example.com"),
                null,
                "Marketing Manager",
                ContactPerson.Department.MARKETING,
                false,
                false
        );
        assertFalse(manager.isExecutive());
    }
    
    @Test
    void equals_SameValues_ShouldBeEqual() {
        PersonName name = new PersonName("John", "Doe");
        Email email = new Email("john.doe@example.com");
        
        ContactPerson contact1 = new ContactPerson(
                name, email, null, "CEO", ContactPerson.Department.EXECUTIVE, true, true
        );
        
        ContactPerson contact2 = new ContactPerson(
                name, email, null, "CEO", ContactPerson.Department.EXECUTIVE, true, true
        );
        
        assertEquals(contact1, contact2);
        assertEquals(contact1.hashCode(), contact2.hashCode());
    }
}