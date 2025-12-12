package at.backend.MarketingCompany.account.auth.adapters.inbound.dto.input;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordInput(
    @NotBlank String currentPassword,
    @NotBlank String newPassword
) {}