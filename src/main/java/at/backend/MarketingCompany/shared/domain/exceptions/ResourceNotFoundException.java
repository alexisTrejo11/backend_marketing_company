package at.backend.MarketingCompany.shared.domain.exceptions;


public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String resourceType, Long id) {
        super(resourceType + " not found with ID: " + id, "RESOURCE_NOT_FOUND");
    }
}