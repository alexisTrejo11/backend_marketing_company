package at.backend.MarketingCompany.account.auth.adapaters.outbound.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@RedisHash(value = "auth_session", timeToLive = 2592000)
public class AuthSessionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String sessionId;

    @Indexed
    private String userId;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastAccessedAt;
    private String userAgent;
    private String ipAddress;
    private boolean revoked;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    @JsonCreator
    public AuthSessionEntity(
            @JsonProperty("sessionId") String sessionId,
            @JsonProperty("userId") String userId,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("expiresAt") LocalDateTime expiresAt,
            @JsonProperty("lastAccessedAt") LocalDateTime lastAccessedAt,
            @JsonProperty("userAgent") String userAgent,
            @JsonProperty("ipAddress") String ipAddress,
            @JsonProperty("revoked") boolean revoked,
            @JsonProperty("ttl") Long ttl
    ) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.lastAccessedAt = lastAccessedAt;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.revoked = revoked;
        this.ttl = ttl;
    }

    @JsonIgnore
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    @JsonIgnore
    public boolean isValid() {
        return !isExpired() && !revoked;
    }

    @JsonIgnore
    public void updateTTL() {
        if (expiresAt != null) {
            long seconds = java.time.Duration.between(LocalDateTime.now(), expiresAt).getSeconds();
            this.ttl = Math.max(seconds, 60L);
        }
    }
}