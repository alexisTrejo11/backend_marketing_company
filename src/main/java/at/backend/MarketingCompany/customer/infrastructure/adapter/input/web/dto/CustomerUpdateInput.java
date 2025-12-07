package at.backend.MarketingCompany.customer.infrastructure.adapter.input.web.dto;

import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.Set;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.application.dto.command.CustomerUpdateCommand;
import at.backend.MarketingCompany.customer.domain.valueobject.BusinessProfile;
import at.backend.MarketingCompany.customer.domain.valueobject.ContactDetails;
import at.backend.MarketingCompany.customer.domain.valueobject.CustomerId;
import at.backend.MarketingCompany.customer.domain.valueobject.SocialMediaHandles;

public record CustomerUpdateInput(
    @Size(max = 50) String firstName,
    @Size(max = 50) String lastName,
    @jakarta.validation.constraints.Email String email,
    String phone,
    @Size(max = 100) String company,
    String industry,
    String brandVoice,
    String brandColors,
    String targetMarket,
    Set<String> competitorUrls,
    Map<String, String> socialMediaHandles) {

  public CustomerUpdateCommand toCommand(String id) {
    var builder = CustomerUpdateCommand.builder()
        .id(CustomerId.of(id));

    if (firstName != null || lastName != null) {
      builder.personalInfo(PersonName.from(firstName, lastName));
    }

    var contactDetails = ContactDetails.empty();
    if (email != null) {
      contactDetails = contactDetails.addEmail(new Email(email));
    }
    if (phone != null) {
      contactDetails = contactDetails.addPhone(new PhoneNumber(phone));
    }
    builder.contactDetails(contactDetails);

    var businessProfile = BusinessProfile.builder()
        .company(company)
        .industry(industry)
        .brandVoice(brandVoice)
        .brandColors(brandColors)
        .targetMarket(targetMarket)
        .competitorUrls(competitorUrls)
        .socialMediaHandles(socialMediaHandles != null ? SocialMediaHandles.create(socialMediaHandles) : null)
        .build();

    builder.businessProfile(businessProfile);

    return builder.build();
  }
}
