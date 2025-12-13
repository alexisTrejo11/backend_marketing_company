package at.backend.MarketingCompany.crm.servicePackage.application.service;

import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.query.GetServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.application.mapper.ServicePackageMapper;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.domain.exceptions.ServicePackageNotFoundException;
import at.backend.MarketingCompany.crm.servicePackage.domain.repository.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import at.backend.MarketingCompany.crm.servicePackage.application.dto.ServicePackageResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements ServicePackageServices {
  private final ServicePackageRepository repository;
  private final ServicePackageMapper mapper;

  @Transactional
  public ServicePackageResponse handle(CreateServicePackageCommand command) {
    ServicePackage servicePackage = ServicePackage.create(command.toCreateParams());

    ServicePackage savedPackage = repository.save(servicePackage);
    return mapper.toResponse(savedPackage);
  }

  @Override
  public ServicePackageResponse handle(DeleteServicePackageCommand command) {
    ServicePackage servicePackage = repository.findById(command.id())
        .orElseThrow(() -> {
          log.warn("[DeleteServicePackageCommand] Service package not found with id: {}", command.id());
          return new ServicePackageNotFoundException(command.id());
        });

    servicePackage.softDelete();
    ServicePackage deletedService = repository.save(servicePackage);

    return mapper.toResponse(deletedService);
  }

  @Override
  public Page<ServicePackageResponse> handle(GetAllServicePackageQuery query) {
    return repository.findAll(query.pageable())
        .map(mapper::toResponse);
  }

  @Override
  public ServicePackageResponse handle(GetServicePackageQuery query) {
    ServicePackage servicePackage = repository.findById(query.id())
        .orElseThrow(() -> {
          log.warn("[GetServicePackageQuery] Service package not found with id: {}", query.id());
          return new ServicePackageNotFoundException(query.id());
        });
    return mapper.toResponse(servicePackage);
  }

  @Override
  @Transactional
  public ServicePackageResponse handle(UpdateServicePackageCommand command) {
    log.debug("[UpdateServicePackageCommand] Updating service package with id: {}", command.id());

    ServicePackage servicePackage = repository.findById(command.id())
        .orElseThrow(() -> {
          log.warn("[UpdateServicePackageCommand] Service package not found with id: {}", command.id());
          return new ServicePackageNotFoundException(command.id());
        });

    log.debug("Found service package: {}, updating fields", servicePackage.getName());

    servicePackage.update(command.toUpdateParams());
    log.debug("Persisting updated service package with id: {}", command.id());
    ServicePackage updatedPackage = repository.save(servicePackage);

    log.debug("Service package updated successfully with id: {}", command.id());
    return mapper.toResponse(updatedPackage);
  }
}
