package at.backend.MarketingCompany.account.user.adapters.inbound.grapqhl.dto;

public record UserStatisticsResponse(
        Long totalUsers,
        long activeUsers,
        long adminUsers,
        long totalSessions
) {}
