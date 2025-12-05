package at.backend.MarketingCompany.account.user.adapters.outbound.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final JpaUserRepository jpaUserRepository;
  private final UserEntityMapper entityMapper;

  @Override
  public User save(User user) {
    UserEntity entity = entityMapper.toEntity(user);
    UserEntity savedEntity = jpaUserRepository.save(entity);
    return entityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<User> findById(UserId userId) {
    return jpaUserRepository.findById(userId.value())
        .map(entityMapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return jpaUserRepository.findByEmail(email.value())
        .map(entityMapper::toDomain);
  }

  @Override
  public void delete(User user) {
    UserEntity entity = entityMapper.toEntity(user);
    entity.markAsDeleted();
    jpaUserRepository.save(entity);
  }

  @Override
  public boolean existsById(UserId userId) {
    return jpaUserRepository.existsById(userId.value());
  }

  @Override
  public boolean existsByEmail(Email email) {
    return jpaUserRepository.existsByEmail(email.value());
  }

  @Override
  public Page<User> search(Pageable pageable) {
    return jpaUserRepository.findAll(pageable)
        .map(entityMapper::toDomain);
  }

  @Override
  public long countAll(boolean includeDeleted) {
    if (includeDeleted) {
      return jpaUserRepository.countAll();
    }
    return jpaUserRepository.countAllNotDeleted();
  }

  @Override
  public Page<User> findByActive(boolean active, Pageable pageable) {
    throw new UnsupportedOperationException("Unimplemented method 'findByActive'");
  }

  @Override
  public Page<User> findByRole(Role role, Pageable pageable) {
    return jpaUserRepository.findByRole(role, pageable)
        .map(entityMapper::toDomain);
  }

  @Override
  public long countActiveUsers(boolean active) {
    return jpaUserRepository.countByActive();
  }

  @Override
  public long countAllActiveSessions() {
    return 0;
  }

  @Override
  public long countByRole(Role role) {
    return jpaUserRepository.countByRole(role);
  }

  @Override
  public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
    return jpaUserRepository.existsByPhoneNumber(phoneNumber.value());
  }

}
