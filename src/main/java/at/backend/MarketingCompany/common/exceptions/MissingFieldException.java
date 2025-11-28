package at.backend.MarketingCompany.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String className, String field) {
        super("Missing required field '" + field + "' in " + className);
    }

    public MissingFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
