package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.controller;

import at.backend.MarketingCompany.config.ratelimit.base.GraphQLRateLimit;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import io.jsonwebtoken.Claims;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UpdatePersonalDataInput;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper.UserResponseMapper;
import at.backend.MarketingCompany.account.user.core.application.command.SoftDeleteUserCommand;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.domain.entity.valueobject.UserId;
import at.backend.MarketingCompany.account.user.core.ports.input.UserCommandService;
import at.backend.MarketingCompany.account.user.core.ports.input.UserQueryService;
import at.backend.MarketingCompany.shared.AppContext;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;
  private final UserResponseMapper mapper;

  @QueryMapping
  @GraphQLRateLimit("user-operation")
  @PreAuthorize("isAuthenticated()")
  public UserResponse me(DataFetchingEnvironment env) {
    UserId userId = AppContext.getUserIdFromContext(env);

    var query = new GetUserByIdQuery(userId);
    User user = userQueryService.getUserById(query);

    return mapper.toUserResponse(user);
  }

  @MutationMapping
  @GraphQLRateLimit("user-operation")
  @PreAuthorize("isAuthenticated()")
  public UserResponse updateMyPersonalData(
      DataFetchingEnvironment env,
      @Argument @Valid UpdatePersonalDataInput input) {

    UserId userId = AppContext.getUserIdFromContext(env);

    var command = input.toCommand(userId);
    User updatedUser = userCommandService.handleUpdatePersonalData(command);

    return mapper.toUserResponse(updatedUser);
  }

  @MutationMapping
  @GraphQLRateLimit("user-operation")
  @PreAuthorize("isAuthenticated()")
  public Boolean deleteMyAccount(DataFetchingEnvironment env) {
    boolean userAction = true;
    UserId userId = AppContext.getUserIdFromContext(env);

    var command = new SoftDeleteUserCommand(userId, userAction);
    userCommandService.handleSoftDeleteUser(command);

    return true;
  }
}
