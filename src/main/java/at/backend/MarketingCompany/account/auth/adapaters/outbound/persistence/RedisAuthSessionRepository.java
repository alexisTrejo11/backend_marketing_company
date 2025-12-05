package at.backend.MarketingCompany.account.auth.adapaters.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisAuthSessionRepository extends CrudRepository<AuthSessionEntity, String> {

  // Basic finders
  Optional<AuthSessionEntity> findByRefreshToken(String refreshToken);

  List<AuthSessionEntity> findByUserId(String userId);

  // Analytics
  long countByUserId(String userId);

  // Custom queries using RedisTemplate would be implemented separately
  void deleteByUserId(String userId);

  void deleteByRefreshToken(String refreshToken);
}
