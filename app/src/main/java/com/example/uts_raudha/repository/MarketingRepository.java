
package com.example.uts_raudha.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.uts_raudha.model.*;
import com.example.uts_raudha.network.ApiClient;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketingRepository {

    // Mengambil semua data kampanye
    public void getCampaigns(
            String token,
            MutableLiveData<List<MarketingCampaign>> result,
            MutableLiveData<String> error
    ) {
        ApiClient.getApiService()
                .getCampaigns("Bearer " + token)
                .enqueue(new Callback<CampaignListResponse>() {

                    @Override
                    public void onResponse(
                            Call<CampaignListResponse> call,
                            Response<CampaignListResponse> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {

                            result.setValue(response.body().getData());

                        } else {
                            error.setValue(getErrorMessage(response));
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<CampaignListResponse> call,
                            Throwable t
                    ) {
                        error.setValue(
                                "Koneksi gagal: " + t.getMessage()
                        );
                    }
                });
    }

    // Menambahkan kampanye baru
    public void addCampaign(
            String token,
            CampaignRequest request,
            MutableLiveData<MarketingCampaign> result,
            MutableLiveData<String> error
    ) {
        ApiClient.getApiService()
                .addCampaign("Bearer " + token, request)
                .enqueue(new Callback<CampaignResponse>() {

                    @Override
                    public void onResponse(
                            Call<CampaignResponse> call,
                            Response<CampaignResponse> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null) {

                            result.setValue(
                                    response.body().getData()
                            );

                        } else {
                            error.setValue(getErrorMessage(response));
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<CampaignResponse> call,
                            Throwable t
                    ) {
                        error.setValue(
                                "Koneksi gagal: " + t.getMessage()
                        );
                    }
                });
    }

    // Mengambil pesan error dari Laravel
    private String getErrorMessage(Response<?> response) {

        try {
            if (response.errorBody() != null) {

                String json = response.errorBody().string();
                JSONObject object = new JSONObject(json);

                if (object.has("message")) {
                    String message = object.getString("message");

                    if (object.has("errors")) {
                        JSONObject errors = object.getJSONObject("errors");

                        StringBuilder detail = new StringBuilder();

                        java.util.Iterator<String> keys =
                                errors.keys();

                        while (keys.hasNext()) {
                            String key = keys.next();

                            org.json.JSONArray messages =
                                    errors.getJSONArray(key);

                            for (int i = 0; i < messages.length(); i++) {
                                detail.append("\n")
                                        .append(messages.getString(i));
                            }
                        }

                        if (detail.length() > 0) {
                            return message + detail;
                        }
                    }

                    return message;
                }

                return json;
            }

        } catch (Exception e) {
            return "Gagal membaca pesan error: " + e.getMessage();
        }

        return "HTTP " + response.code()
                + ": " + response.message();
    }
}
