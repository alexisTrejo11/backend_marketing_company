package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.crm.shared.enums.DealStatus;

import java.util.List;

public record GetDealsByStatusQuery(List<DealStatus> statuses) {}