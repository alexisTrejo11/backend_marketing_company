package at.backend.MarketingCompany.account.user.domain.exceptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}