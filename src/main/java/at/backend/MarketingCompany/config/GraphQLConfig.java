package at.backend.MarketingCompany.config;

import at.backend.MarketingCompany.shared.dto.GraphQL.GraphQLScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(GraphQLScalars.DateTime)
                .scalar(GraphQLScalars.Date)
                .scalar(GraphQLScalars.BigDecimal)
                .scalar(GraphQLScalars.JSON);
    }
}
