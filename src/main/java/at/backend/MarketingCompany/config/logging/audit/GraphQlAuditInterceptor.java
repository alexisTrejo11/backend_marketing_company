package at.backend.MarketingCompany.config.logging.audit;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GraphQlAuditInterceptor implements WebGraphQlInterceptor {

	private final AuditLogger auditLogger;
	private final String serviceName;

	public GraphQlAuditInterceptor(AuditLogger auditLogger) {
		this.auditLogger = auditLogger;
		this.serviceName = System.getenv().getOrDefault("SPRING_APPLICATION_NAME", "marketing-service");
	}

	@Override
	public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
		long startTime = System.currentTimeMillis();

		return chain.next(request).doOnNext(response -> {
			try {
				long duration = System.currentTimeMillis() - startTime;

				// Identificación de la operación (ej: Login, SignUp)
				String operationName = request.getOperationName() != null ?
						request.getOperationName() : "AnonymousOperation";

				// Determinación del tipo de operación analizando el documento GraphQL
				String document = request.getDocument();
				String type = "QUERY";
				if (document != null) {
					String trimmedDoc = document.trim().toLowerCase();
					if (trimmedDoc.startsWith("mutation")) type = "MUTATION";
					else if (trimmedDoc.startsWith("subscription")) type = "SUBSCRIPTION";
				}

				AuditEvent event = AuditEvent.builder()
						.serviceName(serviceName)
						.method("POST")
						.endpoint("/graphql")
						.operation(type + ":" + operationName)
						.userID(extractUserId(request))
						.clientIP(getClientIp(request))
						.userAgent(request.getHeaders().getFirst("User-Agent"))
						.statusCode(200)
						.durationMs(duration)
						.success(response.isValid())
						.metadata(buildMetadata(request, response))
						.build();

				// El AuditLogger se encarga de la persistencia final
				auditLogger.logAuditEvent(event);

			} catch (Exception e) {
				log.warn("Error al crear el evento de auditoría de GraphQL", e);
			}
		});
	}

	private String extractUserId(WebGraphQlRequest request) {
		// Prioridad: Header personalizado > Atributos de Contexto
		String userId = request.getHeaders().getFirst("X-User-ID");
		if (userId != null && !userId.isBlank()) return userId;

		return "anonymous";
	}

	private String getClientIp(WebGraphQlRequest request) {
		List<String> xfHeader = request.getHeaders().get("X-Forwarded-For");
		if (xfHeader != null && !xfHeader.isEmpty()) {
			return xfHeader.get(0).split(",")[0];
		}
		return request.getRemoteAddress() != null ?
				request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
	}

	private Map<String, Object> buildMetadata(WebGraphQlRequest request, WebGraphQlResponse response) {
		Map<String, Object> metadata = new HashMap<>();

		// Registro de variables para trazabilidad completa
		if (request.getVariables() != null && !request.getVariables().isEmpty()) {
			metadata.put("variables", request.getVariables());
		}

		// Registro de errores de negocio devueltos por GraphQL
		if (!response.getErrors().isEmpty()) {
			metadata.put("errors", response.getErrors());
		}

		return metadata;
	}
}