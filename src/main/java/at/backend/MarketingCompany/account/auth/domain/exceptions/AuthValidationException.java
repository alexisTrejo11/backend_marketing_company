package at.backend.MarketingCompany.account.auth.domain.exceptions;

public class AuthValidationException extends RuntimeException {
    public AuthValidationException(String message) {
        super(message);
    }
}