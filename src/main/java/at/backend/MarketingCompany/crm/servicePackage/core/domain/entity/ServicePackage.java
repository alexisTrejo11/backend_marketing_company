package at.backend.MarketingCompany.crm.servicePackage.core.domain.entity;

import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.*;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.Complexity;
import at.backend.MarketingCompany.shared.SocialNetworkPlatform;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.entity.valueobjects.ServiceType;
import at.backend.MarketingCompany.crm.servicePackage.core.domain.exceptions.ServicePackageValidationException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ServicePackage extends BaseDomainEntity<ServicePackageId> {
    private final ServicePackageId id;
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

    private ServicePackage(
            ServicePackageId id,
            String name,
            String description,
            Price price,
            ServiceType serviceType,
            String deliverables,
            EstimatedHours estimatedHours,
            Complexity complexity,
            RecurrenceInfo recurrenceInfo,
            ProjectDuration projectDuration,
            List<String> kpis,
            List<SocialNetworkPlatform> socialNetworkPlatforms,
            Boolean active
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.serviceType = serviceType;
        this.deliverables = deliverables;
        this.estimatedHours = estimatedHours;
        this.complexity = complexity;
        this.recurrenceInfo = recurrenceInfo;
        this.projectDuration = projectDuration;
        this.kpis = new ArrayList<>(kpis != null ? kpis : Collections.emptyList());
        this.socialNetworkPlatforms = new ArrayList<>(socialNetworkPlatforms != null ? socialNetworkPlatforms : Collections.emptyList());
        this.active = active != null ? active : true;
    }

    public static ServicePackage create(CreateServicePackageParams params) {
        return new ServicePackage(
                ServicePackageId.generate(),
                params.name(),
                params.description(),
                params.price(),
                params.serviceType(),
                params.deliverables(),
                params.estimatedHours(),
                params.complexity(),
                params.recurrenceInfo(),
                params.projectDuration(),
                params.kpis() != null ? new ArrayList<>(params.kpis()) : new ArrayList<>(),
                params.socialNetworkPlatforms() != null ? new ArrayList<>(params.socialNetworkPlatforms()) : new ArrayList<>(),
                true
        );
    }

    public static ServicePackage reconstruct(ServicePackageReconstructParams params) {
        return new ServicePackage(
                params.id(),
                params.name(),
                params.description(),
                params.price(),
                params.serviceType(),
                params.deliverables(),
                params.estimatedHours(),
                params.complexity(),
                params.recurrenceInfo(),
                params.projectDuration(),
                params.kpis() != null ? new ArrayList<>(params.kpis()) : new ArrayList<>(),
                params.socialNetworkPlatforms() != null ? new ArrayList<>(params.socialNetworkPlatforms()) : new ArrayList<>(),
                params.active()
        );
    }

    public void update(UpdateServicePackageParams params) {
        this.name = params.name();
        this.description = params.description();
        this.price = params.price();
        this.serviceType = params.serviceType();
        this.deliverables = params.deliverables();
        this.estimatedHours = params.estimatedHours();
        this.complexity = params.complexity();
        this.recurrenceInfo = params.recurrenceInfo();
        this.projectDuration = params.projectDuration();
        this.kpis = new ArrayList<>(params.kpis() != null ? params.kpis() : Collections.emptyList());
        this.socialNetworkPlatforms = new ArrayList<>(params.socialNetworkPlatforms() != null ? params.socialNetworkPlatforms() : Collections.emptyList());
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