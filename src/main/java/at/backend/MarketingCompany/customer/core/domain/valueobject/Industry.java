package at.backend.MarketingCompany.customer.core.domain.valueobject;

import java.util.Set;

public record Industry(String code, String name, String sector) {

    private static final Set<Industry> COMMON_INDUSTRIES = Set.of(
            new Industry("TECH", "Technology", "Services"),
            new Industry("FINA", "Finance", "Financial"),
            new Industry("HEAL", "Healthcare", "Services"),
            new Industry("MANU", "Manufacturing", "Industrial"),
            new Industry("RETA", "Retail", "Commercial"),
            new Industry("EDUC", "Education", "Services"),
            new Industry("CONS", "Consulting", "Professional"),
            new Industry("MARK", "Marketing", "Services"),
            new Industry("REAL", "Real Estate", "Commercial"),
            new Industry("UNKN", "Unknown", "General")
    );

    public static Industry fromCode(String code) {
        return COMMON_INDUSTRIES.stream()
                .filter(industry -> industry.code().equalsIgnoreCase(code))
                .findFirst()
                .orElse(new Industry("UNKN", "Unknown", "General"));
    }

    public boolean isInSector(String targetSector) {
        return sector.equalsIgnoreCase(targetSector);
    }
}