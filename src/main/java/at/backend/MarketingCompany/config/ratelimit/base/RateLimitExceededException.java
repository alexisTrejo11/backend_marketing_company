package at.backend.MarketingCompany.config.ratelimit.base;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}