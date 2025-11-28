package at.backend.MarketingCompany.crm.tasks.api.service;

import at.backend.MarketingCompany.common.exceptions.BusinessLogicException;
import at.backend.MarketingCompany.common.service.CommonService;
import at.backend.MarketingCompany.crm.tasks.api.repository.TaskRepository;
import at.backend.MarketingCompany.crm.tasks.infrastructure.DTOs.TaskInput;
import at.backend.MarketingCompany.crm.tasks.infrastructure.autoMappers.TaskMappers;
import at.backend.MarketingCompany.crm.opportunity.domain.Opportunity;
import at.backend.MarketingCompany.crm.tasks.infrastructure.persistence.TaskEntity;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.user.api.Model.User;
import at.backend.MarketingCompany.crm.opportunity.api.repository.OpportunityRepository;
import at.backend.MarketingCompany.user.api.Repository.UserRepository;
import at.backend.MarketingCompany.customer.api.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements CommonService<TaskEntity, TaskInput, Long> {

    public final TaskRepository TaskRepository;
    public final TaskMappers TaskMappers;
    public final CustomerRepository customerRepository;
    public final OpportunityRepository opportunityRepository;
    public final UserRepository userRepository;

    @Override
    public Page<TaskEntity> getAll(Pageable pageable) {
        return TaskRepository.findAll(pageable);
    }

    @Override
    public TaskEntity getById(Long id) {
        return getTask(id);
    }

    @Override
    public TaskEntity create(TaskInput input) {
        TaskEntity newTaskEntity = TaskMappers.inputToEntity(input);

        newTaskEntity.setCustomerModel(getCustomer(input.customerId()));
        newTaskEntity.setOpportunity(getOpportunity(input.opportunityId()));

        if (input.assignedToUserId() != null) {
            newTaskEntity.setAssignedTo(getUser(input.assignedToUserId()));
        }

        TaskRepository.saveAndFlush(newTaskEntity);

        return newTaskEntity;
    }

    @Override
    public TaskEntity update(Long id, TaskInput input) {
        TaskEntity existingTaskEntity = getTask(id);

        TaskEntity updatedTaskEntity = TaskMappers.inputToUpdatedEntity(existingTaskEntity, input);

        updatedTaskEntity.setCustomerModel(getCustomer(input.customerId()));
        updatedTaskEntity.setOpportunity(getOpportunity(input.opportunityId()));

        if (input.assignedToUserId() != null) {
            updatedTaskEntity.setAssignedTo(getUser(input.assignedToUserId()));
        }


        TaskRepository.saveAndFlush(updatedTaskEntity);

        return updatedTaskEntity;
    }

    @Override
    public void delete(Long id) {
        TaskEntity taskEntity = getTask(id);

        TaskRepository.delete(taskEntity);
    }

    @Override
    public void validate(TaskInput input) {
        Optional<CustomerModel> customer = customerRepository.findById(input.customerId());
        if (customer.isEmpty()) {
            throw new EntityNotFoundException("CustomerModel Not Found");
        }

        if (input.dueDate() != null) {
            if (input.dueDate().isBefore(LocalDateTime.now())) {
                throw new BusinessLogicException("Expected due date cannot be in the past");
            }

            LocalDateTime maxCloseDate = LocalDateTime.now().plusYears(1); // Max Date allowed (1 year)
            if (input.dueDate().isAfter(maxCloseDate)) {
                throw new BusinessLogicException("Expected due date cannot be more than one year in the future");
            }
        }
    }

    private CustomerModel getCustomer(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("CustomerModel Not found"));
    }

    private Opportunity getOpportunity(Long opportunityId) {
        return opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Opportunity Not found"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not found"));
    }

    private TaskEntity getTask(Long id) {
        return TaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskEntity Not found"));
    }
}
