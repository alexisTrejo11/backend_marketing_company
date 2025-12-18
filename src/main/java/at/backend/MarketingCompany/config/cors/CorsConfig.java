package at.backend.MarketingCompany.config.cors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) configuration for the application.
 * 
 * <p>Configures which origins, methods, and headers are allowed for cross-origin requests.
 * This is essential for web applications that need to access the API from different domains.</p>
 * 
 * @see CorsProperties
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class CorsConfig {

    private final CorsProperties corsProperties;

    /**
     * Configures CORS settings for the application.
     * 
     * <p>Settings are loaded from application.yml under 'app.cors' prefix.
     * Different configurations can be applied per environment (dev, staging, prod).</p>
     * 
     * @return CorsConfigurationSource with the configured CORS rules
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS with {} allowed origins", corsProperties.getAllowedOrigins().size());
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed Origins
        if (corsProperties.getAllowedOrigins().contains("*")) {
            log.warn("CORS configured to allow ALL origins (*). This should only be used in development!");
            configuration.addAllowedOriginPattern("*");
        } else {
            corsProperties.getAllowedOrigins().forEach(origin -> {
                configuration.addAllowedOrigin(origin);
                log.debug("Added allowed origin: {}", origin);
            });
        }
        
        // Allowed Methods
        if (corsProperties.getAllowedMethods().contains("*")) {
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        } else {
            configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        }
        log.info("Allowed HTTP methods: {}", configuration.getAllowedMethods());
        
        // Allowed Headers
        if (corsProperties.getAllowedHeaders().contains("*")) {
            configuration.addAllowedHeader("*");
        } else {
            configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        }
        
        // Exposed Headers - headers that browser can access in response
        if (!corsProperties.getExposedHeaders().isEmpty()) {
            configuration.setExposedHeaders(corsProperties.getExposedHeaders());
            log.debug("Exposed headers: {}", corsProperties.getExposedHeaders());
        }
        
        // Allow Credentials (cookies, authorization headers)
        configuration.setAllowCredentials(corsProperties.isAllowCredentials());
        if (corsProperties.isAllowCredentials()) {
            log.info("CORS credentials are ALLOWED (cookies/auth headers can be sent)");
        }
        
        // Max Age - how long browsers can cache preflight requests
        configuration.setMaxAge(corsProperties.getMaxAge());
        log.info("CORS preflight cache max age: {} seconds", corsProperties.getMaxAge());
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS configuration successfully initialized");
        return source;
    }
}