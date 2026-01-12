package at.backend.MarketingCompany.shared.domain;

import at.backend.MarketingCompany.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class BaseDomainEntity<T> {
  protected T id;
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;
  protected LocalDateTime deletedAt;
  protected Integer version;

  private final List<DomainEvent> domainEvents = new ArrayList<>();

  public BaseDomainEntity() {
  }

  public BaseDomainEntity(T id, Integer version, LocalDateTime deletedAt, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.version = version;
    this.deletedAt = deletedAt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public BaseDomainEntity(T id) {
    this.id = id;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.version = 1;
  }

  protected void registerEvent(DomainEvent event) {
    if (event == null) {
      throw new IllegalArgumentException("Domain event cannot be null");
    }
    this.domainEvents.add(event);
  }

  public List<DomainEvent> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void clearDomainEvents() {
    this.domainEvents.clear();
  }

  public boolean hasDomainEvents() {
    return !domainEvents.isEmpty();
  }

  public void softDelete() {
    if (this.deletedAt != null) {
      throw new IllegalStateException("Entity is already deleted");
    }

    this.deletedAt = LocalDateTime.now();
  }

  public void restore() {
    if (this.deletedAt == null) {
      throw new IllegalStateException("Entity is not deleted");
    }
    this.deletedAt = null;
  }
}
