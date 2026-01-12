package at.backend.MarketingCompany.shared.domain.exceptions;

public class InvalidInputException extends BaseException {

    public InvalidInputException(String message) {
        super(message, "INVALID_INPUT");
    }
}