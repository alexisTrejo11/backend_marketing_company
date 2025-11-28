package at.backend.MarketingCompany.crm.servicePackage.application.dto.command;

import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

public record DeleteServicePackageCommand(ServicePackageId id) {
}
