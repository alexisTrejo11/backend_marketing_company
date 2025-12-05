package at.backend.MarketingCompany.account.user.domain.entity.valueobject;

import java.util.UUID;

public record UserId(String value) {
  public UserId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("User ID cannot be null or blank");
    }
  }

  public static UserId generate() {
    return new UserId(UUID.randomUUID().toString());
  }

  public static UserId from(String value) {
    return new UserId(value);
  }

  public static UserId from(UUID uuid) {
    return new UserId(uuid.toString());
  }
}
