package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params.CreateServicePackageParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params.ServicePackageReconstructParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.params.UpdateServicePackageParams;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.EstimatedHours;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Price;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ProjectDuration;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.RecurrenceInfo;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServicePackageId;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.ServicePackageValidationException;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

@Getter
public class ServicePackage extends BaseDomainEntity<ServicePackageId> {
  private String name;
  private String description;
  private Price price;
  private ServiceType serviceType;
  private String deliverables;
  private EstimatedHours estimatedHours;
  private Complexity complexity;
  private RecurrenceInfo recurrenceInfo;
  private ProjectDuration projectDuration;
  private List<String> kpis;
  private List<SocialNetworkPlatform> socialNetworkPlatforms;
  private Boolean active;

  private ServicePackage(ServicePackageId id) {
    this.id = id;
    // Avoid NPE by initializing collections and value objects
    this.kpis = new ArrayList<>();
    this.socialNetworkPlatforms = new ArrayList<>();
    this.projectDuration = ProjectDuration.none();
    this.recurrenceInfo = RecurrenceInfo.none();
    this.estimatedHours = EstimatedHours.none();
    this.active = true;

  }

  public static ServicePackage create(CreateServicePackageParams params) {
    var servicePackage = new ServicePackage(ServicePackageId.generate());

    servicePackage.name = params.name();
    servicePackage.description = params.description();
    servicePackage.price = params.price();
    servicePackage.serviceType = params.serviceType();
    servicePackage.deliverables = params.deliverables();
    servicePackage.estimatedHours = params.estimatedHours();
    servicePackage.complexity = params.complexity();
    servicePackage.recurrenceInfo = params.recurrenceInfo();
    servicePackage.projectDuration = params.projectDuration();
    servicePackage.active = true;

    servicePackage.kpis = new ArrayList<>(
        params.kpis() != null ? params.kpis() : new ArrayList<>());

    servicePackage.socialNetworkPlatforms = new ArrayList<>(
        params.socialNetworkPlatforms() != null ? params.socialNetworkPlatforms() : new ArrayList<>());
    return servicePackage;
  }

  public static ServicePackage reconstruct(ServicePackageReconstructParams params) {
    var servicePackage = new ServicePackage(params.id());

    servicePackage.name = params.name();
    servicePackage.description = params.description();
    servicePackage.price = params.price();
    servicePackage.serviceType = params.serviceType();
    servicePackage.deliverables = params.deliverables();
    servicePackage.estimatedHours = params.estimatedHours();
    servicePackage.complexity = params.complexity();
    servicePackage.recurrenceInfo = params.recurrenceInfo();
    servicePackage.projectDuration = params.projectDuration();
    servicePackage.active = params.active();
    servicePackage.createdAt = params.createdAt();
    servicePackage.updatedAt = params.updatedAt();
    servicePackage.deletedAt = params.deletedAt();
    servicePackage.version = params.version();

    servicePackage.kpis = new ArrayList<>(params.kpis() != null ? params.kpis() : new ArrayList<>());

    servicePackage.socialNetworkPlatforms = new ArrayList<>(
        params.socialNetworkPlatforms() != null ? params.socialNetworkPlatforms() : new ArrayList<>());
    return servicePackage;
  }

  public void update(UpdateServicePackageParams params) {
    this.name = params.name() != null ? params.name() : this.name;
    this.description = params.description() != null ? params.description() : this.description;
    this.price = params.price() != null ? params.price() : this.price;
    this.serviceType = params.serviceType() != null ? params.serviceType() : this.serviceType;
    this.deliverables = params.deliverables() != null ? params.deliverables() : this.deliverables;
    this.estimatedHours = params.estimatedHours() != null ? params.estimatedHours() : this.estimatedHours;
    this.complexity = params.complexity() != null ? params.complexity() : this.complexity;
    this.recurrenceInfo = params.recurrenceInfo() != null ? params.recurrenceInfo() : this.recurrenceInfo;
    this.projectDuration = params.projectDuration() != null ? params.projectDuration() : this.projectDuration;
    this.kpis = params.kpis() != null ? params.kpis() : this.kpis;
    this.socialNetworkPlatforms = params.socialNetworkPlatforms() != null
        ? params.socialNetworkPlatforms()
        : this.socialNetworkPlatforms;
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public void addKpi(String kpi) {
    if (kpi == null || kpi.isBlank()) {
      throw new ServicePackageValidationException("KPI cannot be null or empty");
    }
    if (!this.kpis.contains(kpi)) {
      this.kpis.add(kpi);
    }
  }

  public void removeKpi(String kpi) {
    this.kpis.remove(kpi);
  }

  public void addSocialNetworkPlatform(SocialNetworkPlatform platform) {
    if (platform == null) {
      throw new ServicePackageValidationException("Platform cannot be null");
    }
    if (!this.socialNetworkPlatforms.contains(platform)) {
      this.socialNetworkPlatforms.add(platform);
    }
  }

  public void removeSocialNetworkPlatform(SocialNetworkPlatform platform) {
    this.socialNetworkPlatforms.remove(platform);
  }

  public boolean isRecurring() {
    return Boolean.TRUE.equals(recurrenceInfo.isRecurring());
  }

  public List<String> getKpis() {
    return Collections.unmodifiableList(kpis);
  }

  public List<SocialNetworkPlatform> getSocialNetworkPlatforms() {
    return Collections.unmodifiableList(socialNetworkPlatforms);
  }
}
