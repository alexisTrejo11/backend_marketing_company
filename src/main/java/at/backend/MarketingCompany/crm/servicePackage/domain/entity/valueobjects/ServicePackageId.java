package at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects;

import at.backend.MarketingCompany.common.exceptions.MissingFieldException;

import java.util.UUID;

public record ServicePackageId(String value) {
    public ServicePackageId {
        if (value == null || value.isBlank()) {
            throw new MissingFieldException("ServicePackageId", "value");
        }
    }

    public static ServicePackageId generate() {
        return new ServicePackageId(UUID.randomUUID().toString());
    }

    public static ServicePackageId of(UUID value) {
        return new ServicePackageId(value.toString());
    }

    public static ServicePackageId of(String value) {
        return new ServicePackageId(value);
    }
}