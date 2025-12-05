package at.backend.MarketingCompany.account.user.domain.repository;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {

  // Basic CRUD
  User save(User user);

  Optional<User> findById(UserId userId);

  Optional<User> findByEmail(Email email);

  void delete(User user);

  boolean existsById(UserId userId);

  boolean existsByEmail(Email email);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  // Finders
  Page<User> search(Pageable pageable);

  long countAll(boolean includeDeleted);

  Page<User> findByActive(boolean active, Pageable pageable);

  Page<User> findByRole(Role role, Pageable pageable);

  // Analytics
  long countActiveUsers(boolean active);

  long countAllActiveSessions();

  long countByRole(Role role);
}
