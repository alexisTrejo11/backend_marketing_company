package at.backend.MarketingCompany.account.user.core.ports.input;

import at.backend.MarketingCompany.account.user.core.application.command.CreateUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.RestoreUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.SoftDeleteUserCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserPersonalDataCommand;
import at.backend.MarketingCompany.account.user.core.application.command.UpdateUserStatusCommand;
import at.backend.MarketingCompany.account.user.core.domain.entity.User;

public interface UserCommandService {
  User handleCreateUser(CreateUserCommand command);

  User handleUpdatePersonalData(UpdateUserPersonalDataCommand command);

  User handleSoftDeleteUser(SoftDeleteUserCommand command);

  User handleRestoreUser(RestoreUserCommand command);

  User handleActivateUser(UpdateUserStatusCommand command);

  User handleBanUser(UpdateUserStatusCommand command);
}
