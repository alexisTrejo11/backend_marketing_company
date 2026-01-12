package at.backend.MarketingCompany.customer.core.domain.valueobject;

import java.util.Set;

public enum CampaignType {
    EMAIL_MARKETING("Digital", 1500, Set.of("B2B", "B2C"), true),
    SOCIAL_MEDIA("Digital", 3000, Set.of("B2C", "Branding"), true),
    CONTENT_MARKETING("Digital", 5000, Set.of("B2B", "Education"), true),
    WEBINAR("Digital", 2000, Set.of("B2B", "Education"), true),
    PPC("Digital", 10000, Set.of("B2B", "B2C", "Lead Gen"), true),
    SEO("Digital", 8000, Set.of("B2B", "B2C", "Long-term"), true),
    REMARKETING("Digital", 4000, Set.of("B2B", "B2C", "Conversion"), true),

    DIRECT_MAIL("Traditional", 5000, Set.of("B2B", "Enterprise"), false),
    EVENT("Traditional", 15000, Set.of("B2B", "Networking"), false),
    TRADE_SHOW("Traditional", 25000, Set.of("B2B", "Enterprise"), false),
    PRINT("Traditional", 7000, Set.of("Local", "Branding"), false),

    ENTERPRISE_SALES("Enterprise", 50000, Set.of("B2B", "Enterprise"), true),
    ACCOUNT_BASED("Enterprise", 30000, Set.of("B2B", "Targeted"), true),
    PARTNER_MARKETING("Partnership", 20000, Set.of("B2B", "Channel"), true),
    REFERRAL_PROGRAM("Growth", 10000, Set.of("B2B", "B2C"), true),

    WHITEPAPER("Content", 15000, Set.of("B2B", "Enterprise"), true),
    CASE_STUDY("Content", 12000, Set.of("B2B", "Social Proof"), true),
    PRODUCT_DEMO("Sales", 8000, Set.of("B2B", "Conversion"), true),

    MULTI_CHANNEL("Omnichannel", 30000, Set.of("B2B", "B2C", "Enterprise"), true),
    INTEGRATED("Omnichannel", 40000, Set.of("Enterprise", "Strategic"), true);

    private final String category;
    private final double typicalBudget;
    private final Set<String> bestFor;
    private final boolean trackable;

    CampaignType(String category, double typicalBudget, Set<String> bestFor, boolean trackable) {
        this.category = category;
        this.typicalBudget = typicalBudget;
        this.bestFor = bestFor;
        this.trackable = trackable;
    }

    public String getCategory() { return category; }
    public double getTypicalBudget() { return typicalBudget; }
    public Set<String> getBestFor() { return bestFor; }
    public boolean isTrackable() { return trackable; }
    public boolean isDigital() { return "Digital".equals(category); }
    public boolean isTraditional() { return "Traditional".equals(category); }
    public boolean isEnterprise() { return "Enterprise".equals(category); }

    public boolean isSuitableForB2B() {
        return bestFor.contains("B2B") || bestFor.contains("Enterprise");
    }

    public boolean isSuitableForEnterprise() {
        return bestFor.contains("Enterprise");
    }

    public boolean isSuitableForStartup() {
        return typicalBudget <= 10000 && !bestFor.contains("Enterprise");
    }

    public static Set<CampaignType> getEnterpriseCampaigns() {
        return Set.of(ENTERPRISE_SALES, ACCOUNT_BASED, TRADE_SHOW, WHITEPAPER, INTEGRATED);
    }

    public static Set<CampaignType> getDigitalCampaigns() {
        return Set.of(EMAIL_MARKETING, SOCIAL_MEDIA, CONTENT_MARKETING,
                WEBINAR, PPC, SEO, REMARKETING);
    }

    public static Set<CampaignType> getCampaignsForStartups() {
        return Set.of(EMAIL_MARKETING, SOCIAL_MEDIA, CONTENT_MARKETING,
                REFERRAL_PROGRAM, PRODUCT_DEMO);
    }
}