package at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject;

public enum Role {
    USER("Basic user permissions"),
    ADMIN("Full system access"),
    MANAGER("Team management permissions"),
    SUPPORT("Customer support permissions");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean canManageUsers() {
        return this == ADMIN || this == MANAGER;
    }
}