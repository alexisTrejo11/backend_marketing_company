package at.backend.MarketingCompany.account.user.domain.entity;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.*;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.*;
import at.backend.MarketingCompany.account.user.domain.exceptions.UserValidationException;
import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class User extends BaseDomainEntity<UserId> {
    private Email email;
    private PhoneNumber phoneNumber;
    private HashedPassword hashedPassword;
    private PersonName name;
    private Set<Role> roles;
    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean active;
    private LocalDateTime lastLoginAt;
    private LocalDateTime passwordChangedAt;


    private User(UserId userId) {
        super(userId);
    }

    private User(UserReconstructParams params) {
        super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
        this.email = params.email();
        this.phoneNumber = params.phoneNumber();
        this.hashedPassword = params.hashedPassword();
        this.name = params.name();
        this.roles = Set.copyOf(params.roles());
        this.emailVerified = params.emailVerified();
        this.phoneVerified = params.phoneVerified();
        this.active = params.active();
        this.lastLoginAt = params.lastLoginAt();
        this.passwordChangedAt = params.passwordChangedAt();
        validateState();
    }

    public static User reconstruct(UserReconstructParams params) {
        return new User(params);
    }

    public static User createUser(CreateUserParams params) {
        validateCreationParams(params);

        User newUser = new User(UserId.generate());
        newUser.email = params.email();
        newUser.phoneNumber = params.phoneNumber();
        newUser.hashedPassword = params.hashedPassword();
        newUser.name = params.name();
        newUser.roles = Set.of(Role.USER); // Default role
        newUser.emailVerified = false;
        newUser.phoneVerified = false;
        newUser.active = true;
        newUser.createdAt = LocalDateTime.now();
        newUser.updatedAt = LocalDateTime.now();
        newUser.version = 1;

        return newUser;
    }

    public static User createAdmin(CreateUserParams params) {
        User admin = createUser(params);
        admin.roles = Set.of(Role.USER, Role.ADMIN);
        return admin;
    }

    // ===== BUSINESS LOGIC =====
    public void updateProfile(PersonName name, PhoneNumber phoneNumber) {
        if (name == null) {
            throw new UserValidationException("Name is required");
        }

        this.name = name;
        this.phoneNumber = phoneNumber;
        updateTimestamp();
    }

    public void changePassword(HashedPassword newHashedPassword) {
        if (newHashedPassword == null) {
            throw new UserValidationException("New password is required");
        }

        this.hashedPassword = newHashedPassword;
        this.passwordChangedAt = LocalDateTime.now();
        updateTimestamp();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        updateTimestamp();
    }

    public void verifyPhone() {
        this.phoneVerified = true;
        updateTimestamp();
    }

    public void markAsActive() {
        this.active = true;
        updateTimestamp();
    }

    public void markAsInactive() {
        this.active = false;
        updateTimestamp();
    }

    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
        updateTimestamp();
    }

    public void addRole(Role role) {
        if (role == null) {
            throw new UserValidationException("Role cannot be null");
        }
        this.roles.add(role);
        updateTimestamp();
    }

    public void removeRole(Role role) {
        if (role == null) {
            throw new UserValidationException("Role cannot be null");
        }
        this.roles.remove(role);
        updateTimestamp();
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public boolean isAdmin() {
        return this.roles.contains(Role.ADMIN);
    }

    public boolean canLogin() {
        return this.active && this.emailVerified;
    }

    public boolean isPasswordExpired() {
        if (passwordChangedAt == null) {
            return false;
        }
        return passwordChangedAt.isBefore(LocalDateTime.now().minusDays(90));
    }

    private void validateState() {
        if (email == null) {
            throw new UserValidationException("Email is required");
        }
        if (hashedPassword == null) {
            throw new UserValidationException("Password is required");
        }
        if (name == null) {
            throw new UserValidationException("Name is required");
        }
        if (roles == null || roles.isEmpty()) {
            throw new UserValidationException("At least one role is required");
        }
    }

    private static void validateCreationParams(CreateUserParams params) {
        if (params == null) {
            throw new UserValidationException("Creation parameters cannot be null");
        }
        if (params.email() == null) {
            throw new UserValidationException("Email is required");
        }
        if (params.hashedPassword() == null) {
            throw new UserValidationException("Password is required");
        }
        if (params.name() == null) {
            throw new UserValidationException("Name is required");
        }
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public void markAsDeleted() {
        super.markAsDeleted();
    }
}