package at.backend.MarketingCompany.account.auth.adapaters.outbound.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.auth.domain.repository.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthSessionRepositoryImpl implements AuthSessionRepository {

  private final RedisAuthSessionRepository redisAuthSessionRepository;
  private final AuthSessionEntityMapper authSessionEntityMapper;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public AuthSession save(AuthSession session) {
    log.debug("Saving auth session with ID: {}", session.getSessionId().value());

    AuthSessionEntity entity = authSessionEntityMapper.toEntity(session);
    AuthSessionEntity savedEntity = redisAuthSessionRepository.save(entity);

    log.info("Auth session saved successfully with ID: {}", savedEntity.getSessionId());
    return authSessionEntityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<AuthSession> findById(SessionId sessionId) {
    log.debug("Finding auth session by ID: {}", sessionId.value());

    return redisAuthSessionRepository.findById(sessionId.value())
        .map(authSessionEntityMapper::toDomain);
  }

  @Override
  public Optional<AuthSession> findByRefreshToken(String refreshToken) {
    log.debug("Finding auth session by refresh token");

    return redisAuthSessionRepository.findByRefreshToken(refreshToken)
        .map(authSessionEntityMapper::toDomain);
  }

  @Override
  public void delete(AuthSession session) {
    log.debug("Deleting auth session with ID: {}", session.getSessionId().value());

    AuthSessionEntity entity = authSessionEntityMapper.toEntity(session);
    redisAuthSessionRepository.delete(entity);

    log.info("Auth session deleted successfully with ID: {}", session.getSessionId().value());
  }

  @Override
  public List<AuthSession> findByUserId(UserId userId) {
    log.debug("Finding auth sessions for user ID: {}", userId.value());

    return redisAuthSessionRepository.findByUserId(userId.value()).stream()
        .map(authSessionEntityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AuthSession> findExpiredSessions() {
    log.debug("Finding expired auth sessions");

    // Implementar lógica para encontrar sesiones expiradas
    // Esto requeriría un scan de Redis, lo cual es costoso
    // En producción, usaríamos TTL de Redis y limpieza periódica
    return List.of();
  }

  @Override
  public void deleteAllByUserId(UserId userId) {
    log.debug("Deleting all auth sessions for user ID: {}", userId.value());

    List<AuthSessionEntity> userSessions = redisAuthSessionRepository.findByUserId(userId.value());
    redisAuthSessionRepository.deleteAll(userSessions);

    log.info("Deleted {} auth sessions for user ID: {}", userSessions.size(), userId.value());
  }

  @Override
  public void deleteExpiredSessions() {
    log.debug("Deleting expired auth sessions");

    log.info("Redis TTL handles session expiration automatically");
  }

  @Override
  public long countActiveSessionsByUserId(UserId userId) {
    log.debug("Counting active sessions for user ID: {}", userId.value());

    return redisAuthSessionRepository.countByUserId(userId.value());
  }

  @Override
  public long countAllActiveSessions() {
    log.debug("Counting all active sessions");

    return redisAuthSessionRepository.count();
  }

  public void renewSessionTTL(SessionId sessionId, long ttlSeconds) {
    String key = "auth_sessions:" + sessionId.value();
    redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
    log.debug("Renewed TTL for session {}: {} seconds", sessionId.value(), ttlSeconds);
  }
}
