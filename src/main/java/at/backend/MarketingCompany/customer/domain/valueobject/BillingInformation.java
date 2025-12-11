package at.backend.MarketingCompany.customer.domain.valueobject;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.customer.domain.exceptions.CustomerDomainException;
import lombok.Builder;

@Builder
public record BillingInformation(
    String taxId,
    Email billingEmail,
    PaymentMethod preferredPaymentMethod,
    String billingAddress,
    boolean approvedCredit
) {

    public static BillingInformation create(String taxId, PaymentMethod paymentMethod) {
        return new BillingInformation(taxId, null, paymentMethod, null, false);
    }

    public BillingInformation {
        validate(taxId, billingEmail, preferredPaymentMethod);
    }
    
    public enum PaymentMethod {
        CREDIT_CARD,
        BANK_TRANSFER,
        PAYPAL,
        INVOICE
    }

    private static void validate(
            String taxId,
            Email billingEmail,
            PaymentMethod paymentMethod
    ) {
        if (taxId == null || taxId.isBlank()) {
            throw new CustomerDomainException("Tax ID cannot be null or empty");
        }

        if (taxId.length() < 5 || taxId.length() > 50) {
            throw new CustomerDomainException(
                    "Tax ID must be between 5 and 50 characters"
            );
        }

        if (billingEmail == null) {
           throw new CustomerDomainException("Billing email cannot be null at creation");
        }

        if (paymentMethod == null) {
            throw new CustomerDomainException("Payment method cannot be null");
        }
    }
}