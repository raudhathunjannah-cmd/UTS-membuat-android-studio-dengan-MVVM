package com.example.uts_raudha.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uts_raudha.model.*;
import com.example.uts_raudha.repository.MarketingRepository;

import java.util.List;

public class MarketingViewModel extends ViewModel {

    private final MarketingRepository repository =
            new MarketingRepository();

    private final MutableLiveData<List<MarketingCampaign>>
            campaigns = new MutableLiveData<>();

    private final MutableLiveData<MarketingCampaign>
            addedCampaign = new MutableLiveData<>();

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public LiveData<List<MarketingCampaign>> getCampaigns() {
        return campaigns;
    }

    public LiveData<MarketingCampaign> getAddedCampaign() {
        return addedCampaign;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadCampaigns(String token) {

        error.setValue(null);

        repository.getCampaigns(
                token,
                campaigns,
                error
        );
    }

    public void addCampaign(
            String token,
            CampaignRequest request
    ) {

        error.setValue(null);

        repository.addCampaign(
                token,
                request,
                addedCampaign,
                error
        );
    }
}