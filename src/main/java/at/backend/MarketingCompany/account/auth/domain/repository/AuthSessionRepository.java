package at.backend.MarketingCompany.account.auth.domain.repository;

import at.backend.MarketingCompany.auth.domain.entity.AuthSession;
import at.backend.MarketingCompany.auth.domain.entity.valueobject.SessionId;
import at.backend.MarketingCompany.auth.domain.entity.valueobject.UserId;

import java.util.List;
import java.util.Optional;

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