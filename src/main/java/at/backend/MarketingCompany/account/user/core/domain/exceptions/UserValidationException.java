package at.backend.MarketingCompany.account.user.core.domain.exceptions;


public class UserValidationException extends UserException {
    public UserValidationException(String message) {
        super(message);
    }
}