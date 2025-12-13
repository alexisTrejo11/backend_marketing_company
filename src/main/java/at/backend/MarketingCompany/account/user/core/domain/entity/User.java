package at.backend.MarketingCompany.account.user.core.domain.entity;

import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.HashedPassword;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.*;
import at.backend.MarketingCompany.account.user.core.domain.exceptions.UserValidationException;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class User extends BaseDomainEntity<UserId> {
  private Email email;
  private PhoneNumber phoneNumber;
  private HashedPassword hashedPassword;
  private PersonalData personalData;
  private Set<Role> roles;
  private UserStatus status;
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
    this.status = params.status();
    this.personalData = params.personalData();
    this.roles = Set.copyOf(params.roles());
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
    newUser.personalData = params.personalData();
    newUser.status = params.status();
    newUser.createdAt = LocalDateTime.now();
    newUser.updatedAt = LocalDateTime.now();
    newUser.version = 1;

    return newUser;
  }

  public void assignRoles(Set<Role> roles) {
    if (this.roles != null) {
      throw new UserValidationException("Roles have already been assigned");
    }

    if (roles == null || roles.isEmpty()) {
      throw new UserValidationException("At least one role is required");
    }

    this.roles = Set.copyOf(roles);
  }

  public void updatePersonalData(PersonalData personalData) {
    if (personalData == null) {
      throw new UserValidationException("Personal data cannot be null to update");
    }

    this.personalData = personalData;
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

  public void activate() {
    this.status = UserStatus.ACTIVE;
    updateTimestamp();
  }

  public void deactivate() {
    this.status = UserStatus.INACTIVE;
    updateTimestamp();
  }

  public void ban() {
    this.status = UserStatus.SUSPENDED;
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
    return this.status.equals(UserStatus.ACTIVE);
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
    if (personalData == null) {
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
    if (params.personalData() == null) {
      throw new UserValidationException("Name is required");
    }

    if (params.status() == null) {
      throw new UserValidationException("User status is required");
    }

    if (params.status() != UserStatus.ACTIVE && params.status() != UserStatus.PENDING_ACTIVATION) {
      throw new UserValidationException("Invalid user status for creation, must be ACTIVE or PENDING_ACTIVATION");
    }
  }

  private void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

}
