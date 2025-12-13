package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UpdatePersonalDataInput;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto.UserResponse;
import at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.mapper.UserResponseMapper;
import at.backend.MarketingCompany.account.user.core.application.command.SoftDeleteUserCommand;
import at.backend.MarketingCompany.account.user.core.application.queries.GetUserByIdQuery;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;
import at.backend.MarketingCompany.account.user.core.ports.input.UserCommandService;
import at.backend.MarketingCompany.account.user.core.ports.input.UserQueryService;
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
  public UserResponse me(@Argument @Valid @NotBlank String userId) {
    User user = userQueryService.handleGetUserById(GetUserByIdQuery.from(userId));
    return mapper.toUserResponse(user);
  }

  @MutationMapping
  public UserResponse updateMyPersonalData(@Argument @Valid UpdatePersonalDataInput input) {
    var command = input.toCommand();

    User updatedUser = userCommandService.handleUpdatePersonalData(command);

    return mapper.toUserResponse(updatedUser);
  }

  @MutationMapping
  public Boolean deleteMyAccount(@Argument @Valid @NotBlank String userId) {
    var command = SoftDeleteUserCommand.from(userId);
    userCommandService.handleSoftDeleteUser(command);

    return true;
  }
}
