package at.backend.MarketingCompany.account.user.core.ports.input;

import org.springframework.data.domain.Page;

import at.backend.MarketingCompany.account.user.core.application.UserStatistics;
import at.backend.MarketingCompany.account.user.core.application.queries.GetActiveUsersQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByEmailQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserStatisticsQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUsersByRoleQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.SearchUsersQuery;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;

public interface UserQueryService {
  User handleGetUserById(GetUserByIdQuery query);

  User handleGetUserByEmail(GetUserByEmailQuery query);

  Page<User> handleSearchUsers(SearchUsersQuery query);

  Page<User> handleGetActiveUsers(GetActiveUsersQuery query);

  Page<User> handleGetUsersByRole(GetUsersByRoleQuery query);

  UserStatistics handleGetUserStatistics(GetUserStatisticsQuery query);
}
