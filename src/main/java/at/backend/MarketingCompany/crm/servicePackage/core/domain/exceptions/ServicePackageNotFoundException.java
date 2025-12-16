package at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public class ServicePackageNotFoundException extends RuntimeException {
    public ServicePackageNotFoundException(ServicePackageId id) {
        super(String.format("Service package not found with id: %s", id.value()));
    }
}
