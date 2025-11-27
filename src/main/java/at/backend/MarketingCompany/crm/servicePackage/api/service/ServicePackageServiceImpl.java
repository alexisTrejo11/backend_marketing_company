package at.backend.MarketingCompany.crm.servicePackage.api.service;

import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackageEntity;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.DTOs.ServicePackageInput;
import at.backend.MarketingCompany.crm.servicePackage.infrastructure.autoMappers.ServicePackageMappers;
import at.backend.MarketingCompany.crm.servicePackage.api.repostiory.ServicePackageRepository;
import at.backend.MarketingCompany.common.exceptions.BusinessLogicException;
import at.backend.MarketingCompany.common.service.CommonService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ServicePackageServiceImpl implements CommonService<ServicePackageEntity, ServicePackageInput, Long> {

    public final ServicePackageRepository servicePackageRepository;
    public final ServicePackageMappers servicePackageMappers;

    @Override
    public Page<ServicePackageEntity> getAll(Pageable pageable) {
        return servicePackageRepository.findAll(pageable);
    }

    @Override
    public ServicePackageEntity getById(Long id) {
        return getPackage(id);
    }

    @Override
    public ServicePackageEntity create(ServicePackageInput input) {
        ServicePackageEntity newServicePackageEntity = servicePackageMappers.inputToEntity(input);

        servicePackageRepository.saveAndFlush(newServicePackageEntity);

        return newServicePackageEntity;
    }

    @Override
    public ServicePackageEntity update(Long id, ServicePackageInput input) {
        ServicePackageEntity existingServicePackageEntity = getPackage(id);

        ServicePackageEntity updatedServicePackageEntity = servicePackageMappers.inputToUpdatedEntity(existingServicePackageEntity, input);

        servicePackageRepository.saveAndFlush(updatedServicePackageEntity);

        return updatedServicePackageEntity;
    }

    @Override
    public void delete(Long id) {
        ServicePackageEntity servicePackageEntity = getPackage(id);

        servicePackageRepository.delete(servicePackageEntity);
    }

    @Override
    public void validate(ServicePackageInput input) {
        if (input.estimatedHours() != null && input.estimatedHours() > 1000) {
            throw new BusinessLogicException("Estimated hours must not exceed 1000.");
        }

        if (Boolean.TRUE.equals(input.isRecurring()) && input.frequency() == null) {
            throw new BusinessLogicException("Frequency must be provided for recurring services.");
        }

        if (Boolean.FALSE.equals(input.isRecurring()) && input.frequency() != null) {
            throw new BusinessLogicException("Frequency should not be set for non-recurring services.");
        }

        if (input.projectDuration() != null && input.projectDuration() > 36) {
            throw new BusinessLogicException("Project duration must not exceed 36 months.");
        }

        if (input.price().compareTo(new BigDecimal("1000000")) > 0) {
            throw new BusinessLogicException("Price must not exceed 1,000,000.");
        }

    }

    private ServicePackageEntity getPackage(Long id) {
        return servicePackageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("service package not found"));
    }
}
