package at.backend.MarketingCompany.customer.application.dto.query;

import org.springframework.data.domain.Pageable;

public record GetAllCustomersQuery(Pageable pageable) {
}
