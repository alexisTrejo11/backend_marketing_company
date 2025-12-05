package at.backend.MarketingCompany.account.user.adapters.outbound.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserStatus;

// TODO: Check
public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
  Optional<UserEntity> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.deletedAt IS NULL")
  long countAllNotDeleted();

  @Query("SELECT COUNT(u) FROM UserEntity u")
  long countAll();

  @Query("SELECT COUNT(u) FROM UserEntity u WHERE (u.deletedAt IS NULL) AND (u.status = ACTIVE)")
  long countByActive();

  @Query("SELECT COUNT(u) FROM UserEntity u JOIN u.roles r WHERE r = :role")
  long countByRole(@Param("role") Role role);

  Page<UserEntity> findByStatus(UserStatus status, Pageable pageable);

  @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = :role")
  Page<UserEntity> findByRole(@Param("role") Role role, Pageable pageable);

  Optional<UserEntity> findByIdAndDeletedAtIsNull(String id);

  Page<UserEntity> findAll(Pageable pageable);
}
