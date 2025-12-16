package at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.controller;

import at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto.CreateServicePackageInput;
import at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto.ServicePackageOutput;
import at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.dto.UpdateServicePackageInput;
import at.backend.MarketingCompany.crm.servicePackage.adapter.input.graphql.mapper.ServicePackageOutputMapper;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.CreateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.DeleteServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.command.UpdateServicePackageCommand;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetAllServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.application.dto.query.GetServicePackageQuery;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.core.ports.input.ServicePackageService;
import at.backend.MarketingCompany.shared.PageResponse;
import at.backend.MarketingCompany.shared.dto.PageInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ServicePackageController {
  private final ServicePackageService service;
  private final ServicePackageOutputMapper mapper;

  @QueryMapping
  public PageResponse<ServicePackageOutput> servicePackages(@Argument @Valid @NotNull PageInput input) {
    var query = new GetAllServicePackageQuery(input.toPageable());
    Page<ServicePackage> servicePackagePage = service.getAllServicePackage(query);

    return mapper.toPageOutput(servicePackagePage);
  }

  @QueryMapping
  public ServicePackageOutput servicePackage(@Argument @Valid @NotNull String id) {
    var query = GetServicePackageQuery.of(id);
    ServicePackage servicePackage = service.getServicePackage(query);

    return mapper.toOutput(servicePackage);
  }

  @MutationMapping
  public ServicePackageOutput createServicePackage(@Argument @Valid @NotNull CreateServicePackageInput input) {
    CreateServicePackageCommand command = input.toCommand();
    ServicePackage servicePackage = service.createServicePackage(command);
    return mapper.toOutput(servicePackage);
  }

  @MutationMapping
  public ServicePackageOutput updateServicePackage(@Argument @Valid @NotNull UpdateServicePackageInput input) {
    var command = input.toCommand();
    ServicePackage servicePackage = service.updateServicePackage(command);
    return mapper.toOutput(servicePackage);
  }

  @MutationMapping
  public Boolean deleteServicePackage(@Argument @Valid @NotNull String id) {
    var command = DeleteServicePackageCommand.of(id);
    service.deleteServicePackage(command);
    return true;
  }
}
