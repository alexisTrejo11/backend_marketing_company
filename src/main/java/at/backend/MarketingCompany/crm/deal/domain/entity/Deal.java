package at.backend.MarketingCompany.crm.deal.domain.entity;

import at.backend.MarketingCompany.common.utils.BaseDomainEntity;
import at.backend.MarketingCompany.crm.shared.enums.DealStatus;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.*;
import at.backend.MarketingCompany.crm.deal.domain.entity.valueobject.external.EmployeeId;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealStatusTransitionException;
import at.backend.MarketingCompany.crm.deal.domain.exceptions.DealValidationException;
import at.backend.MarketingCompany.crm.servicePackage.domain.entity.valueobjects.ServicePackageId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Deal extends BaseDomainEntity<DealId> {
    private CustomerId customerId;
    private OpportunityId opportunityId;
    private DealStatus dealStatus;
    private FinalAmount finalAmount;
    private ContractPeriod period;
    private EmployeeId campaignManagerId;
    private String deliverables;
    private String terms;
    private List<ServicePackageId> servicePackageIds;

    private Deal(DealId dealId) {
        super(dealId);
    }

    private Deal(DealReconstructParams params) {
        super(params.id(), params.version(), params.deletedAt(), params.createdAt(), params.updatedAt());
        this.customerId = params.customerId();
        this.opportunityId = params.opportunityId();
        this.dealStatus = params.dealStatus();
        this.finalAmount = params.finalAmount();
        this.period = params.period();
        this.campaignManagerId = params.campaignManagerId();
        this.deliverables = params.deliverables();
        this.terms = params.terms();
        this.servicePackageIds = Collections.unmodifiableList(params.servicePackageIds());
        validateState();
    }

    public static Deal reconstruct(DealReconstructParams params) {
        return new Deal(params);
    }

    public static Deal create(CreateDealParams params) {
        validateCreationParams(params);

        Deal newDeal = new Deal(DealId.create());
        newDeal.customerId = params.customerId();
        newDeal.opportunityId = params.opportunityId();
        newDeal.servicePackageIds = Collections.unmodifiableList(params.servicePackageIds());
        newDeal.dealStatus = DealStatus.DRAFT;
        newDeal.period = new ContractPeriod(params.startDate(), Optional.empty());
        newDeal.finalAmount = FinalAmount.zero();

        return newDeal;
    }

    public void startNegotiation() {
        validateTransition(DealStatus.IN_NEGOTIATION);
        this.dealStatus = DealStatus.IN_NEGOTIATION;
        updateTimestamp();
    }

    public void signDeal(FinalAmount amount, String terms, EmployeeId managerId) {
        validateTransition(DealStatus.SIGNED);

        if (amount == null || !amount.isPositive()) {
            throw new DealValidationException("Final amount must be positive to sign a deal.");
        }
        if (terms == null || terms.trim().isEmpty()) {
            throw new DealValidationException("Terms must be provided for a SIGNED deal.");
        }
        if (managerId == null) {
            throw new DealValidationException("Campaign manager must be assigned to sign a deal.");
        }

        this.dealStatus = DealStatus.SIGNED;
        this.finalAmount = amount;
        this.terms = terms.trim();
        this.campaignManagerId = managerId;
        updateTimestamp();
    }


    public void markAsPaid() {
        validateTransition(DealStatus.PAID);
        this.dealStatus = DealStatus.PAID;
        updateTimestamp();
    }


    public void startExecution() {
        validateTransition(DealStatus.IN_PROGRESS);
        this.dealStatus = DealStatus.IN_PROGRESS;
        updateTimestamp();
    }


    public void completeDeal(LocalDate endDate, String finalDeliverables) {
        validateTransition(DealStatus.COMPLETED);

        if (endDate == null) {
            throw new DealValidationException("End date is required to complete a deal.");
        }
        if (finalDeliverables == null || finalDeliverables.trim().isEmpty()) {
            throw new DealValidationException("Deliverables must be provided for a COMPLETED deal.");
        }
        if (endDate.isBefore(this.period.startDate())) {
            throw new DealValidationException("End date cannot be before start date.");
        }

        this.period = new ContractPeriod(this.period.startDate(), Optional.of(endDate));
        this.deliverables = finalDeliverables.trim();
        this.dealStatus = DealStatus.COMPLETED;
        updateTimestamp();
    }

    public void cancelDeal() {
        validateTransition(DealStatus.CANCELLED);
        this.dealStatus = DealStatus.CANCELLED;
        updateTimestamp();
    }

    public void updateServicePackages(List<ServicePackageId> newServicePackages) {
        if (this.dealStatus == DealStatus.COMPLETED || this.dealStatus == DealStatus.CANCELLED) {
            throw new DealValidationException("Cannot update service packages for a " + this.dealStatus + " deal.");
        }
        if (newServicePackages == null || newServicePackages.isEmpty()) {
            throw new DealValidationException("Service packages list cannot be empty.");
        }

        this.servicePackageIds = Collections.unmodifiableList(newServicePackages);
        updateTimestamp();
    }

    private void validateTransition(DealStatus newStatus) {
        if (!this.dealStatus.canTransitionTo(newStatus)) {
            throw new DealStatusTransitionException(
                    String.format("Cannot transition from %s to %s", this.dealStatus, newStatus)
            );
        }
    }

    private void validateState() {
        if (customerId == null) {
            throw new DealValidationException("Customer ID is required.");
        }
        if (opportunityId == null) {
            throw new DealValidationException("Opportunity ID is required.");
        }
        if (servicePackageIds == null || servicePackageIds.isEmpty()) {
            throw new DealValidationException("At least one service package is required.");
        }
        if (period == null) {
            throw new DealValidationException("Contract period is required.");
        }
    }

    private static void validateCreationParams(CreateDealParams params) {
        if (params == null) {
            throw new DealValidationException("Creation parameters cannot be null.");
        }
        if (params.customerId() == null) {
            throw new DealValidationException("Customer ID is required.");
        }
        if (params.opportunityId() == null) {
            throw new DealValidationException("Opportunity ID is required.");
        }
        if (params.servicePackageIds() == null || params.servicePackageIds().isEmpty()) {
            throw new DealValidationException("At least one service package is required.");
        }
        if (params.startDate() == null) {
            throw new DealValidationException("Start date is required.");
        }
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public DealId getId() {
        return id;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public OpportunityId getOpportunityId() {
        return opportunityId;
    }

    public DealStatus getDealStatus() {
        return dealStatus;
    }

    public Optional<FinalAmount> getFinalAmount() {
        return Optional.ofNullable(finalAmount);
    }

    public ContractPeriod getPeriod() {
        return period;
    }

    public Optional<EmployeeId> getCampaignManagerId() {
        return Optional.ofNullable(campaignManagerId);
    }

    public Optional<String> getDeliverables() {
        return Optional.ofNullable(deliverables);
    }

    public Optional<String> getTerms() {
        return Optional.ofNullable(terms);
    }

    public List<ServicePackageId> getServicePackageIds() {
        return servicePackageIds;
    }

    public boolean isSigned() {
        return dealStatus == DealStatus.SIGNED;
    }

    public boolean isCompleted() {
        return dealStatus == DealStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return dealStatus == DealStatus.CANCELLED;
    }

    public boolean canBeModified() {
        return dealStatus == DealStatus.DRAFT || dealStatus == DealStatus.IN_NEGOTIATION;
    }

    @Override
    public void markAsDeleted() {
        if (!isCancelled() && !isCompleted()) {
            throw new DealValidationException("Only cancelled or completed deals can be deleted.");
        }
        super.markAsDeleted();
    }
}