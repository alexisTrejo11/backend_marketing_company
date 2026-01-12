package at.backend.MarketingCompany.shared.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private String eventType;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
    }
    
    protected DomainEvent(String eventType) {
        this();
        this.eventType = eventType;
    }
    
    public UUID getEventId() {
        return eventId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public String getEventType() {
        return eventType;
    }
}