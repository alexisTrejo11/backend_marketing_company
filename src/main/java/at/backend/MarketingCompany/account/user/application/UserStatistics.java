package at.backend.MarketingCompany.account.user.application;

public record UserStatistics(
        long totalUsers,
        long activeUsers,
        long adminUsers,
        long totalSessions) {
}
