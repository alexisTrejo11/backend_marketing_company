/**
 * Cross-Origin Resource Sharing (CORS) configuration for the application.
 *
 * <h2>Overview</h2>
 * <p>
 * CORS is a security feature implemented by browsers that restricts web pages
 * from making
 * requests to a different domain than the one serving the web page. This
 * package provides
 * flexible CORS configuration to allow legitimate cross-origin requests while
 * maintaining security.
 * </p>
 *
 * <h2>Why CORS?</h2>
 * <p>
 * Modern web applications often have:
 * <ul>
 * <li><b>Frontend:</b> React/Vue/Angular app hosted on
 * {@code https://app.example.com}</li>
 * <li><b>Backend API:</b> Spring Boot API hosted on
 * {@code https://api.example.com}</li>
 * </ul>
 * Without CORS configuration, browsers would block the frontend from accessing
 * the API.
 * </p>
 *
 * <h2>Architecture</h2>
 * <ul>
 * <li>{@link at.backend.MarketingCompany.config.cors.CorsConfig} - Main CORS
 * configuration</li>
 * <li>{@link at.backend.MarketingCompany.config.cors.CorsProperties} - CORS
 * settings from application.yml</li>
 * </ul>
 *
 * <h2>Configuration</h2>
 *
 * <h3>Basic Setup (application.yml)</h3>
 * 
 * <pre>
 * app:
 *   cors:
 *     allowed-origins:
 *       - https://app.example.com
 *       - https://admin.example.com
 *
 *     allowed-methods:
 *       - GET
 *       - POST
 *       - PUT
 *       - DELETE
 *
 *     allowed-headers:
 *       - "*"
 *
 *     exposed-headers:
 *       - X-RateLimit-Remaining
 *       - X-Total-Count
 *
 *     allow-credentials: true
 *     max-age: 3600
 * </pre>
 *
 * <h3>Development vs Production</h3>
 * <table border="1">
 * <tr>
 * <th>Setting</th>
 * <th>Development</th>
 * <th>Production</th>
 * </tr>
 * <tr>
 * <td>allowed-origins</td>
 * <td>http://localhost:3000<br>
 * http://localhost:5173</td>
 * <td>https://app.example.com<br>
 * (specific domains only)</td>
 * </tr>
 * <tr>
 * <td>allow-credentials</td>
 * <td>true</td>
 * <td>true (if using cookies/JWT)</td>
 * </tr>
 * <tr>
 * <td>max-age</td>
 * <td>600 (10 min)</td>
 * <td>3600 (1 hour)</td>
 * </tr>
 * </table>
 *
 * <h2>CORS Flow</h2>
 *
 * <h3>Simple Requests</h3>
 * <p>
 * For simple GET/POST requests with standard headers:
 * </p>
 * <ol>
 * <li>Browser sends request with Origin header</li>
 * <li>Server responds with Access-Control-Allow-Origin header</li>
 * <li>Browser allows JavaScript to access response if origins match</li>
 * </ol>
 *
 * <h3>Preflight Requests</h3>
 * <p>
 * For complex requests (PUT/DELETE, custom headers, credentials):
 * </p>
 * <ol>
 * <li>Browser sends OPTIONS request (preflight)</li>
 * <li>Server responds with allowed methods, headers, origins</li>
 * <li>If approved, browser sends actual request</li>
 * <li>Server responds with data</li>
 * </ol>
 *
 * <b>Example preflight:</b>
 * 
 * <pre>
 * OPTIONS /graphql HTTP/1.1
 * Origin: https://app.example.com
 * Access-Control-Request-Method: POST
 * Access-Control-Request-Headers: Authorization, Content-Type
 *
 * HTTP/1.1 200 OK
 * Access-Control-Allow-Origin: https://app.example.com
 * Access-Control-Allow-Methods: GET, POST, PUT, DELETE
 * Access-Control-Allow-Headers: Authorization, Content-Type
 * Access-Control-Max-Age: 3600
 * </pre>
 *
 * <h2>Security Considerations</h2>
 *
 * <h3>DO NOT use "*" for origins in production</h3>
 * 
 * <pre>
 * # ❌ INSECURE - allows any website to access your API
 * allowed-origins:
 *   - "*"
 *
 * # ✅ SECURE - only specific domains can access
 * allowed-origins:
 *   - https://app.example.com
 *   - https://admin.example.com
 * </pre>
 *
 * <h3>Credentials + Wildcard Origins</h3>
 * <p>
 * You <b>CANNOT</b> use {@code allowCredentials: true} with
 * {@code allowedOrigins: ["*"]}.
 * Browsers will reject this configuration for security reasons.
 * </p>
 *
 * <h3>Exposed Headers</h3>
 * <p>
 * By default, browsers only expose these headers to JavaScript:
 * <ul>
 * <li>Cache-Control</li>
 * <li>Content-Language</li>
 * <li>Content-Type</li>
 * <li>Expires</li>
 * <li>Last-Modified</li>
 * <li>Pragma</li>
 * </ul>
 * Custom headers (like rate limit headers) must be explicitly exposed.
 * </p>
 *
 * <h2>Testing CORS</h2>
 *
 * <h3>Using curl</h3>
 * 
 * <pre>
 * # Test preflight request
 * curl -X OPTIONS http://localhost:8080/graphql \
 *   -H "Origin: http://localhost:3000" \
 *   -H "Access-Control-Request-Method: POST" \
 *   -H "Access-Control-Request-Headers: Content-Type" \
 *   -v
 *
 * # Should return:
 * # Access-Control-Allow-Origin: http://localhost:3000
 * # Access-Control-Allow-Methods: GET, POST, PUT, DELETE
 * # Access-Control-Max-Age: 3600
 * </pre>
 *
 * <h3>Using Browser DevTools</h3>
 * <ol>
 * <li>Open browser DevTools (F12)</li>
 * <li>Go to Network tab</li>
 * <li>Make a request from your frontend</li>
 * <li>Look for OPTIONS request (preflight)</li>
 * <li>Check response headers for Access-Control-*</li>
 * </ol>
 *
 * <h3>Using JavaScript</h3>
 * 
 * <pre>
 * // Frontend code (React/Vue/Angular)
 * fetch('http://localhost:8080/graphql', {
 *   method: 'POST',
 *   credentials: 'include',  // Send cookies
 *   headers: {
 *     'Content-Type': 'application/json',
 *     'Authorization': 'Bearer ' + token
 *   },
 *   body: JSON.stringify({ query: '...' })
 * })
 * .then(response => {
 *   // Can now read exposed headers
 *   const remaining = response.headers.get('X-RateLimit-Remaining');
 *   console.log('Requests remaining:', remaining);
 * });
 * </pre>
 *
 * <h2>Common CORS Errors</h2>
 *
 * <dl>
 * <dt>"Access to fetch has been blocked by CORS policy"</dt>
 * <dd>
 * <b>Cause:</b> Origin not in allowed-origins list<br>
 * <b>Solution:</b> Add the origin to application.yml
 * </dd>
 *
 * <dt>"The 'Access-Control-Allow-Origin' header contains multiple values"</dt>
 * <dd>
 * <b>Cause:</b> CORS configured in multiple places (Spring Security +
 * WebMvcConfigurer)<br>
 * <b>Solution:</b> Configure CORS in only one place (use our CorsConfig)
 * </dd>
 *
 * <dt>"Credential is not supported if the CORS header
 * 'Access-Control-Allow-Origin' is '*'"</dt>
 * <dd>
 * <b>Cause:</b> Using allowCredentials=true with allowed-origins="*"<br>
 * <b>Solution:</b> Specify exact origins instead of wildcard
 * </dd>
 *
 * <dt>"Request header field Authorization is not allowed"</dt>
 * <dd>
 * <b>Cause:</b> Authorization not in allowed-headers<br>
 * <b>Solution:</b> Use allowed-headers: ["*"] or explicitly list Authorization
 * </dd>
 * </dl>
 *
 * <h2>Best Practices</h2>
 * <ol>
 * <li><b>Use specific origins in production</b> - Never use "*" wildcard</li>
 * <li><b>Enable credentials only if needed</b> - If not using cookies/auth
 * headers, set to false</li>
 * <li><b>Expose only necessary headers</b> - Don't expose sensitive
 * information</li>
 * <li><b>Set appropriate max-age</b> - Balance between performance and
 * security</li>
 * <li><b>Use HTTPS in production</b> - HTTP origins are less secure</li>
 * <li><b>Document allowed origins</b> - Keep a list of approved domains</li>
 * <li><b>Monitor CORS errors</b> - Log failed CORS requests for security
 * analysis</li>
 * </ol>
 *
 * <h2>Environment-Specific Configuration</h2>
 * <p>
 * Use Spring profiles to configure different CORS settings per environment:
 * </p>
 *
 * <pre>
 * # application-dev.yml
 * app:
 *   cors:
 *     allowed-origins:
 *       - http://localhost:3000
 *       - http://127.0.0.1:3000
 *
 * # application-prod.yml
 * app:
 *   cors:
 *     allowed-origins:
 *       - https://app.example.com
 *       - https://admin.example.com
 * </pre>
 *
 * <h2>Integration with GraphQL</h2>
 * <p>
 * CORS works seamlessly with GraphQL endpoints. All GraphQL requests (queries,
 * mutations, subscriptions)
 * respect CORS configuration. Rate limit headers are automatically exposed in
 * CORS responses.
 * </p>
 *
 * @see at.backend.MarketingCompany.config.cors.CorsConfig
 * @see at.backend.MarketingCompany.config.cors.CorsProperties
 * @see at.at.backend.MarketingCompany.config.security.SecurityConfig
 *
 * @author Alexis Trejo
 * @version 2.0.0
 * @since 2.0.0
 */
package at.backend.MarketingCompany.config.cors;
