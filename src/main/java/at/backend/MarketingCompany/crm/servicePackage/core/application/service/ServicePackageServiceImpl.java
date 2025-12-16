package at.backend.MarketingCompany.crm.servicePackage.core.application.service;

import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.ServicePackageNotFoundException;
import at.backend.MarketingCompany.crm.servicePackage.core.ports.input.ServicePackageService;
import at.backend.MarketingCompany.crm.servicePackage.core.ports.output.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements ServicePackageService {
    private final ServicePackageRepository repository;

    @Override
    @Transactional
    public ServicePackage createServicePackage(CreateServicePackageCommand command) {
        log.info("[CreateServicePackageCommand] Creating new service package with name: {}", command.name());
        var servicePackage = ServicePackage.create(command.toCreateParams());

        log.info("Persisting new service package with name: {}", command.name());
        ServicePackage savedPackageService =  repository.save(servicePackage);

        log.info("Service package created successfully with id: {}", savedPackageService.getId());
        return savedPackageService;
    }

    @Override
    @Transactional
    public ServicePackage updateServicePackage(UpdateServicePackageCommand command) {
        log.info("[UpdateServicePackageCommand] Updating service package with id: {}", command.id());

        ServicePackage servicePackage = repository.findById(command.id())
                .orElseThrow(() -> new ServicePackageNotFoundException(command.id()));

        log.info("Found service package: {}, updating fields", servicePackage.getName());

        servicePackage.update(command.toUpdateParams());
        log.info("Persisting updated service package with id: {}", command.id());
        ServicePackage updatedPackage = repository.save(servicePackage);

        log.info("Service package updated successfully with id: {}", command.id());
        return updatedPackage;
    }

    @Override
    @Transactional
    public ServicePackage deleteServicePackage(DeleteServicePackageCommand command) {
        ServicePackage servicePackage = repository.findById(command.id())
                .orElseThrow(() -> new ServicePackageNotFoundException(command.id()));

        servicePackage.softDelete();
        return repository.save(servicePackage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServicePackage> getAllServicePackage(GetAllServicePackageQuery query) {
        return repository.findAll(query.pageable());
    }

    @Override
    @Transactional(readOnly = true)
    public ServicePackage getServicePackage(GetServicePackageQuery query) {
        return repository.findById(query.id())
                .orElseThrow(() -> new ServicePackageNotFoundException(query.id()));
    }
}
