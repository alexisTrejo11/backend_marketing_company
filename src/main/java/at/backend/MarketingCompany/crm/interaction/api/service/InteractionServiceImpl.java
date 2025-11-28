package at.backend.MarketingCompany.crm.interaction.api.service;

import at.backend.MarketingCompany.crm.interaction.infrastructure.DTOs.InteractionInput;
import at.backend.MarketingCompany.crm.interaction.infrastructure.autoMappers.InteractionMappers;
import at.backend.MarketingCompany.customer.api.repository.CustomerModel;
import at.backend.MarketingCompany.crm.interaction.infrastructure.persistence.Interaction;
import at.backend.MarketingCompany.customer.api.repository.CustomerRepository;
import at.backend.MarketingCompany.crm.interaction.api.repository.InteractionRepository;
import at.backend.MarketingCompany.crm.shared.enums.FeedbackType;
import at.backend.MarketingCompany.crm.shared.enums.InteractionType;
import at.backend.MarketingCompany.common.service.CommonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InteractionServiceImpl implements CommonService<Interaction, InteractionInput, Long> {

    public final InteractionRepository interactionRepository;
    public final CustomerRepository customerRepository;
    public final InteractionMappers interactionMappers;

    @Override
    public Page<Interaction> getAll(Pageable pageable) {
        return interactionRepository.findAll(pageable);
    }

    @Override
    public Interaction getById(Long id) {
        return getInteraction(id);
    }

    @Transactional
    @Override
    public Interaction create(InteractionInput input) {
        Interaction newInteraction = interactionMappers.inputToEntity(input);

        newInteraction.setCustomerModel(getCustomer(input.customerId()));

        interactionRepository.saveAndFlush(newInteraction);

        return newInteraction;
    }

    @Transactional
    @Override
    public Interaction update(Long id, InteractionInput input) {
        Interaction existingInteraction = getInteraction(id);

        Interaction updatedInteraction = interactionMappers.inputToUpdatedEntity(existingInteraction, input);

        updatedInteraction.setCustomerModel(getCustomer(input.customerId()));

        interactionRepository.saveAndFlush(updatedInteraction);

        return updatedInteraction;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Interaction existingInteraction = getInteraction(id);

        interactionRepository.delete(existingInteraction);
    }

    @Override
    public void validate(InteractionInput input) {
        if (input.dateTime().isAfter(LocalDateTime.now().plusYears(1))) {
            throw new IllegalArgumentException("The interaction date and time cannot be more than 1 year in the future");
        }

        if (FeedbackType.STRATEGY.equals(input.feedbackType()) && !"Follow-up meeting scheduled".equalsIgnoreCase(input.outcome())) {
            throw new IllegalArgumentException("For feedback type 'STRATEGY', the outcome must include a follow-up action");
        }

        if (InteractionType.MEETING.equals(input.type()) && input.feedbackType() == null) {
            throw new IllegalArgumentException("Feedback type must be specified for 'MEETING' interactions");
        }

        if (input.channelPreference() != null) {
            if (InteractionType.CALL.equals(input.type()) && !"Phone".equalsIgnoreCase(input.channelPreference())) {
                throw new IllegalArgumentException("For 'CALL' interactions, channel preference must be 'Phone'");
            }
            if (InteractionType.EMAIL.equals(input.type()) && !"Email".equalsIgnoreCase(input.channelPreference())) {
                throw new IllegalArgumentException("For 'EMAIL' interactions, channel preference must be 'Email'");
            }
        }
    }

    private CustomerModel getCustomer(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() ->  new EntityNotFoundException("CustomerModel not found"));
    }

    private Interaction getInteraction(Long id) {
        return interactionRepository.findById(id).orElseThrow(() ->  new EntityNotFoundException("Interaction not found"));
    }
}
