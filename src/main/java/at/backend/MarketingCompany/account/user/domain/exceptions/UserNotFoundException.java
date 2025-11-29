package at.backend.MarketingCompany.account.user.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User not found with ID: " + userId);
    }
    
    public UserNotFoundException(Email email) {
        super("User not found with email: " + email.value());
    }
}