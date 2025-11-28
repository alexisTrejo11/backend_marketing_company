package at.backend.MarketingCompany.crm.interaction.domain.exceptions;

public class InteractionNotFoundException extends RuntimeException {
    public InteractionNotFoundException(String interactionId) {
        super("Interaction not found with ID: " + interactionId);
    }
}