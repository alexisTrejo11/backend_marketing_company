package at.backend.MarketingCompany.account.auth.adapters.outbound.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisAuthSessionRepository extends CrudRepository<AuthSessionEntity, String> {

    Optional<AuthSessionEntity> findByRefreshToken(String refreshToken);

    List<AuthSessionEntity> findByUserId(String userId);
}
