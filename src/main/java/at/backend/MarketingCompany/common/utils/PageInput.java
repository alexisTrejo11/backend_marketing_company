package at.backend.MarketingCompany.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageInput(
    int page,
    int size,
    String sortBy,
    boolean ascending
)
{
    public Pageable toPageable() {
        var sort = ascending ? Sort.by(sortBy).ascending()
                             : org.springframework.data.domain.Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }
}
