package at.backend.MarketingCompany.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import at.backend.MarketingCompany.config.cors.CorsConfig;

/**
 * Main security configuration for the application.
 *
 * <p>
 * Configures authentication, authorization, CORS, CSRF, and session management.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final GraphQLAccessDeniedHandler graphQLAccessDeniedHandler;
  private final GraphQLAuthenticationEntryPoint graphQLAuthenticationEntryPoint;
  private final CorsConfig corsConfig;

  @Autowired
  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      GraphQLAccessDeniedHandler graphQLAccessDeniedHandler,
      GraphQLAuthenticationEntryPoint graphQLAuthenticationEntryPoint,
      CorsConfig corsConfig) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.graphQLAccessDeniedHandler = graphQLAccessDeniedHandler;
    this.graphQLAuthenticationEntryPoint = graphQLAuthenticationEntryPoint;
    this.corsConfig = corsConfig;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Configurar CORS
        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))

        // Deshabilitar CSRF para APIs REST/GraphQL
        .csrf(AbstractHttpConfigurer::disable)

        // Configurar manejo de sesiones como stateless
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // Configurar autorización de endpoints
        .authorizeHttpRequests(authorize -> authorize
            // Endpoints públicos de autenticación
            .requestMatchers(HttpMethod.POST,
                "/api/auth/signup",
                "/api/auth/login",
                "/api/auth/refresh",
                "/api/auth/validate-token")
            .permitAll()

            // GraphQL endpoint - manejado por directivas
            .requestMatchers(HttpMethod.POST, "/graphql").permitAll()

            // UIs de GraphQL (solo desarrollo)
            .requestMatchers(
                "/graphiql",
                "/graphiql/**",
                "/voyager",
                "/voyager/**",
                "/playground",
                "/playground/**")
            .hasRole("DEVELOPER")

            // Endpoints de health y métricas
            .requestMatchers(
                "/health",
                "/health/**",
                "/actuator/health",
                "/actuator/health/**",
                "/actuator/info",
                "/actuator/metrics",
                "/actuator/prometheus")
            .permitAll()

            // WebJars y recursos estáticos
            .requestMatchers(
                "/webjars/**",
                "/favicon.ico",
                "/error",
                "/robots.txt")
            .permitAll()

            // Todos los demás endpoints requieren autenticación
            .anyRequest().authenticated())

        // Configurar manejo de excepciones para GraphQL
        .exceptionHandling(exceptions -> exceptions
            .authenticationEntryPoint(graphQLAuthenticationEntryPoint)
            .accessDeniedHandler(graphQLAccessDeniedHandler))

        // Agregar filtro JWT
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
