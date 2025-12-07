package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto;

import java.util.Map;
import java.util.Set;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.application.dto.command.CustomerCreateCommand;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.SocialMediaHandles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerInput(
    @NotBlank @Size(max = 50) String firstName,
    @NotBlank @Size(max = 50) String lastName,
    @NotBlank @jakarta.validation.constraints.Email String email,
    String phone,
    @NotBlank @Size(max = 100) String company,
    String industry,
    String brandVoice,
    String brandColors,
    String targetMarket,
    Set<String> competitorUrls,
    Map<String, String> socialMediaHandles) {

  public CustomerCreateCommand toCommand() {
    var builder = CustomerCreateCommand.builder();

    builder.personalInfo(PersonName.from(firstName, lastName));

    builder.contactDetails(new ContactDetails(
        Email.from(email),
        phone != null ? PhoneNumber.from(phone) : null));

    builder.businessProfile(BusinessProfile.builder()
        .company(company)
        .industry(industry)
        .brandVoice(brandVoice)
        .brandColors(brandColors)
        .targetMarket(targetMarket)
        .competitorUrls(competitorUrls)
        .socialMediaHandles(socialMediaHandles != null ? SocialMediaHandles.create(socialMediaHandles) : null)
        .build());

    return builder.build();
  }
}
