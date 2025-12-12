package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserStatisticsResponse;
import at.backend.MarketingCompany.account.user.core.application.UserStatistics;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.shared.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserGraphQLMapper {

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
                user.getId().value(),
                user.getEmail().value(),
                user.getPersonalData().firstName(),
                user.getPersonalData().lastName(),
                user.getPhoneNumber() != null ? user.getPhoneNumber().value() : null,
                user.getRoles(),
                user.getStatus(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        if (users == null) return List.of();

        return users.stream()
                .map(this::toUserResponse)
                .toList();
    }


    public PageResponse<UserResponse> toUserPageResponse(Page<User> userPage) {
        if (userPage == null) return PageResponse.empty();

        return PageResponse.of(userPage.map(this::toUserResponse));
    }

    public UserStatisticsResponse toUserStatisticsResponse(
            UserStatistics statistics) {
        return new UserStatisticsResponse(
                statistics.totalUsers(),
                statistics.activeUsers(),
                statistics.adminUsers(),
                statistics.totalSessions()
        );
    }

}
