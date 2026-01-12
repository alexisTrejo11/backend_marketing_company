package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.controller;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.CreateUserInput;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UpdatePersonalDataInput;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserStatisticsResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper.UserResponseMapper;
import at.backend.MarketingCompany.account.user.core.application.UserStatistics;
import at.backend.MarketingCompany.account.user.core.application.command.RestoreUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.SoftDeleteUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserStatusCommand;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByEmailQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserStatisticsQuery;
import at.backend.MarketingCompany.account.user.core.application.queries.SearchUsersQuery;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.ports.input.UserCommandService;
import at.backend.MarketingCompany.account.user.core.ports.input.UserQueryService;
import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;

@Controller
@RequiredArgsConstructor
public class UserManagerController {
  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;
  private final UserResponseMapper mapper;

  @MutationMapping
  @GraphQLRateLimit("strict")
  public UserResponse createUser(@Argument @Valid CreateUserInput input) {
    var command = input.toCommand();
    User createdUser = userCommandService.handleCreateUser(command);
    return mapper.toUserResponse(createdUser);
  }

  @MutationMapping
  @GraphQLRateLimit("strict")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse updateUserPersonalData(@Argument @Valid UpdatePersonalDataInput input,
      @Argument @Valid @NotBlank String userId) {

    var command = input.toCommand(UserId.of(userId));
    User updatedUser = userCommandService.handleUpdatePersonalData(command);
    return mapper.toUserResponse(updatedUser);
  }

  @MutationMapping
  @GraphQLRateLimit("strict")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse banUser(@Argument @Valid @NotBlank String id) {
    var command = UpdateUserStatusCommand.from(id);
    User user = userCommandService.handleBanUser(command);
    return mapper.toUserResponse(user);
  }

  @MutationMapping
  @GraphQLRateLimit("strict")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse activateUser(@Argument @Valid @NotBlank String id) {
    var command = UpdateUserStatusCommand.from(id);
    User user = userCommandService.handleActivateUser(command);
    return mapper.toUserResponse(user);
  }

  @MutationMapping
  @GraphQLRateLimit("strict")
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse restoreUser(@Argument @Valid @NotBlank String id) {
    var command = RestoreUserCommand.from(id);
    User user = userCommandService.handleRestoreUser(command);
    return mapper.toUserResponse(user);
  }

  @MutationMapping
  @GraphQLRateLimit("strict")
  @PreAuthorize("hasRole('ADMIN')")
  public Boolean deleteUser(@Argument @Valid @NotBlank String id) {
    // Is Admin Action not user action
    boolean isUserAction = false;

    var command = SoftDeleteUserCommand.from(id, isUserAction);
    userCommandService.handleSoftDeleteUser(command);
    return true;
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasRole('ADMIN')")
  public PageResponse<UserResponse> getAllUsers(@Argument @Valid @NotNull PageInput pageInput) {
    var searchUsersQuery = new SearchUsersQuery(pageInput.toPageable());
    Page<User> usersPage = userQueryService.handleSearchUsers(searchUsersQuery);
    return mapper.toUserPageResponse(usersPage);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserById(@Argument @Valid @NotBlank String id) {
    var query = GetUserByIdQuery.from(id);
    User user = userQueryService.getUserById(query);
    return mapper.toUserResponse(user);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasRole('ADMIN')")
  public UserResponse getUserByEmail(@Argument @Valid @NotBlank String email) {
    var query = GetUserByEmailQuery.from(email);
    User user = userQueryService.handleGetUserByEmail(query);
    return mapper.toUserResponse(user);
  }

  @QueryMapping
  @GraphQLRateLimit
  @PreAuthorize("hasRole('ADMIN')")
  public UserStatisticsResponse userStatistics() {
    var query = new GetUserStatisticsQuery();
    UserStatistics statistics = userQueryService.handleGetUserStatistics(query);
    return mapper.toUserStatisticsResponse(statistics);
  }
}
