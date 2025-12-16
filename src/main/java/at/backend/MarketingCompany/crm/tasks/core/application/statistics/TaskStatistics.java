package at.backend.MarketingCompany.crm.tasks.core.application.statistics;

public record TaskStatistics(
      long totalTasks,
      long pendingTasks,
      long completedTasks,
      long overdueTasks) {
  }