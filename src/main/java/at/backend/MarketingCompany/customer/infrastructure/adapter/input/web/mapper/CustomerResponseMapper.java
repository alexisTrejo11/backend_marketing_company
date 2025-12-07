package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import at.backend.MarketingCompany.common.PageResponse;
import at.backend.MarketingCompany.customer.domain.Customer;
import at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto.CustomerResponse;

@Component
public class CustomerResponseMapper {
  public CustomerResponse toResponse(Customer customer) {
    if (customer == null) {
      return null;
    }

    var builder = CustomerResponse.builder();
    builder.id(customer.getId().value());
    builder.createdAt(customer.getCreatedAt());
    builder.updatedAt(customer.getUpdatedAt());
    builder.status(customer.getStatus() != null ? customer.getStatus().name() : null);
    builder.blocked(customer.isBlocked());
    builder.hasSocialMediaHandles(customer.hasSocialMediaHandles());
    builder.hasCompetitors(customer.hasCompetitors());
    if (customer.getPersonalInfo() != null) {
      builder.firstName(customer.getPersonalInfo().firstName());
      builder.lastName(customer.getPersonalInfo().lastName());
    }

    if (customer.getContactDetails() != null) {
      builder.email(customer.getContactDetails().email() != null ? customer.getContactDetails().email().value() : null);
      builder.phone(customer.getContactDetails().phone() != null ? customer.getContactDetails().phone().value() : null);
    }

    if (customer.getBusinessProfile() != null) {
      builder.company(customer.getBusinessProfile().company());
      builder.industry(customer.getBusinessProfile().industry());
      builder.brandVoice(customer.getBusinessProfile().brandVoice());
      builder.brandColors(customer.getBusinessProfile().brandColors());
      builder.targetMarket(customer.getBusinessProfile().targetMarket());
    }

    return builder.build();
  }

  public PageResponse<CustomerResponse> toPageResponse(Page<Customer> customerPage) {
    Page<CustomerResponse> page = customerPage.map(this::toResponse);
    return PageResponse.of(page);
  }
}
