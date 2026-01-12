/**
 * Core annotations and types for the rate limiting system.
 *
 * <h2>Overview</h2>
 * This package contains the foundational components used throughout the rate limiting infrastructure:
 * <ul>
 *   <li>Annotation for marking rate-limited operations</li>
 *   <li>Enum defining rate limit scope types</li>
 *   <li>Exception for rate limit violations</li>
 * </ul>
 *
 * <h2>Components</h2>
 *
 * <h3>{@link at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit}</h3>
 * <p>
 * Method-level annotation for declaring rate limits on GraphQL operations.
 * Must be applied to methods annotated with {@code @MutationMapping} or {@code @QueryMapping}.
 * </p>
 *
 * <b>Attributes:</b>
 * <ul>
 *   <li>{@code value}: The configuration profile name (e.g., "sensitive", "standard")</li>
 *   <li>{@code type}: The rate limit scope ({@code IP_BASED}, {@code USER_BASED}, or {@code GLOBAL})</li>
 * </ul>
 *
 * <b>Example:</b>
 * <pre>{@code
 * @MutationMapping
 * @GraphQLRateLimit("sensitive")
 * public AuthResponse signUp(@Argument SignUpInput input) {
 *     // Applies rate limit using "sensitive" profile
 *     // Defaults to IP_BASED type
 * }
 *
 * @QueryMapping
 * @GraphQLRateLimit(value = "standard", type = RateLimitType.USER_BASED)
 * public List<Order> getMyOrders() {
 *     // Applies rate limit per authenticated user
 * }
 * }</pre>
 *
 * <h3>{@link at.backend.MarketingCompany.config.ratelimit.base.RateLimitType}</h3>
 * <p>
 * Enum defining the scope of rate limiting. Determines how clients are identified and grouped for rate limiting.
 * </p>
 *
 * <b>Types:</b>
 * <dl>
 *   <dt><b>IP_BASED</b> (default)</dt>
 *   <dd>
 *     Rate limit is applied per client IP address. Best for public endpoints and anonymous access.
 *     Uses X-Forwarded-For header if present, otherwise uses direct IP.
 *     <br><b>Use case:</b> Login, registration, public API endpoints
 *   </dd>
 *
 *   <dt><b>USER_BASED</b></dt>
 *   <dd>
 *     Rate limit is applied per authenticated user ID. Best for authenticated operations.
 *     Requires user identification from authentication context (JWT token, session, etc.).
 *     <br><b>Use case:</b> User-specific actions, profile updates, data modifications
 *   </dd>
 *
 *   <dt><b>GLOBAL</b></dt>
 *   <dd>
 *     Single rate limit shared across all clients. Best for resource-intensive operations.
 *     All users compete for the same quota.
 *     <br><b>Use case:</b> Heavy computation, third-party API calls, system-wide limits
 *   </dd>
 * </dl>
 *
 * <b>Example:</b>
 * <pre>{@code
 * // IP-based: Different IPs get separate quotas
 * @GraphQLRateLimit(value = "sensitive", type = RateLimitType.IP_BASED)
 * public AuthResponse login(@Argument LoginInput input) { ... }
 *
 * // User-based: Each user has their own quota
 * @GraphQLRateLimit(value = "standard", type = RateLimitType.USER_BASED)
 * public Profile updateProfile(@Argument ProfileInput input) { ... }
 *
 * // Global: All users share one quota
 * @GraphQLRateLimit(value = "strict", type = RateLimitType.GLOBAL)
 * public Report generateExpensiveReport() { ... }
 * }</pre>
 *
 * <h3>{@link at.backend.MarketingCompany.config.ratelimit.base.RateLimitExceededException}</h3>
 * <p>
 * Runtime exception thrown when a rate limit is exceeded. Contains a descriptive message
 * indicating the operation that was rate-limited and when the client can retry.
 * </p>
 *
 * <b>Message format:</b>
 * <pre>
 * "Rate limit exceeded for operation 'signUp'. Try again in 45 seconds."
 * </pre>
 *
 * <b>Handling:</b>
 * <p>
 * This exception is typically caught by GraphQL error handling infrastructure and converted
 * to a GraphQL error response. Custom error handlers can intercept this to provide
 * specialized rate limit error responses.
 * </p>
 *
 * <b>Example:</b>
 * <pre>{@code
 * @ControllerAdvice
 * public class GraphQLErrorHandler {
 *
 *     @ExceptionHandler(RateLimitExceededException.class)
 *     public GraphQLError handleRateLimit(RateLimitExceededException ex) {
 *         return GraphQLError.newError()
 *             .errorType(ErrorType.RATE_LIMITED)
 *             .message(ex.getMessage())
 *             .extensions(Map.of(
 *                 "category", "RATE_LIMIT",
 *                 "retryAfter", extractRetrySeconds(ex.getMessage())
 *             ))
 *             .build();
 *     }
 * }
 * }</pre>
 *
 * <h2>Configuration Integration</h2>
 * <p>
 * The annotation's {@code value} attribute references profiles defined in {@code application.yml}.
 * If the profile doesn't exist, the system falls back to default configuration.
 * </p>
 *
 * <b>Configuration example:</b>
 * <pre>
 * app:
 *   rate-limit:
 *     endpoints:
 *       sensitive:           # Referenced by @GraphQLRateLimit("sensitive")
 *         max-requests: 5
 *         duration-seconds: 60
 *
 *       standard:            # Referenced by @GraphQLRateLimit("standard")
 *         max-requests: 100
 *         duration-seconds: 60
 * </pre>
 *
 * <h2>Design Decisions</h2>
 * <dl>
 *   <dt>Why annotation-based?</dt>
 *   <dd>
 *     Declarative configuration keeps rate limiting logic separate from business logic.
 *     Easy to see which operations are rate-limited at a glance.
 *   </dd>
 *
 *   <dt>Why enum for types?</dt>
 *   <dd>
 *     Type-safe and prevents configuration errors. Enum provides clear, documented options.
 *   </dd>
 *
 *   <dt>Why runtime exception?</dt>
 *   <dd>
 *     Rate limiting is cross-cutting and shouldn't pollute method signatures.
 *     GraphQL error handling naturally catches runtime exceptions.
 *   </dd>
 * </dl>
 *
 * @see at.backend.MarketingCompany.config.ratelimit
 * @author Marketing Company Development Team
 * @version 2.0.0
 * @since 2.0.0
 */
package at.backend.MarketingCompany.config.ratelimit.base;