package at.backend.MarketingCompany.shared.domain.exceptions;

public class CampaignServiceException extends BaseException {

    public CampaignServiceException(String message) {
        super(message, "Campaign Service");
    }
}