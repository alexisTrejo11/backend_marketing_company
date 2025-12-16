package at.backend.MarketingCompany.customer.adapter.input.web.dto;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanySize;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CompanyStatus;
import at.backend.MarketingCompany.customer.core.domain.valueobject.Industry;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Builder
public record CompanyResponse(
    String id,
    String companyName,
    String legalName,
    String taxId,
    String website,
    Integer foundingYear,
    
    IndustryResponse industry,
    CompanySize size,
    Integer employeeCount,
    RevenueResponse annualRevenue,
    
    CompanyStatus status,
    boolean isPublicCompany,
    boolean isStartup,
    
    String targetMarket,
    String missionStatement,
    Set<String> keyProducts,
    Set<String> competitorUrls,
    
    Set<ContactPersonResponse> contactPersons,
    ContractDetailsResponse contractDetails,
    BillingInfoResponse billingInfo,
    SocialMediaResponse socialMedia,
    
    Integer opportunityCount,
    Integer interactionCount,

    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    
    @Builder
    public record IndustryResponse(
        String code,
        String name,
        String sector
    ) {

        public static IndustryResponse fromDomain(Industry industry) {
            if (industry == null) {
                return null;
            }

            return IndustryResponse.builder()
                .code(industry.code())
                .name(industry.name())
                .sector(industry.sector())
                .build();
        }
    }
    
    @Builder
    public record RevenueResponse(
        BigDecimal amount,
        String currency,
        String range
    ) {}
    
    @Builder
    public record ContactPersonResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String position,
        String department,
        boolean isDecisionMaker,
        boolean isPrimaryContact
    ) {}
    
    @Builder
    public record ContractDetailsResponse(
        String contractId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal monthlyFee,
        String type,
        boolean autoRenewal,
        boolean isActive,
        boolean isExpiringSoon
    ) {}
    
    @Builder
    public record BillingInfoResponse(
        String billingEmail,
        String preferredPaymentMethod,
        String billingAddress,
        boolean approvedCredit
    ) {}
    
    @Builder
    public record SocialMediaResponse(
        String linkedinUrl,
        String twitterHandle,
        String facebookUrl,
        String instagramHandle
    ) {}
}

