package at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;

public record DeleteServicePackageCommand(ServicePackageId id) {

    public static DeleteServicePackageCommand of(String id) {
        return new DeleteServicePackageCommand(ServicePackageId.of(id));
    }
}
