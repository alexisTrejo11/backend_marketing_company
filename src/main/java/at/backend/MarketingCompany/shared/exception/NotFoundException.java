package at.backend.MarketingCompany.shared.exception;

import java.util.Map;

public class NotFoundException extends DomainException {
    public NotFoundException(String resource, String identifier) {
        super(
            String.format("%s not found with ID: %s", resource, identifier),
            "RESOURCE_NOT_FOUND",
            Map.of("resource", resource, "identifier", identifier)
        );
    }
}
