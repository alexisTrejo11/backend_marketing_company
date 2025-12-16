package at.backend.MarketingCompany.customer.adapter.output.persistence.entity;

import at.backend.MarketingCompany.customer.core.domain.valueobject.BillingInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BillingInformationEmbeddable {
  @Column(name = "billing_email")
  private String billingEmail;

  @Enumerated(EnumType.STRING)
  @Column(name = "preferred_payment_method")
  private BillingInformation.PaymentMethod preferredPaymentMethod;

  @Column(name = "billing_address", length = 500)
  private String billingAddress;

  @Column(name = "approved_credit")
  private Boolean approvedCredit;
}
