package at.backend.MarketingCompany.shared.graphql.error;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Builder
public record GraphQLErrorResponse(
        String message,
        List<String> path,
        List<Location> locations,
        Map<String, Object> extensions,
        Instant timestamp) {

    @Builder
    public record Location(int line, int column) {
    }
}