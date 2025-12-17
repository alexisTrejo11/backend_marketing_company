package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.PersonalDataResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserStatisticsResponse;
import at.backend.MarketingCompany.account.user.core.application.UserStatistics;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class UserResponseMapper {
  public UserResponse toUserResponse(User user) {
    if (user == null)
      return null;

    var personalData = PersonalDataResponse.fromDomain(user.getPersonalData());
    var email = user.getEmail() != null ? user.getEmail().value() : null;
    var phoneNumber = user.getPhoneNumber() != null ? user.getPhoneNumber().value() : null;

    return UserResponse.builder()
        .id(user.getId() != null ? user.getId().asString() : null)
        .email(email)
        .phoneNumber(phoneNumber)
        .roles(user.getRoles())
        .status(user.getStatus())
        .personalData(personalData)
        .lastLoginAt(user.getLastLoginAt() != null
            ? user.getLastLoginAt().atOffset(OffsetDateTime.now().getOffset())
            : null)
        .createdAt(user.getCreatedAt() != null
            ? user.getCreatedAt().atOffset(OffsetDateTime.now().getOffset())
            : null)
        .updatedAt(user.getUpdatedAt() != null
            ? user.getUpdatedAt().atOffset(OffsetDateTime.now().getOffset())
            : null)
        .build();
  }

  public List<UserResponse> toUserResponseList(List<User> users) {
    if (users == null)
      return List.of();

    return users.stream()
        .map(this::toUserResponse)
        .toList();
  }

  public PageResponse<UserResponse> toUserPageResponse(Page<User> userPage) {
    if (userPage == null)
      return PageResponse.empty();

    return PageResponse.of(userPage.map(this::toUserResponse));
  }

  public UserStatisticsResponse toUserStatisticsResponse(
      UserStatistics statistics) {
    return new UserStatisticsResponse(
        statistics.totalUsers(),
        statistics.activeUsers(),
        statistics.adminUsers(),
        statistics.totalSessions());
  }

}
