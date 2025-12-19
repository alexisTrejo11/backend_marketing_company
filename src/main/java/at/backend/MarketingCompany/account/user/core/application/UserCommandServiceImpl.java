package at.backend.MarketingCompany.account.user.core.application;

import at.backend.MarketingCompany.account.user.core.application.command.CreateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserPersonalDataCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserStatusCommand;
import at.backend.MarketingCompany.account.user.core.application.command.RestoreUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.SoftDeleteUserCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserStatus;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserNotFoundException;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserValidationException;
import at.backend.MarketingCompany.account.user.core.ports.input.UserCommandService;
import at.backend.MarketingCompany.account.user.core.ports.output.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public User handleCreateUser(CreateUserCommand command) {
    log.info("Creating new user with email: {}", command.email());

    if (userRepository.existsByEmail(command.email())) {
      throw new UserValidationException("User with this email already exists");
    }

    if (command.phoneNumber() != null && userRepository.existsByPhoneNumber(command.phoneNumber())) {
      throw new UserValidationException("User with this phoneNumber already exists");
    }

    var passwordHashed = passwordEncoder.encode(command.password().value());
    log.info("Password hashed successfully for new user");

    var createParams = command.toCreateParams(passwordHashed, UserStatus.ACTIVE);
    User user = User.createUser(createParams);

    user.assignRoles(Set.of(command.role()));
    log.info("User entity created");

    User createdUser = userRepository.save(user);
    log.info("User created successfully with ID: {}", createdUser.getId());

    return createdUser;
  }

  @Override
  @Transactional
  public User handleUpdatePersonalData(UpdateUserPersonalDataCommand command) {
    log.info("Updating profile for user: {}", command.userId());

    User user = findUserOrThrow(command.userId());
    log.info("Updating personal data for user entity");

    user.updatePersonalData(command.toPersonalData());
    User updatedUser = userRepository.save(user);
    log.info("Profile updated successfully for user: {}", command.userId());

    return updatedUser;
  }

  @Override
  @Transactional
  public User handleActivateUser(UpdateUserStatusCommand command) {
    log.info("Activating user: {}", command.userId());

    User user = findUserOrThrow(command.userId());
    user.activate();

    User updatedUser = userRepository.save(user);
    log.info("User activated successfully: {}", command.userId());

    return updatedUser;
  }

  @Override
  public User handleBanUser(UpdateUserStatusCommand command) {
    log.info("Banning user: {}", command.userId());

    User user = findUserOrThrow(command.userId());
    user.ban();
    log.info("User mark as banned");

    User updatedUser = userRepository.save(user);
    log.info("User banned successfully: {}", command.userId());

    return updatedUser;
  }

  @Override
  public User handleSoftDeleteUser(SoftDeleteUserCommand command) {
    log.info("Soft deleting user: {}", command.userId());

		User user = findUserOrThrow(command.userId());
	  if (command.isUserAction()) {
		  user.validateUserOperation();
	  }

    log.info("Soft deleting user entity");

    user.softDelete();
    log.info("Saving soft deleted user to repository");

    var savedUser = userRepository.save(user);

    log.info("User soft deleted successfully: {}", command.userId());
    return savedUser;
  }

  @Override
  public User handleRestoreUser(RestoreUserCommand command) {
    log.info("Restoring user: {}", command.userId());
    User user = userRepository.findDeletedById(command.userId())
        .orElseThrow(() -> new UserNotFoundException(command.userId()));

    user.restore();
    log.info("User marked as active");

    var savedUser = userRepository.save(user);
    log.info("User restored successfully: {}", command.userId());
    return savedUser;
  }

  private User findUserOrThrow(UserId userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }
}
