package at.backend.MarketingCompany.account.user.core.application;

import at.backend.MarketingCompany.account.auth.core.application.commands.UpdateProfileCommand;
import at.backend.MarketingCompany.account.auth.core.port.output.AuthSessionRepository;
import at.backend.MarketingCompany.account.user.core.application.command.ActivateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.DeactivateUserCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.PersonalData;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserNotFoundException;
import at.backend.MarketingCompany.account.user.core.ports.output.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl {
    private final UserRepository userRepository;
    private final AuthSessionRepository authSessionRepository;


    @Transactional
    public User handle(UpdateProfileCommand command) {
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
