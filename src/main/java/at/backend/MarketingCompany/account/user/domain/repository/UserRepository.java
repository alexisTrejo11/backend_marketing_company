package at.backend.MarketingCompany.account.user.domain.repository;

import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;

import java.util.List;
import java.util.Optional;

import javax.management.relation.Role;

public interface UserRepository {

  // Basic CRUD
  User save(User user);

  Optional<User> findById(UserId userId);

  Optional<User> findByEmail(Email email);

  void delete(User user);

  boolean existsById(UserId userId);

  boolean existsByEmail(Email email);

  // Finders
  List<User> findAll();

  List<User> findByActive(boolean active);

  List<User> findByRole(Role role);

  // Analytics
  long countActiveUsers();

  long countByRole(Role role);
}
