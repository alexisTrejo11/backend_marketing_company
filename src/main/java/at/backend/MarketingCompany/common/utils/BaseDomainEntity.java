package at.backend.MarketingCompany.common.utils;

import java.time.LocalDateTime;

public abstract class BaseDomainEntity<T> {
    protected T id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;
    protected Integer version;

    public BaseDomainEntity() {
    }

    public BaseDomainEntity(T id, Integer version, LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
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


    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }

    public T getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public Integer getVersion() {
        return version;
    }
}
