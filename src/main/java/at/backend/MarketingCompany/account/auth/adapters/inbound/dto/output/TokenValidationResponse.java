package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.output;

public record TokenValidationResponse(
    boolean valid,
    String message,
    Object claims
) {}
