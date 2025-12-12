package at.backend.MarketingCompany.account.user.core.application;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.user.core.application.command.CreateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserPersonalDataCommand;
import at.backend.MarketingCompany.account.auth.core.port.output.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.core.application.command.ActivateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.DeactivateUserCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.CreateUserParams;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserNotFoundException;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserValidationException;
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
public class UserCommandServiceImpl {
    private final UserRepository userRepository;
    private final AuthSessionRepository authSessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User handleCreateUser(CreateUserCommand command) {
        log.info("Creating new user with email: {}", command.email());
        if (userRepository.existsByEmail(command.email())) {
            throw new UserValidationException("User with this email already exists");
        }

        if (userRepository.existsByPhoneNumber(command.phoneNumber())) {
            throw new UserValidationException("User with this phoneNumber already exists");
        }

        log.info("Encoding password for new user");
        var passwordHashed = passwordEncoder.encode(command.password().value());

        log.info("Preparing user creation parameters");
        var createParams = command.toCreateParams(passwordHashed);

        log.info("Creating user entity");
        User user = User.createUser(createParams);
        user.assignRoles(Set.of(command.role()));

        log.info("Saving new user to repository");
        User createdUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", createdUser.getId().value());
        return createdUser;
    }


    @Transactional
    public User handleUpdatePersonalData(UpdateUserPersonalDataCommand command) {
        log.info("Updating profile for user: {}", command.userId());
        User user = findUserById(command.userId());

        user.updatePersonalData(command.toPersonalData());

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
}
