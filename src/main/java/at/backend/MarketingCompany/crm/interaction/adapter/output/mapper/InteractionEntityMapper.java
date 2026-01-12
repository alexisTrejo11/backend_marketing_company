package at.backend.MarketingCompany.crm.interaction.adapter.output.mapper;

import at.backend.MarketingCompany.crm.interaction.adapter.output.entity.InteractionEntity;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.Interaction;
import at.backend.MarketingCompany.crm.interaction.core.domain.entity.valueobject.*;

import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.customer.adapter.output.persistence.entity.CustomerCompanyEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InteractionEntityMapper {

  public InteractionEntity toEntity(Interaction interaction) {
    if (interaction == null)
      return null;

    InteractionEntity entity = new InteractionEntity();

    // ID
    if (interaction.getId() != null) {
      entity.setId(interaction.getId().getValue());
    }

    // Basic fields
    entity.setType(interaction.getType());
    entity.setDateTime(interaction.getDateTime().value());
    entity.setOutcome(interaction.getOutcome().value());

    // Optional fields
    interaction.getDescription().ifPresent(description -> entity.setDescription(description.value()));
    interaction.getFeedbackType().ifPresent(entity::setFeedbackType);
    interaction.getChannelPreference().ifPresent(channel -> entity.setChannelPreference(channel.value()));

    // Audit fields
    entity.setCreatedAt(interaction.getCreatedAt());
    entity.setUpdatedAt(interaction.getUpdatedAt());
    entity.setDeletedAt(interaction.getDeletedAt());
    entity.setVersion(interaction.getVersion());

    // Relations - solo IDs
    if (interaction.getCustomerCompanyId() != null) {
      var customer = new CustomerCompanyEntity(interaction.getCustomerCompanyId().getValue());
      entity.setCustomerCompany(customer);
    }

    return entity;
  }

  public Interaction toDomain(InteractionEntity entity) {
    if (entity == null)
      return null;

    var reconstructParams = InteractionReconstructParams.builder()
        .id(new InteractionId(entity.getId()))
        .customerCompanyId(entity.getCustomerCompany() != null ? new CustomerCompanyId(entity.getCustomerId()) : null)
        .type(entity.getType())
        .dateTime(InteractionDateTime.from(entity.getDateTime()))
        .description(InteractionDescription.from(entity.getDescription()))
        .outcome(InteractionOutcome.from(entity.getOutcome()))
        .feedbackType(entity.getFeedbackType())
        .channelPreference(ChannelPreference.from(entity.getChannelPreference()))
        .version(entity.getVersion())
        .deletedAt(entity.getDeletedAt())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();

    return Interaction.reconstruct(reconstructParams);
  }

  public void updateEntity(InteractionEntity existingEntity, Interaction interaction) {
    existingEntity.setType(interaction.getType());
    existingEntity.setDateTime(interaction.getDateTime().value());
    existingEntity.setOutcome(interaction.getOutcome().value());

    interaction.getDescription().ifPresentOrElse(
        description -> existingEntity.setDescription(description.value()),
        () -> existingEntity.setDescription(null));

    interaction.getFeedbackType().ifPresentOrElse(
        existingEntity::setFeedbackType,
        () -> existingEntity.setFeedbackType(null));

    interaction.getChannelPreference().ifPresentOrElse(
        channel -> existingEntity.setChannelPreference(channel.value()),
        () -> existingEntity.setChannelPreference(null));

  }

  public List<InteractionEntity> toEntityList(List<Interaction> interactions) {
    return interactions.stream()
        .map(this::toEntity)
        .toList();
  }

  public List<Interaction> toDomainList(List<InteractionEntity> entities) {
    return entities.stream()
        .map(this::toDomain)
        .toList();
  }
}
