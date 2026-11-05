package com.example.uts_raudha.network;

import com.example.uts_raudha.model.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/login")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );

    @POST("api/logout")
    Call<ApiMessageResponse> logout(
            @Header("Authorization") String token
    );

    @GET("api/marketing-campaigns")
    Call<CampaignListResponse> getCampaigns(
            @Header("Authorization") String token
    );

    @POST("api/marketing-campaigns")
    Call<CampaignResponse> addCampaign(
            @Header("Authorization") String token,
            @Body CampaignRequest request
    );

    @GET("api/marketing-campaigns/{id}")
    Call<CampaignResponse> getCampaignDetail(
            @Header("Authorization") String token,
            @Path("id") int id
    );


}