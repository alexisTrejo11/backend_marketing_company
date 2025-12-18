package at.backend.MarketingCompany.shared;

import lombok.Builder;

@Builder
public record PaginationMetadata(
        int currentPage,
        int pageSize,
        long total,
        int totalPages,
        boolean hasNextPage,
        boolean hasPreviousPage) {

}
