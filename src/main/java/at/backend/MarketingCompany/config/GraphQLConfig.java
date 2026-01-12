package at.backend.MarketingCompany.config;

import at.backend.MarketingCompany.config.security.AuthenticationDirective;
import at.backend.MarketingCompany.shared.dto.GraphQL.GraphQLScalars;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import at.backend.MarketingCompany.config.security.RoleDirective;

@Configuration
public class GraphQLConfig {

  private final AuthenticationDirective authenticationDirective;
  private final RoleDirective roleDirective;

  @Value("classpath:graphql/schema.graphqls")
  private Resource schemaResource;

  @Autowired
  public GraphQLConfig(AuthenticationDirective authenticationDirective,
      RoleDirective roleDirective) {
    this.authenticationDirective = authenticationDirective;
    this.roleDirective = roleDirective;
  }

  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    return wiringBuilder -> wiringBuilder
        .scalar(GraphQLScalars.DateTime)
        .scalar(GraphQLScalars.Date)
        .scalar(GraphQLScalars.BigDecimal)
        .scalar(GraphQLScalars.JSON);
  }

  @Bean
  public GraphQLSchema graphQLSchema() throws IOException {
    String sdl = loadSchema();
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();

    return new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .directive("authenticated", authenticationDirective)
        .directive("hasRole", roleDirective)
        .build();
  }

  private String loadSchema() throws IOException {
    try (InputStream inputStream = schemaResource.getInputStream()) {
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
