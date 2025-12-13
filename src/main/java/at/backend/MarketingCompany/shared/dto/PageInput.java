package at.backend.MarketingCompany.shared.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PageInput(
    int page,
    int size,
    String sortBy,
    boolean ascending) {
  public Pageable toPageable() {
    var sort = ascending ? Sort.by(sortBy).ascending()
        : org.springframework.data.domain.Sort.by(sortBy).descending();
    return PageRequest.of(page, size, sort);
  }

  public PageInput {
    if (size <= 0) {
      size = 10; // default page size
    }
    if (sortBy == null || sortBy.isBlank()) {
      sortBy = "createdAt"; // default sort by createdAt
    }

    if (page < 0) {
      page = 0; // default to first page
    }

    if (size > 100) {
      size = 100; // maximum page size
    }
  }
}
