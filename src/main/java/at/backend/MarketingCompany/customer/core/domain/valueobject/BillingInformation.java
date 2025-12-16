package at.backend.MarketingCompany.customer.core.domain.valueobject;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.customer.core.domain.exceptions.CustomerDomainException;
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
        validate(taxId, paymentMethod);
        return new BillingInformation(taxId, null, paymentMethod, null, false);
    }

    
    public enum PaymentMethod {
        CREDIT_CARD,
        BANK_TRANSFER,
        PAYPAL,
        INVOICE
    }

    private static void validate(
            String taxId,
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

        if (paymentMethod == null) {
            throw new CustomerDomainException("Payment method cannot be null");
        }
    }
}