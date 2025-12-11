package at.backend.MarketingCompany.customer.application.dto.command;

import at.backend.MarketingCompany.customer.domain.valueobject.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public class CompanyCommands {
    @Builder
    public record CreateCompanyCommand(
            String companyName,
            String industry,
            CompanySize companySize,
            Integer employeeCount,
            Integer foundingYear,
            String missionStatement,
            String targetMarket,
            Set<ContactPersonCommand> contactPersons,
            String taxId,
            String website,
            @NotNull Set<String> keyProducts
    ) {

        public record ContactPersonCommand(
                String firstName,
                String lastName,
                String email,
                String phone,
                String position,
                ContactPerson.Department department,
                boolean isDecisionMaker,
                boolean isPrimaryContact
        ) {
        }
    }

    @Builder
    public static record UpdateCompanyCommand(
            @NotNull CustomerCompanyId id,
            String companyName,
            UpdateCompanyProfileCommand profileCommand,
            UpdateCompanyBillingCommand billingCommand
    ) {
        @Builder
        public record UpdateCompanyBillingCommand(
                @NotNull String taxId,
                @Email String billingEmail,
                Optional<BillingInformation.PaymentMethod> paymentMethod,
                String billingAddress,
                boolean approvedCredit
        ) {
        }

        public record UpdateCompanyProfileCommand(
                @NotNull String industryCode,
                Optional<Integer> employeeCount,
                Optional<String> missionStatement,
                Optional<String> targetMarket,
                Optional<Set<String>> keyProducts,
                Optional<Set<String>> competitorUrls,
                Optional<SocialMediaHandles> socialMediaHandles,
                Optional<Integer> foundingYear
        ) {
            public UpdateCompanyProfileCommand {
                if (employeeCount == null) {
                    employeeCount = Optional.empty();
                }
                if (missionStatement == null) {
                    missionStatement = Optional.empty();
                }
                if (targetMarket == null) {
                    targetMarket = Optional.empty();
                }
                if (keyProducts == null) {
                    keyProducts = Optional.empty();
                }

                if (competitorUrls == null) {
                    competitorUrls = Optional.empty();
                }

                if (socialMediaHandles == null) {
                    socialMediaHandles = Optional.empty();
                }
                if (foundingYear == null) {
                    foundingYear = Optional.empty();
                }
            }
        }
    }

    public record ActivateCompanyCommand(
            @NotNull CustomerCompanyId id,
            String activationNotes
    ) {
        public static ActivateCompanyCommand from(String id, String activationNotes) {
            return new ActivateCompanyCommand(new CustomerCompanyId(id), activationNotes);
        }
    }

    @Builder
    public record BlockCompanyCommand(
            @NotNull CustomerCompanyId id,
            @NotNull String reason
    ) {
    }

    @Builder
    public record DeactivateCompanyCommand(
            @NotNull CustomerCompanyId id,
            String reason
    ) {
    }

    @Builder
    public record UpgradeToEnterpriseCommand(
            @NotNull CustomerCompanyId id,
            @NotNull BigDecimal annualRevenue,
            String currency
    ) {
    }

    @Builder
    public record SignContractCommand(
            @NotNull CustomerCompanyId id,
            @NotNull String contractId,
            @NotNull BigDecimal monthlyFee,
            @NotNull ContractDetails.ContractType contractType,
            Integer durationMonths,
            boolean autoRenewal
    ) {
    }

    @Builder
    public record DeleteCompanyCommand(
            @NotNull CustomerCompanyId id
    ) {
    }

    @Builder
    public record AddContactPersonCommand(
            @NotNull CustomerCompanyId companyId,
            @NotNull String firstName,
            @NotNull String lastName,
            @Email String email,
            String phone,
            String position,
            String department,
            boolean isDecisionMaker
    ) {
    }

    @Builder
    public record RemoveContactPersonCommand(
            @NotNull CustomerCompanyId companyId,
            @NotNull @Email String email
    ) {
    }

}