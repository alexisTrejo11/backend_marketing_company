package at.backend.MarketingCompany.crm.deal.v2.application.dto.query;

import at.backend.MarketingCompany.crm.Utils.enums.DealStatus;

import java.util.List;

public record GetDealsByStatusQuery(List<DealStatus> statuses) {}