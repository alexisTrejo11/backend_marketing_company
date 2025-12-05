package at.backend.MarketingCompany.account.auth.adapaters.inbound.dto.output;

public record TokenValidationResponse(
    boolean valid,
    String message,
    Object claims
) {}
