package at.backend.MarketingCompany.shared.dto.GraphQL;

import com.fasterxml.jackson.databind.JsonNode;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.scalars.ExtendedScalars;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class GraphQLScalars {

    public static final GraphQLScalarType DateTime = ExtendedScalars.DateTime;
    public static final GraphQLScalarType Date = ExtendedScalars.Date;
    public static final GraphQLScalarType BigDecimal = ExtendedScalars.GraphQLBigDecimal;

  public static final GraphQLScalarType JSON = GraphQLScalarType.newScalar()
      .name("JSON")
      .description("Custom scalar type for JSON objects or arrays")
      .coercing(new Coercing<JsonNode, String>() {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public String serialize(@NotNull Object dataFetcherResult,
                                @NotNull GraphQLContext graphQLContext,
                                @NotNull Locale locale) throws CoercingSerializeException {
          try {
            if (dataFetcherResult instanceof JsonNode) {
              return dataFetcherResult.toString();
            }
            if (dataFetcherResult instanceof String) {
              objectMapper.readTree((String) dataFetcherResult);
              return (String) dataFetcherResult;
            }
            return objectMapper.writeValueAsString(dataFetcherResult);
          } catch (Exception e) {
            throw new CoercingSerializeException("Error serializing JSON: " + e.getMessage());
          }
        }

        @Override
        public JsonNode parseValue(@NotNull Object input,
                                   @NotNull GraphQLContext graphQLContext,
                                   @NotNull Locale locale) throws CoercingParseValueException {
          try {
	          if (input instanceof String) {
              return objectMapper.readTree((String) input);
            }

            if (input instanceof JsonNode) {
              return (JsonNode) input;
            }

            return objectMapper.valueToTree(input);
          } catch (Exception e) {
            throw new CoercingParseValueException("Error parsing JSON value: " + e.getMessage());
          }
        }

        @Override
        public JsonNode parseLiteral(@NotNull Value<?> input,
                                     @NotNull CoercedVariables variables,
                                     @NotNull GraphQLContext graphQLContext,
                                     @NotNull Locale locale) throws CoercingParseLiteralException {
          try {
            if (input instanceof StringValue) {
              String stringValue = ((StringValue) input).getValue();
              return objectMapper.readTree(stringValue);
            }

            return objectMapper.valueToTree(input);
          } catch (Exception e) {
            throw new CoercingParseLiteralException("Error parsing JSON literal: " + e.getMessage());
          }
        }
      })
      .build();
}