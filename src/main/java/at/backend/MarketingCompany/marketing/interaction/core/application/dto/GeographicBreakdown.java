package at.backend.MarketingCompany.marketing.interaction.core.application.dto;

import java.util.Map;

public record GeographicBreakdown(
      Map<String, Long> topCountries,
      Map<String, Long> topCities
  ) {}
