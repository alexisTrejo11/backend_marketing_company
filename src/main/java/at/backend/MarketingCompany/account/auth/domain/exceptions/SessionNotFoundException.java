package at.backend.MarketingCompany.account.auth.domain.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String refreshToken) {
        super("Session not found with ID: " + refreshToken.substring(0, 8) + "...");
    }
}