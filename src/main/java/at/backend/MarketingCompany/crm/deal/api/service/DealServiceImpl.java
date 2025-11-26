package at.backend.MarketingCompany.crm.deal.api.service;

import at.backend.MarketingCompany.common.exceptions.BusinessLogicException;
import at.backend.MarketingCompany.common.service.CommonService;
import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.v2.infrastructure.persistence.JpaDealRepository;
import at.backend.MarketingCompany.crm.deal.v2.infrastructure.persistence.DealEntity;
import at.backend.MarketingCompany.crm.deal.infrastructure.DTOs.DealInput;
import at.backend.MarketingCompany.crm.deal.infrastructure.autoMappers.DealMappers;
import at.backend.MarketingCompany.crm.opportunity.api.repository.OpportunityRepository;
import at.backend.MarketingCompany.crm.opportunity.domain.Opportunity;
import at.backend.MarketingCompany.crm.servicePackage.api.repostiory.ServicePackageRepository;
import at.backend.MarketingCompany.crm.servicePackage.domain.ServicePackage;
import at.backend.MarketingCompany.user.api.Model.User;
import at.backend.MarketingCompany.user.api.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements CommonService<DealEntity, DealInput, UUID> {

    public final JpaDealRepository customerRepository;
    public final DealMappers customerMappers;
    public final ServicePackageRepository servicePackageRepository;
    public final UserRepository userRepository;
    public final OpportunityRepository opportunityRepository;

    @Override
    public Page<DealEntity> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public DealEntity getById(UUID id) {
        return getDeal(id);
    }

    @Override
    public DealEntity create(DealInput input) {
        DealEntity newDealEntity = customerMappers.inputToEntity(input);

        fetchRelationships(newDealEntity, input);
        customerRepository.saveAndFlush(newDealEntity);

        return newDealEntity;
    }

    @Override
    public DealEntity update(UUID id, DealInput input) {
        DealEntity existingDealEntity = getDeal(id);

        DealEntity updatedDealEntity = customerMappers.inputToUpdatedEntity(existingDealEntity, input);
        fetchRelationships(existingDealEntity, input);

        customerRepository.saveAndFlush(updatedDealEntity);

        return updatedDealEntity;
    }

    @Override
    public void delete(UUID id) {
        DealEntity dealEntity = getDeal(id);

        customerRepository.delete(dealEntity);
    }

    @Override
    public void validate(DealInput input) {
        if (input.endDate() != null && input.endDate().isBefore(input.startDate())) {
            throw new BusinessLogicException("End date must be after the start date.");
        }

        boolean isStatusSignedOrPaid = isStatusSignedOrPaid(input);
        if (isStatusSignedOrPaid && (input.finalAmount() == null || input.finalAmount().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessLogicException("Final amount must be greater than zero for SIGNED or PAID deals.");
        }

        if (input.dealStatus() == DealStatus.COMPLETED && (input.deliverables() == null || input.deliverables().isBlank())) {
            throw new BusinessLogicException("Deliverables must be provided for COMPLETED deals.");
        }

        if (isStatusSignedOrPaid && (input.terms() == null || input.terms().isBlank())) {
            throw new BusinessLogicException("Terms must be provided for SIGNED or PAID deals.");
        }

        if (input.dealStatus() == DealStatus.COMPLETED) {
            if (input.startDate() == null || input.endDate() == null) {
                throw new BusinessLogicException("Both start date and end date must be defined for COMPLETED deals.");
            }

            if (input.endDate().isBefore(input.startDate())) {
                throw new BusinessLogicException("End date must be after the start date for COMPLETED deals.");
            }
        }

        if ((input.dealStatus() == DealStatus.PAID || input.dealStatus() == DealStatus.COMPLETED)
                && input.finalAmount() != null && input.finalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Final amount cannot be zero for PAID or COMPLETED deals.");
        }
    }

    private boolean isStatusSignedOrPaid(DealInput input) {
        return input.dealStatus() == DealStatus.SIGNED || input.dealStatus() == DealStatus.PAID;
    }

    private void fetchRelationships(DealEntity dealEntity, DealInput input) {
        if (input.campaignManagerId() != null) {
            User user = userRepository.findById(input.campaignManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            dealEntity.setCampaignManager(user);
        }

        Opportunity opportunity = opportunityRepository.findById(input.opportunityId())
                .orElseThrow(() -> new EntityNotFoundException("opportunity not found"));
        dealEntity.setOpportunity(opportunity);
        dealEntity.setCustomerModel(opportunity.getCustomerModel());

        List<ServicePackage> servicePackages = servicePackageRepository.findAllById(input.servicePackageIds());

        dealEntity.setServices(servicePackages);
    }

    private DealEntity getDeal(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dealEntity not found"));
    }
}