package at.backend.MarketingCompany.config.ratelimit;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class GraphQLRateLimitRegistry {

	private final ApplicationContext applicationContext;
	private final Map<String, GraphQLRateLimit> rateLimitMap = new HashMap<>();

	public GraphQLRateLimitRegistry(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void init() {
		log.debug("INITIALIZING GRAPHQL RATE LIMIT REGISTRY");

		// Search for all beans annotated with @Controller
		String[] controllerBeans = applicationContext.getBeanNamesForAnnotation(
				org.springframework.stereotype.Controller.class
		);

		for (String beanName : controllerBeans) {
			Object bean = applicationContext.getBean(beanName);
			Class<?> clazz = bean.getClass();

			// If it's a proxy class, get the superclass
			if (clazz.getName().contains("$$")) {
				clazz = clazz.getSuperclass();
			}

			log.debug("Scanning controller: {}", clazz.getName());

			for (Method method : clazz.getDeclaredMethods()) {
				// Search for @MutationMapping and @QueryMapping
				MutationMapping mutationMapping = AnnotationUtils.findAnnotation(method, MutationMapping.class);
				QueryMapping queryMapping = AnnotationUtils.findAnnotation(method, QueryMapping.class);

				if (mutationMapping != null || queryMapping != null) {
					String operationName = getOperationName(method, mutationMapping, queryMapping);

					// Check for @GraphQLRateLimit
					GraphQLRateLimit rateLimit = AnnotationUtils.findAnnotation(method, GraphQLRateLimit.class);

					if (rateLimit != null) {
						rateLimitMap.put(operationName, rateLimit);
						log.info("Registered rate limit for operation '{}': profile={}, type={}",
								operationName, rateLimit.value(), rateLimit.type());
					}
				}
			}
		}

		log.debug("REGISTRY INITIALIZED: {} operations with rate limits", rateLimitMap.size());
	}

	public GraphQLRateLimit getRateLimitForOperation(String operationName) {
		if (operationName == null) return null;

		// Search case-insensitive (endpoints registry with upper case and need to be normalized ej: SignUp registry and need to match endpoint signup)
		String normalizedName = operationName.toLowerCase();

		// First try exact match
		GraphQLRateLimit rateLimit = rateLimitMap.get(operationName);
		if (rateLimit != null) return rateLimit;

		// Search case-insensitive match
		for (Map.Entry<String, GraphQLRateLimit> entry : rateLimitMap.entrySet()) {
			if (entry.getKey().toLowerCase().equals(normalizedName)) {
				return entry.getValue();
			}
		}

		return null;
	}

	private String getOperationName(Method method, MutationMapping mutation, QueryMapping query) {
		// Priority: annotation name > annotation value > method name
		if (mutation != null) {
			String name = mutation.name().isEmpty() ? mutation.value() : mutation.name();
			return name.isEmpty() ? method.getName() : name;
		}

		if (query != null) {
			String name = query.name().isEmpty() ? query.value() : query.name();
			return name.isEmpty() ? method.getName() : name;
		}

		return method.getName();
	}

	public Map<String, GraphQLRateLimit> getAllRateLimits() {
		return new HashMap<>(rateLimitMap);
	}
}