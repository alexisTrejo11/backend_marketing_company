package at.backend.MarketingCompany.crm.deal.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.deal.infrastructure.DTOs.DealInput;
import at.backend.MarketingCompany.crm.deal.v2.infrastructure.persistence.DealEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DealMappers {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerModel", ignore = true)
    @Mapping(target = "opportunity", ignore = true)
    @Mapping(target = "campaignManager", ignore = true)
    DealEntity inputToEntity(DealInput input);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerModel", ignore = true)
    @Mapping(target = "opportunity", ignore = true)
    @Mapping(target = "campaignManager", ignore = true)
    DealEntity inputToUpdatedEntity(@MappingTarget DealEntity existingUser, DealInput input);
         
}
