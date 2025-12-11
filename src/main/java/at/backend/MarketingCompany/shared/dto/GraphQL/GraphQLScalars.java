package at.backend.MarketingCompany.shared.dto.GraphQL;

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
            .description("Custom scalar type for JSON objects")
            .coercing(new Coercing<Object, Object>() {
                private final ObjectMapper objectMapper = new ObjectMapper();

                @Override
                public Object serialize(@NotNull Object dataFetcherResult, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingSerializeException {
                    try {
                        return objectMapper.convertValue(dataFetcherResult, Map.class);
                    } catch (Exception e) {
                        throw new CoercingSerializeException("Error serializing JSON: " + e.getMessage());
                    }
                }

                @Override
                public Object parseValue(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseValueException {
                    try {
                        return objectMapper.convertValue(input, Map.class);
                    } catch (Exception e) {
                        throw new CoercingParseValueException("Error parsing JSON value: " + e.getMessage());
                    }
                }

                @Override
                public Object parseLiteral(@NotNull Value<?> input, @NotNull CoercedVariables variables, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseLiteralException {
                    if (input instanceof StringValue) {
                        try {
                            return objectMapper.readValue(((StringValue) input).getValue(), Map.class);
                        } catch (Exception e) {
                            throw new CoercingParseLiteralException("Error parsing JSON literal: " + e.getMessage());
                        }
                    }
                    throw new CoercingParseLiteralException("Expected a JSON string but got: " + input.getClass().getSimpleName());
                }
            })
            .build();
}