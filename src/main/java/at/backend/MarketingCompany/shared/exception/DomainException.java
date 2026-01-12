package at.backend.MarketingCompany.shared.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DomainException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    public DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = Map.of();
    }

    public DomainException(String message, String errorCode, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = Map.of();
    }
}

