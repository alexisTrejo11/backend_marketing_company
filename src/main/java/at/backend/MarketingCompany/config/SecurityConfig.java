package at.backend.MarketingCompany.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Main security configuration for the application.
 *
 * <p>Configures authentication, authorization, CORS, CSRF, and session management.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CorsConfigurationSource corsConfigurationSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.anyRequest().permitAll()
				)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable);

		return http.build();
	}
}
