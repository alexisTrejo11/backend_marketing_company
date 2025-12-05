package at.backend.MarketingCompany.account.user.adapters.outbound.persistence;

import at.backend.MarketingCompany.account.auth.domain.entitiy.valueobject.Role;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.UserStatus;
import at.backend.MarketingCompany.common.jpa.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_user_active", columnList = "active"),
    @Index(name = "idx_user_created", columnList = "createdAt")
})
public class UserEntity extends BaseJpaEntity {
  @Column(name = "email", nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @Column(name = "hashed_password", nullable = false, length = 100)
  private String hashedPassword;

  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Column(name = "status", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 20)
  private Set<Role> roles;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "password_changed_at")
  private LocalDateTime passwordChangedAt;

  public UserEntity(String id) {
    this.setId(id);
  }

  public void markAsDeleted() {
    this.setDeletedAt(LocalDateTime.now());
    this.status = UserStatus.INACTIVE;
  }

}
