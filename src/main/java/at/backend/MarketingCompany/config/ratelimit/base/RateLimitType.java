package at.backend.MarketingCompany.config.ratelimit.base;

public enum RateLimitType {
    IP_BASED,
    USER_BASED,
		GLOBAL,
    API_KEY_BASED,
    CUSTOM
}