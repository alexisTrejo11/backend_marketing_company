package at.backend.MarketingCompany.customer.domain.valueobject;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;

public record ContactDetails(Email email, PhoneNumber phone) {

  public static ContactDetails empty() {
    return new ContactDetails(null, null);
  }

  public ContactDetails addPhone(PhoneNumber phone) {
    return new ContactDetails(this.email, phone);
  }

  public ContactDetails addEmail(Email email) {
    return new ContactDetails(email, this.phone);
  }
}
