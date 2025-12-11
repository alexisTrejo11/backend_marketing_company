package at.backend.MarketingCompany.customer.domain.valueobject;

import lombok.Builder;

import java.util.Set;

@Builder
public record CompanyProfile(Industry industry, CompanySize size, AnnualRevenue revenue, String missionStatement,
                             String targetMarket, Set<String> competitorUrls, SocialMediaHandles socialMediaHandles,
                             Set<String> keyProducts, Integer foundingYear) {

    public boolean isStartup() {
        return foundingYear != null && (java.time.Year.now().getValue() - foundingYear) <= 5;
    }

    public boolean isPublicCompany() {
        if (socialMediaHandles == null || revenue == null) {
            return false;
        }

        return size.isEnterprise() && revenue.isHighValue();
    }

    public boolean competesWith(String competitor) {
        return competitorUrls != null && competitorUrls.stream().anyMatch(url -> url.toLowerCase().contains(competitor.toLowerCase()));
    }

    public boolean hasEstablishedPresence() {
        return foundingYear != null && (java.time.Year.now().getValue() - foundingYear) >= 10;
    }
}