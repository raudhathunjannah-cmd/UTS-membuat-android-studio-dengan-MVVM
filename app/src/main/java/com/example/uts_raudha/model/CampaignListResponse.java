package com.example.uts_raudha.model;

import java.util.List;
public class CampaignListResponse {
    private boolean success;
    private String message;
    private List<MarketingCampaign> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<MarketingCampaign> getData() {
        return data;
    }
}
