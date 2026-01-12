package at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions;

public class ServicePackageAlreadyExistsException extends RuntimeException {
    public ServicePackageAlreadyExistsException(String name) {
        super(String.format("Service package already exists with name: %s", name));
    }
}