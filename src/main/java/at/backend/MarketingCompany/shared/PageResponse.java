package at.backend.MarketingCompany.shared;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PageResponse<T>(
        Iterable<T> items,
        PaginationMetadata pagination) {

    public static <T> PageResponse<T> of(Iterable<T> item, PaginationMetadata pagination) {
        return new PageResponse<>(item, pagination);
    }

    public static <T> PageResponse<T> of(Page<T> page) {
        if (page == null) {
            return empty();
        }

        if (page.getContent().isEmpty()) {
            return empty();
        }

        Iterable<T> item = page.getContent();
        Pageable pageable = page.getPageable();
        PaginationMetadata pagination = PaginationMetadata.builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .total(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .hasPreviousPage(page.hasPrevious())
                .build();

        return new PageResponse<>(item, pagination);
    }

    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(List.of(), PaginationMetadata.builder()
                .currentPage(0)
                .pageSize(0)
                .total(0)
                .totalPages(0)
                .hasNextPage(false)
                .hasPreviousPage(false)
                .build());
    }
}
