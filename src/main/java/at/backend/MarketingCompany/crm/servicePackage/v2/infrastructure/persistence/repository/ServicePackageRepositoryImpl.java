package at.backend.MarketingCompany.crm.servicePackage.v2.infrastructure.persistence.repository;

import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.ServicePackage;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.entity.valueobjects.*;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.exceptions.ServicePackagePersistenceException;
import at.backend.MarketingCompany.crm.servicePackage.v2.domain.repository.ServicePackageRepository;
import at.backend.MarketingCompany.crm.servicePackage.v2.infrastructure.persistence.model.ServicePackageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ServicePackageRepositoryImpl implements ServicePackageRepository {

    private final JpaServicePackageRepository jpaRepository;

    @Override
    public ServicePackage save(ServicePackage servicePackage) {
        log.debug("Saving service package to database with id: {}", servicePackage.getId().value());
        
        ServicePackageEntity jpaEntity = toJpaEntity(servicePackage);
        ServicePackageEntity savedEntity = jpaRepository.saveAndFlush(jpaEntity);

        log.debug("Service package persisted successfully with id: {}", savedEntity.getId());
        return toDomain(savedEntity);
    }

    @Override
    public Optional<ServicePackage> findById(ServicePackageId id) {
        log.debug("Finding service package by id: {}", id.value());
        Optional<ServicePackage> result = jpaRepository.findById(id.value())
                .map(this::toDomain);

        if (result.isPresent()) {
            log.debug("Service package found with id: {}", id.value());
        } else {
            log.debug("Service package not found with id: {}", id.value());
        }

        return result;
    }

    @Override
    public Page<ServicePackage> findAll(Pageable pageable) {
        log.debug("Finding all service packages with page: {}, size: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        Page<ServicePackage> result = jpaRepository.findAll(pageable)
                .map(this::toDomain);

        log.debug("Found {} service packages in page {} of {}",
                 result.getNumberOfElements(),
                 result.getNumber(),
                 result.getTotalPages());

        return result;
    }

    @Override
    public void delete(ServicePackage servicePackage) {
        log.debug("Deleting service package from database with id: {}", 
                 servicePackage.getId().value());
        
        jpaRepository.deleteById(servicePackage.getId().value());
        jpaRepository.flush();

        log.debug("Service package deleted successfully with id: {}",
                 servicePackage.getId().value());

    }

    @Override
    public boolean existsById(ServicePackageId id) {
        log.debug("Checking if service package exists with id: {}", id.value());
        
        boolean exists = jpaRepository.existsById(id.value());
        log.debug("Service package with id {} exists: {}", id.value(), exists);
        return exists;
    }


    private ServicePackageEntity toJpaEntity(ServicePackage domain) {
        log.debug("Mapping domain ServicePackage to JPA entity");
        
        try {
            ServicePackageEntity entity = new ServicePackageEntity();
            entity.setId(domain.getId().value());
            entity.setName(domain.getName());
            entity.setDescription(domain.getDescription());
            entity.setPrice(domain.getPrice().amount());
            entity.setServiceType(domain.getServiceType());
            entity.setDeliverables(domain.getDeliverables());
            entity.setEstimatedHours(domain.getEstimatedHours().hours());
            entity.setComplexity(domain.getComplexity());
            entity.setIsRecurring(domain.getRecurrenceInfo().isRecurring());
            entity.setFrequency(domain.getRecurrenceInfo().frequency());
            entity.setProjectDuration(domain.getProjectDuration() != null 
                ? domain.getProjectDuration().months() : null);
            entity.setKpis(domain.getKpis());
            entity.setSocialNetworkPlatforms(domain.getSocialNetworkPlatforms());
            entity.setActive(domain.getActive());
            entity.setCreatedAt(domain.getCreatedAt());
            entity.setUpdatedAt(domain.getUpdatedAt());
            
            return entity;
            
        } catch (Exception e) {
            log.error("Error mapping domain to JPA entity", e);
            throw new ServicePackagePersistenceException(
                "Failed to map domain object to JPA entity", e);
        }
    }

    private ServicePackage toDomain(ServicePackageEntity entity) {
        log.debug("Mapping JPA entity to domain ServicePackage");

        var reconstructParamsBuilder = ServicePackageReconstructParams.builder()
            .id(new ServicePackageId(entity.getId()))
            .name(entity.getName())
            .description(entity.getDescription())
            .price(new Price(entity.getPrice()))
            .serviceType(entity.getServiceType())
            .deliverables(entity.getDeliverables())
            .estimatedHours(new EstimatedHours(entity.getEstimatedHours()))
            .complexity(entity.getComplexity())
            .recurrenceInfo(new RecurrenceInfo(
                entity.getIsRecurring(),
                entity.getFrequency()))
            .projectDuration(entity.getProjectDuration() != null
                ? new ProjectDuration(entity.getProjectDuration()) : null)
            .kpis(entity.getKpis())
            .socialNetworkPlatforms(entity.getSocialNetworkPlatforms())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .version(entity.getVersion())
                .build();

            return ServicePackage.reconstruct(reconstructParamsBuilder);
    }
}