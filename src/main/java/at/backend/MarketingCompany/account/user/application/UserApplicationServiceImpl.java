package at.backend.MarketingCompany.account.user.application;

import at.backend.MarketingCompany.account.auth.application.commands.UpdateProfileCommand;
import at.backend.MarketingCompany.account.user.application.command.ActivateUserCommand;
import at.backend.MarketingCompany.account.user.application.command.DeactivateUserCommand;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.auth.domain.repository.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.application.queries.GetActiveUsersQuery;
import at.backend.MarketingCompany.account.user.application.queries.GetUserByEmailQuery;
import at.backend.MarketingCompany.account.user.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.application.queries.GetUserStatisticsQuery;
import at.backend.MarketingCompany.account.user.application.queries.GetUsersByRoleQuery;
import at.backend.MarketingCompany.account.user.application.queries.SearchUsersQuery;
import at.backend.MarketingCompany.account.user.domain.entity.User;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.domain.exceptions.UserNotFoundException;
import at.backend.MarketingCompany.account.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserApplicationServiceImpl {
  private final UserRepository userRepository;
  private final AuthSessionRepository authSessionRepository;

  @Transactional(readOnly = true)
  public User handle(GetUserByIdQuery query) {
    log.debug("Fetching user by ID: {}", query.userId());

    return findUserById(query.userId());
  }

  @Transactional(readOnly = true)
  public User handle(GetUserByEmailQuery query) {
    log.debug("Fetching user by email: {}", query.email());

    return findUserByEmail(query.email());
  }

  @Transactional(readOnly = true)
  public Page<User> handle(SearchUsersQuery query) {
    log.debug("Fetching all users");

    return userRepository.search(query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<User> handle(GetActiveUsersQuery query) {
    log.debug("Fetching active users");

    return userRepository.findByActive(true, query.pageable());
  }

  @Transactional(readOnly = true)
  public Page<User> handle(GetUsersByRoleQuery query) {
    log.debug("Fetching users by role: {}", query.role());

    return userRepository.findByRole(query.role(), query.pageable());
  }

  @Transactional(readOnly = true)
  public UserStatistics handle(GetUserStatisticsQuery query) {
    log.debug("Fetching user statistics");

    long totalUsers = userRepository.countAll(true);
    long activeUsers = userRepository.countActiveUsers(true);
    long adminUsers = userRepository.countByRole(Role.ADMIN);
    long totalSessions = authSessionRepository.countAllActiveSessions();

    return new UserStatistics(totalUsers, activeUsers, adminUsers, totalSessions);
  }

  @Transactional
  public User handle(UpdateProfileCommand command) {
    log.info("Updating profile for user: {}", command.userId());

    User user = findUserById(UserId.from(command.userId()));
    user.updateProfile(command.name(), command.phoneNumber());

    User updatedUser = userRepository.save(user);
    log.info("Profile updated successfully for user: {}", command.userId());

    return updatedUser;
  }

  @Transactional
  public User handle(DeactivateUserCommand command) {
    log.info("Deactivating user: {}", command.userId());

    User user = findUserById(command.userId());
    user.markAsInactive();

    User updatedUser = userRepository.save(user);

    // Logout all sessions
    authSessionRepository.deleteAllByUserId(user.getId());

    log.info("User deactivated successfully: {}", command.userId());
    return updatedUser;
  }

  @Transactional
  public User handle(ActivateUserCommand command) {
    log.info("Activating user: {}", command.userId());

    User user = findUserById(command.userId());
    user.markAsActive();

    User updatedUser = userRepository.save(user);
    log.info("User activated successfully: {}", command.userId());

    return updatedUser;
  }


  private User findUserById(UserId userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId.value()));
  }

  private User findUserByEmail(Email email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(email));
  }
}

