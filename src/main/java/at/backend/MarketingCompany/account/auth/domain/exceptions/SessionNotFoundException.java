package at.backend.MarketingCompany.account.auth.domain.exceptions;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String sessionId) {
        super("Session not found with ID: " + sessionId);
    }
}