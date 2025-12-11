package at.backend.MarketingCompany.shared.domain.exceptions;

import at.backend.MarketingCompany.shared.domain.marketingCampaing.ActivityStatus;

public class InvalidStatusTransitionException extends BaseException {

    public InvalidStatusTransitionException(ActivityStatus currentStatus, ActivityStatus targetStatus) {
        super("Cannot transition activity from " + currentStatus + " to " + targetStatus, "INVALID_STATUS_TRANSITION");
    }
}
