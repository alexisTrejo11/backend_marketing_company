package at.backend.MarketingCompany.marketing.attribution.domain.HelperHandlers;

import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public class DealId {
    String value;
}


