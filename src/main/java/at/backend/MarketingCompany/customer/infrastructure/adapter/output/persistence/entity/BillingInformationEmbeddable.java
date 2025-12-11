package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BillingInformationEmbeddable {
    @Column(name = "billing_email")
    private String billingEmail;
    
    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;
    
    @Column(name = "billing_address", length = 500)
    private String billingAddress;
    
    @Column(name = "approved_credit")
    private Boolean approvedCredit;
}
