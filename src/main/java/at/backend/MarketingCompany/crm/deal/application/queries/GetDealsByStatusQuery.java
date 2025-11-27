package at.backend.MarketingCompany.crm.deal.application.queries;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;

import java.util.List;

public record GetDealsByStatusQuery(List<DealStatus> statuses) {}