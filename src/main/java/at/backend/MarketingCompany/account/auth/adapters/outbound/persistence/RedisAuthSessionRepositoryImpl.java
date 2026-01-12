package at.backend.MarketingCompany.account.auth.adapters.outbound.persistence;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.auth.core.port.output.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisAuthSessionRepositoryImpl implements AuthSessionRepository {
    private static final String SESSION_KEY_PREFIX = "session:";           // refreshToken -> AuthSessionEntity
    private static final String USER_SESSIONS_PREFIX = "user_sessions:";   // userId -> Set<refreshToken>

    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthSessionEntityMapper mapper;

    private String getSessionKey(String refreshToken) {
        return SESSION_KEY_PREFIX + refreshToken;
    }

    private String getUserSessionsKey(String userId) {
        return USER_SESSIONS_PREFIX + userId;
    }

    private String getUserSessionsKey(UserId userId) {
        return USER_SESSIONS_PREFIX + userId.asString();
    }

    @Override
    public AuthSession save(AuthSession session) {
        try {
            String refreshToken = session.getSessionId().value();
            log.debug("Saving auth session with refresh token as ID: {}", refreshToken.substring(0, 6) + "...");

            AuthSessionEntity entity = mapper.toEntity(session);
            entity.updateTTL();

            String sessionKey = getSessionKey(refreshToken);
            String userSessionsKey = getUserSessionsKey(session.getUserId());

            redisTemplate.opsForValue().set(
                    sessionKey,
                    entity,
                    entity.getTtl(),
                    TimeUnit.SECONDS
            );

            redisTemplate.opsForSet().add(userSessionsKey, refreshToken);
            redisTemplate.expire(userSessionsKey, entity.getTtl(), TimeUnit.SECONDS);

            log.info("Auth session saved with refresh token: {}", refreshToken);
            return session;

        } catch (Exception e) {
            log.error("Error saving auth session: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save auth session", e);
        }
    }

    @Override
    public Optional<AuthSession> findById(SessionId sessionId) {
        return findByRefreshToken(sessionId.value());
    }

    @Override
    public Optional<AuthSession> findByRefreshToken(String refreshToken) {
        try {
            log.debug("Finding auth session by refresh token (which is the ID): {}", refreshToken);

            String sessionKey = getSessionKey(refreshToken);
            AuthSessionEntity entity = (AuthSessionEntity) redisTemplate.opsForValue().get(sessionKey);

            if (entity == null) {
                log.debug("Session not found for refresh token: {}", refreshToken);
                return Optional.empty();
            }

            if (entity.isValid()) {
                entity.setLastAccessedAt(LocalDateTime.now());
                entity.updateTTL();
                redisTemplate.opsForValue().set(
                        sessionKey,
                        entity,
                        entity.getTtl(),
                        TimeUnit.SECONDS
                );

                log.debug("Session found and valid for refresh token: {}", refreshToken);
                return Optional.of(mapper.toDomain(entity));
            } else {
                log.debug("Session found but invalid for refresh token: {}", refreshToken);
                deleteByRefreshToken(refreshToken);
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("Error finding session by refresh token {}: {}", refreshToken, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void delete(AuthSession session) {
        deleteByRefreshToken(session.getSessionId().value());
    }

    private void deleteByRefreshToken(String refreshToken) {
        try {
            log.debug("Deleting auth session with refresh token: {}", refreshToken.substring(0, 6) + "...");

            Optional<AuthSession> sessionOpt = findByRefreshToken(refreshToken);
            if (sessionOpt.isEmpty()) {
                log.debug("Session not found for deletion with refresh token: {}", refreshToken);
                return;
            }

            AuthSession session = sessionOpt.get();
            String sessionKey = getSessionKey(refreshToken);
            String userSessionsKey = getUserSessionsKey(session.getUserId());

            redisTemplate.delete(sessionKey);

            redisTemplate.opsForSet().remove(userSessionsKey, refreshToken);

            log.info("Auth session deleted with refresh token: {}", refreshToken.substring(0, 6) + "...");

        } catch (Exception e) {
            log.error("Error deleting auth session with refresh token {}: {}", refreshToken, e.getMessage(), e);
        }
    }

    @Override
    public List<AuthSession> findByUserId(UserId userId) {
        try {
            log.debug("Finding auth sessions for user: {}", userId.asString());

            String userSessionsKey = getUserSessionsKey(userId);
            Set<Object> refreshTokens = redisTemplate.opsForSet().members(userSessionsKey);

            if (refreshTokens == null || refreshTokens.isEmpty()) {
                return Collections.emptyList();
            }

            return refreshTokens.stream()
                    .map(token -> findByRefreshToken((String) token))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error finding sessions for user {}: {}", userId.asString(), e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<AuthSession> findExpiredSessions() {
        log.debug("Finding expired sessions (Redis TTL handles expiration automatically)");
        return Collections.emptyList();
    }

    @Override
    public void deleteAllByUserId(UserId userId) {
        try {
            log.debug("Deleting all sessions for user: {}", userId);

            List<AuthSession> userSessions = findByUserId(userId);

            userSessions.forEach(session -> deleteByRefreshToken(session.getSessionId().value()));

            String userSessionsKey = getUserSessionsKey(userId);
            redisTemplate.delete(userSessionsKey);

            log.info("Deleted {} sessions for user: {}", userSessions.size(), userId);

        } catch (Exception e) {
            log.error("Error deleting sessions for user {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void deleteExpiredSessions() {
        log.debug("Redis TTL handles session expiration automatically");
    }

    @Override
    public long countActiveSessionsByUserId(UserId userId) {
        try {
            log.debug("Counting active sessions for user: {}", userId);

            return findByUserId(userId).stream()
                    .filter(session -> !session.isExpired() && !session.isRevoked())
                    .count();

        } catch (Exception e) {
            log.error("Error counting sessions for user {}: {}", userId, e.getMessage());
            return 0L;
        }
    }

    @Override
    public long countAllActiveSessions() {
        try {
            log.debug("Counting all active sessions");

            Set<String> keys = redisTemplate.keys(SESSION_KEY_PREFIX + "*");

            return keys.stream()
                    .map(key -> (AuthSessionEntity) redisTemplate.opsForValue().get(key))
                    .filter(Objects::nonNull)
                    .filter(AuthSessionEntity::isValid)
                    .count();
        } catch (Exception e) {
            log.error("Error counting all active sessions: {}", e.getMessage());
            return 0L;
        }
    }

    public void updateSessionWithNewRefreshToken(String oldRefreshToken, String newRefreshToken, AuthSession updatedSession) {
        try {
            deleteByRefreshToken(oldRefreshToken);

            save(updatedSession);

            log.info("Session refreshed: {} -> {}", oldRefreshToken, newRefreshToken);

        } catch (Exception e) {
            log.error("Error updating session with new refresh token: {}", e.getMessage(), e);
        }
    }

    public boolean existsByRefreshToken(String refreshToken) {
        String sessionKey = getSessionKey(refreshToken);
        return redisTemplate.hasKey(sessionKey);
    }

    public Optional<UserId> findUserIdByRefreshToken(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .map(AuthSession::getUserId);
    }
}