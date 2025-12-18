package at.backend.MarketingCompany.config.ratelimit.base;

import at.backend.MarketingCompany.config.ratelimit.base.RateLimitType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphQLRateLimit {
	String value() default "standard";
	RateLimitType type() default RateLimitType.IP_BASED;
}