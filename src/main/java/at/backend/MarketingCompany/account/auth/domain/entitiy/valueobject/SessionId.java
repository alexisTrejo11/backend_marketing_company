package at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject;

import java.util.UUID;

public record SessionId(String value) {
  public SessionId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Session ID cannot be null or blank");
    }
  }

  public static SessionId create() {
    return new SessionId(UUID.randomUUID().toString());
  }

  public static SessionId from(String value) {
    return new SessionId(value);
  }

  public static SessionId from(UUID uuid) {
    return new SessionId(uuid.toString());
  }

}
