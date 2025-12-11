package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import at.backend.MarketingCompany.shared.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact_persons",
       indexes = {
           @Index(name = "idx_contact_company", columnList = "company_id"),
           @Index(name = "idx_contact_email", columnList = "email")
       })
public class ContactPersonEntity extends BaseJpaEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CustomerCompanyEntity company;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "position")
    private String position;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "is_decision_maker")
    private Boolean isDecisionMaker = false;
    
    @Column(name = "is_primary_contact")
    private Boolean isPrimaryContact = false;
    
    @Column(name = "notes", length = 1000)
    private String notes;
    
    @PrePersist
    @PreUpdate
    public void validateContact() {
        if (email == null && phone == null) {
            throw new IllegalArgumentException("Contact person must have at least email or phone");
        }
    }
}