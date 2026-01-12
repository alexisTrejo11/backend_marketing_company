package at.backend.MarketingCompany.account.user.core.ports.output;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
  // Persistence
  User save(User user);

  void delete(User user);

  // Finders
  Page<User> search(Pageable pageable);

  Optional<User> findById(UserId userId);

  Optional<User> findDeletedById(UserId userId);

  Optional<User> findByEmail(Email email);

  long countAll(boolean includeDeleted);

  Page<User> findByActive(boolean active, Pageable pageable);

  Page<User> findByRole(Role role, Pageable pageable);

  boolean existsById(UserId userId);

  boolean existsByEmail(Email email);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  // Analytics
  long countActiveUsers(boolean active);

  long countAllActiveSessions();

  long countByRole(Role role);
}
