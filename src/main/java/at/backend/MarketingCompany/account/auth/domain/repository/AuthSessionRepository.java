package at.backend.MarketingCompany.account.auth.domain.repository;

import java.util.List;
import java.util.Optional;

import at.backend.MarketingCompany.account.auth.domain.entitiy.AuthSession;
import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.SessionId;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

public interface AuthSessionRepository {

  // Basic CRUD
  AuthSession save(AuthSession session);

  Optional<AuthSession> findById(SessionId sessionId);

  Optional<AuthSession> findByRefreshToken(String refreshToken);

  void delete(AuthSession session);

  // Finders
  List<AuthSession> findByUserId(UserId userId);

  List<AuthSession> findExpiredSessions();

  // Batch operations
  void deleteAllByUserId(UserId userId);

  void deleteExpiredSessions();

  // Analytics
  long countActiveSessionsByUserId(UserId userId);

  long countAllActiveSessions();
}
