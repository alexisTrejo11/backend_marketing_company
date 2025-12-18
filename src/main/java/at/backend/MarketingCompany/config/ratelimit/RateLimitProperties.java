package at.backend.MarketingCompany.config.ratelimit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

	private GlobalConfig global = new GlobalConfig();
	private DefaultConfig defaults = new DefaultConfig();
	private Map<String, EndpointConfig> endpoints = new HashMap<>();

	@Data
	public static class GlobalConfig {
		private boolean enabled = true;
		private int maxRequests = 1000;
		private int durationHours = 1;
	}

	@Data
	public static class DefaultConfig {
		private int maxRequests = 100;
		private int durationSeconds = 60;
	}

	@Data
	public static class EndpointConfig {
		private int maxRequests;
		private int durationSeconds;
	}
}