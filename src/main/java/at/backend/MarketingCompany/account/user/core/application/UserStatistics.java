package at.backend.MarketingCompany.account.user.core.application;

public record UserStatistics(
        long totalUsers,
        long activeUsers,
        long adminUsers,
        long totalSessions) {
}
