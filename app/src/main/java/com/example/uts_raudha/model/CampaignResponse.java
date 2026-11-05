
package com.example.uts_raudha.model;

public class CampaignResponse {

    private boolean success;
    private String message;
    private MarketingCampaign data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public MarketingCampaign getData() {
        return data;
    }
}
