package at.backend.MarketingCompany.account.auth.adapaters.outbound.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "auth_sessions", timeToLive = 30 * 24 * 60 * 60) // 30 days TTL
public class AuthSessionEntity implements Serializable {
  @Id
  private String sessionId;

  @Indexed
  private String userId;

  @Indexed
  private String refreshToken;

  private LocalDateTime createdAt;
  private LocalDateTime expiresAt;
  private LocalDateTime lastAccessedAt;
  private String userAgent;
  private String ipAddress;
  private boolean revoked;

  @TimeToLive(unit = TimeUnit.SECONDS)
  private Long ttl;

  public void calculateTTL() {
    if (expiresAt != null) {
      this.ttl = java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
      // Ensure minimum TTL of 1 minute
      if (this.ttl < 60) {
        this.ttl = 60L;
      }
    }
  }

  public boolean isExpired() {
    return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
  }

  public boolean isValid() {
    return !isExpired() && !revoked;
  }
}
