package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.controller;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserStatisticsResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper.UserGraphQLMapper;
import at.backend.MarketingCompany.account.user.core.application.UserQueryServiceImpl;
import at.backend.MarketingCompany.account.user.core.application.command.ActivateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.DeactivateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByEmailQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserStatisticsQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.SearchUsersQuery;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserQueryServiceImpl userService;
    private final UserGraphQLMapper mapper;

    @MutationMapping
    public UserResponse deactivateUser(@Argument String userId) {
        log.info("Deactivate user request: {}", userId);

        var deactivateUserCommand = DeactivateUserCommand.from(userId);
        var result = userService.handle(deactivateUserCommand);

        return mapper.toUserResponse(result);
    }

    @MutationMapping
    public UserResponse activateUser(@Argument String userId) {
        log.info("Activate user request: {}", userId);

        var activateUserCommand = ActivateUserCommand.from(userId);
        var result = userService.handle(activateUserCommand);

        return mapper.toUserResponse(result);
    }

    @QueryMapping
    public PageResponse<UserResponse> searchUsers(PageInput pageInput) {
        log.debug("Get all users request");

        var searchUsersQuery = new SearchUsersQuery(pageInput.toPageable());
        var users = userService.handle(searchUsersQuery);

        return mapper.toUserPageResponse(users);
    }

    @QueryMapping
    public UserResponse getUserById(@Argument String userId) {
        log.debug("Get user by ID request: {}", userId);

        var query = GetUserByIdQuery.from(userId);
        var user = userService.handle(query);

        return mapper.toUserResponse(user);
    }

    @QueryMapping
    public UserResponse getUserByEmail(@Argument String email) {
        log.debug("Get user by email request: {}", email);

        var query = GetUserByEmailQuery.from(email);
        var user = userService.handle(query);

        return mapper.toUserResponse(user);
    }


    @QueryMapping
    public UserStatisticsResponse userStatistics() {
        log.debug("Get user statistics request");

        var query = new GetUserStatisticsQuery();
        var statistics = userService.handle(query);

        return mapper.toUserStatisticsResponse(statistics);
    }
}
