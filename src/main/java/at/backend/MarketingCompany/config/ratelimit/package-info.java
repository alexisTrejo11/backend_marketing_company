/**
 * Rate limiting infrastructure for GraphQL operations.
 *
 * <h2>Overview</h2>
 * This package provides a comprehensive rate limiting solution for GraphQL operations with two levels of protection:
 * <ul>
 *   <li><b>Global Rate Limiting:</b> Applies to all GraphQL requests per IP address</li>
 *   <li><b>Operation-specific Rate Limiting:</b> Fine-grained control per GraphQL operation (query/mutation)</li>
 * </ul>
 *
 * <h2>Architecture</h2>
 * The rate limiting system uses Redis for distributed rate limiting and consists of:
 * <ul>
 *   <li>{@link at.backend.MarketingCompany.config.ratelimit.RedisRateLimiter} - Core rate limiting logic using Redis</li>
 *   <li>{@link at.backend.MarketingCompany.config.ratelimit.GlobalGraphQlRateLimitInterceptor} - Global rate limiter (Order 1)</li>
 *   <li>{@link at.backend.MarketingCompany.config.ratelimit.GraphQLOperationRateLimitInterceptor} - Operation-specific limiter (Order 2)</li>
 *   <li>{@link at.backend.MarketingCompany.config.ratelimit.GraphQLRateLimitRegistry} - Maps operations to their rate limit configurations</li>
 *   <li>{@link at.backend.MarketingCompany.config.ratelimit.RateLimitProperties} - Configuration properties from application.yml</li>
 * </ul>
 *
 * <h2>Usage</h2>
 *
 * <h3>1. Annotate GraphQL Operations</h3>
 * Add the {@code @GraphQLRateLimit} annotation to any {@code @MutationMapping} or {@code @QueryMapping} method:
 *
 * <pre>{@code
 * @MutationMapping
 * @GraphQLRateLimit("sensitive")  // Uses 'sensitive' profile from config
 * public AuthResponse signUp(@Valid @Argument SignUpInput input) {
 *     // Implementation
 * }
 *
 * @QueryMapping
 * @GraphQLRateLimit(value = "standard", type = RateLimitType.USER_BASED)
 * public User getUser(@Argument String userId) {
 *     // Implementation
 * }
 * }</pre>
 *
 * <h3>2. Configure Rate Limits</h3>
 * Define rate limit profiles in {@code application.yml}:
 *
 * <pre>
 * app:
 *   rate-limit:
 *     global:
 *       enabled: true
 *       max-requests: 5000
 *       duration-hours: 1
 *
 *     defaults:
 *       max-requests: 100
 *       duration-seconds: 60
 *
 *     endpoints:
 *       sensitive:
 *         max-requests: 5
 *         duration-seconds: 60
 *
 *       standard:
 *         max-requests: 100
 *         duration-seconds: 60
 *
 *       strict:
 *         max-requests: 10
 *         duration-seconds: 300
 * </pre>
 *
 * <h2>Rate Limit Types</h2>
 * The {@link at.backend.MarketingCompany.config.ratelimit.base.RateLimitType} enum defines the scope:
 * <ul>
 *   <li><b>IP_BASED</b> (default): Limits per client IP address</li>
 *   <li><b>USER_BASED</b>: Limits per authenticated user ID</li>
 *   <li><b>GLOBAL</b>: Single limit shared across all clients</li>
 * </ul>
 *
 * <h2>Execution Flow</h2>
 * <ol>
 *   <li>Request arrives at GraphQL endpoint</li>
 *   <li>{@code GlobalGraphQlRateLimitInterceptor} checks global rate limit (if enabled)</li>
 *   <li>If global limit exceeded → reject with 429-equivalent error</li>
 *   <li>{@code GraphQLOperationRateLimitInterceptor} checks operation-specific limit</li>
 *   <li>If operation limit exceeded → reject with descriptive error</li>
 *   <li>Request proceeds to controller method</li>
 *   <li>Response headers include rate limit information</li>
 * </ol>
 *
 * <h2>Response Headers</h2>
 * Both interceptors add informative headers to responses:
 *
 * <h3>Global Rate Limit Headers</h3>
 * <ul>
 *   <li>{@code X-RateLimit-Global-Limit}: Maximum requests allowed in the time window</li>
 *   <li>{@code X-RateLimit-Global-Remaining}: Requests remaining in current window</li>
 *   <li>{@code X-RateLimit-Global-Reset}: Seconds until the limit resets</li>
 *   <li>{@code X-RateLimit-Global-Window}: Duration of the rate limit window</li>
 * </ul>
 *
 * <h3>Operation-specific Rate Limit Headers</h3>
 * <ul>
 *   <li>{@code X-RateLimit-Operation-Limit}: Maximum requests for this operation</li>
 *   <li>{@code X-RateLimit-Operation-Remaining}: Requests remaining</li>
 *   <li>{@code X-RateLimit-Operation-Reset}: Seconds until reset</li>
 *   <li>{@code X-RateLimit-Operation-Window}: Duration of the window</li>
 *   <li>{@code X-RateLimit-Operation-Name}: Name of the GraphQL operation</li>
 *   <li>{@code X-RateLimit-Operation-Profile}: Configuration profile used</li>
 * </ul>
 *
 * <h2>Redis Key Structure</h2>
 * Rate limit counters are stored in Redis with structured keys:
 * <ul>
 *   <li>Global: {@code rate_limit:global:ip:{ip_address}}</li>
 *   <li>IP-based: {@code rate_limit:graphql:{operation}:ip:{ip_address}}</li>
 *   <li>User-based: {@code rate_limit:graphql:{operation}:user:{user_id}}</li>
 *   <li>Operation global: {@code rate_limit:graphql:{operation}}</li>
 * </ul>
 *
 * <h2>Error Handling</h2>
 * When rate limits are exceeded:
 * <ul>
 *   <li>A {@link java.lang.RuntimeException} is thrown with a descriptive message</li>
 *   <li>The message includes the operation name and retry time</li>
 *   <li>Example: "Rate limit exceeded for operation 'signUp'. Try again in 45 seconds."</li>
 *   <li>GraphQL error handling infrastructure converts this to a proper GraphQL error response</li>
 * </ul>
 *
 * <h2>Best Practices</h2>
 * <ol>
 *   <li><b>Sensitive Operations:</b> Use strict limits (5-10 req/min) for authentication, registration, password reset</li>
 *   <li><b>Read Operations:</b> More lenient limits (100-500 req/min) for queries</li>
 *   <li><b>Write Operations:</b> Moderate limits (20-50 req/min) for mutations</li>
 *   <li><b>Public Endpoints:</b> Stricter limits for unauthenticated operations</li>
 *   <li><b>Global Protection:</b> Keep global limits high (1000-5000 req/hour) as a safety net</li>
 * </ol>
 *
 * <h2>Monitoring</h2>
 * The system logs all rate limit checks with detailed information:
 * <ul>
 *   <li>Operation name and profile used</li>
 *   <li>Rate limit key and current count</li>
 *   <li>Remaining requests and reset time</li>
 *   <li>Whether the limit was exceeded</li>
 * </ul>
 *
 * Example logs:
 * <pre>
 * INFO  - === OPERATION RATE LIMIT CHECK: SignUp ===
 * INFO  - Rate limit found for SignUp: profile=sensitive, type=IP_BASED
 * INFO  - Using profile 'sensitive': 5 req/60s
 * INFO  - Rate limit key: graphql:SignUp:ip:192.168.1.100
 * INFO  - Rate limit OK for SignUp - Remaining: 4, Reset in: 60s
 *
 * WARN  - RATE LIMIT EXCEEDED for SignUp - Remaining: 0, Reset in: 45s
 * </pre>
 *
 * <h2>Testing</h2>
 * To test rate limiting:
 * <ol>
 *   <li>Configure a low limit (e.g., 3 requests per minute)</li>
 *   <li>Make multiple rapid requests to the same operation</li>
 *   <li>Verify that after the limit, requests are rejected</li>
 *   <li>Check Redis for the rate limit keys: {@code redis-cli KEYS "rate_limit:*"}</li>
 *   <li>Verify response headers contain rate limit information</li>
 * </ol>
 *
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li>Redis operations are fast (sub-millisecond) and non-blocking</li>
 *   <li>Rate limit checks add minimal latency (typically &lt; 5ms)</li>
 *   <li>Uses Redis TTL for automatic cleanup of expired counters</li>
 *   <li>No database queries needed for rate limiting</li>
 * </ul>
 *
 * <h2>Troubleshooting</h2>
 * <dl>
 *   <dt>Rate limits not working?</dt>
 *   <dd>
 *     <ul>
 *       <li>Check Redis is running: {@code redis-cli ping}</li>
 *       <li>Verify configuration in application.yml</li>
 *       <li>Check logs for "REGISTRY INITIALIZED" message</li>
 *       <li>Ensure operation name matches (case-sensitive)</li>
 *     </ul>
 *   </dd>
 *
 *   <dt>Wrong limits being applied?</dt>
 *   <dd>
 *     <ul>
 *       <li>Verify profile name in @GraphQLRateLimit matches config</li>
 *       <li>Check logs for "Using profile" message to see which config is used</li>
 *       <li>If profile not found, defaults are used</li>
 *     </ul>
 *   </dd>
 *
 *   <dt>Headers not appearing?</dt>
 *   <dd>
 *     <ul>
 *       <li>Headers are only added after successful rate limit check</li>
 *       <li>Check if GraphQL error handling strips headers</li>
 *       <li>Verify interceptor order (Global=1, Operation=2)</li>
 *     </ul>
 *   </dd>
 * </dl>
 *
 * @see at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit
 * @see at.backend.MarketingCompany.config.ratelimit.base.RateLimitType
 * @see at.backend.MarketingCompany.config.ratelimit.RedisRateLimiter
 * @see at.backend.MarketingCompany.config.ratelimit.RateLimitProperties
 *
 * @author Marketing Company Development Team
 * @version 2.0.0
 * @since 2.0.0
 */
package at.backend.MarketingCompany.config.ratelimit;