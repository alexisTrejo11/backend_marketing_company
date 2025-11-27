package at.backend.MarketingCompany.crm.servicePackage.api.controller;

import at.backend.MarketingCompany.common.service.CommonService;
import at.backend.MarketingCompany.common.utils.PageInput;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.DTOs.ServicePackageInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ServicePackageController {
    private final CommonService<ServicePackageEntity, ServicePackageInput, Long> service;

    @QueryMapping
    public Page<ServicePackageEntity> getAllServicePackages(@Argument PageInput input) {
        Pageable pageable = PageRequest.of(input.page(), input.size());

        return service.getAll(pageable);
    }

    @QueryMapping
    public ServicePackageEntity getServicePackageById(@Argument Long id) {
        return service.getById(id);
    }

    @MutationMapping
    public ServicePackageEntity createServicePackage(@Valid @Argument ServicePackageInput input) {
        service.validate(input);

        return service.create(input);
    }

    @MutationMapping
    public ServicePackageEntity updateServicePackage(@Valid @Argument Long id, @Argument ServicePackageInput input) {
        service.validate(input);

        return service.update(id, input);
    }

    @MutationMapping
    public boolean deleteServicePackage(@Argument Long id) {
        service.delete(id);
        return true;
    }
}
