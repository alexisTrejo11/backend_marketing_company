package at.backend.MarketingCompany.account.user.core.domain.exceptions;

import at.backend.MarketingCompany.shared.exception.DomainException;

import java.util.Map;

public class UserException extends DomainException {
    public UserException(String message) {
        super(message, "USER_ERROR");
    }

    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UserException(String message, String errorCode, Map<String, Object> details) {
        super(message, errorCode, details);
    }

    public UserException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}
