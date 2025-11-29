package at.backend.MarketingCompany.common;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record PageResponse<T>(
        Iterable<T> item,
        PaginationMetadata pagination) {

    public static <T> PageResponse<T> of(Iterable<T> item, PaginationMetadata pagination) {
        return new PageResponse<>(item, pagination);
    }


    public static <T> PageResponse<T> of(Page<T> page) {
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


        @Builder
        public record PaginationMetadata(
                int currentPage,
                int pageSize,
                long total,
                int totalPages,
                boolean hasNextPage,
                boolean hasPreviousPage
        ) {}
}
