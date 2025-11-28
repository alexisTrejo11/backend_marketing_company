package at.backend.MarketingCompany.crm.servicePackage.v2.application.dto.command;

import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.ServicePackageId;

public record DeleteServicePackageCommand(ServicePackageId id) {
}
