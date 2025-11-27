package at.backend.MarketingCompany.crm.servicePackage.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.servicePackage.infrastructure.DTOs.ServicePackageInput;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServicePackageMappers {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServicePackageEntity inputToEntity(ServicePackageInput input);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServicePackageEntity inputToUpdatedEntity(@MappingTarget ServicePackageEntity existingUser, ServicePackageInput input);
         
}
