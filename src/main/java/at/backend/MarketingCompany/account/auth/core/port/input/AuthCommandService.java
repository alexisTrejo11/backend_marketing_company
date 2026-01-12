package at.backend.MarketingCompany.account.auth.core.port.input;

import at.backend.MarketingCompany.account.auth.core.application.commands.*;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.AuthResult;

public interface AuthCommandService {
    AuthResult handleSignUp(SignUpCommand command);

    AuthResult handleLogin(LoginCommand command);

    AuthResult handleRefreshToken(RefreshSessionCommand command);

    void handleLogout(LogoutCommand command);

    void handleLogoutAll(LogoutAllCommand command);

    void handleChangePassword(ChangePasswordCommand command);
}
