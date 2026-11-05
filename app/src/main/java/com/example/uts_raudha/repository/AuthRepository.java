package com.example.uts_raudha.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.uts_raudha.model.ApiMessageResponse;
import com.example.uts_raudha.model.LoginRequest;
import com.example.uts_raudha.model.LoginResponse;
import com.example.uts_raudha.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public void login(
            LoginRequest request,
            MutableLiveData<LoginResponse> result,
            MutableLiveData<String> error
    ) {

        ApiClient.getApiService()
                .login(request)
                .enqueue(new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(
                            Call<LoginResponse> call,
                            Response<LoginResponse> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            result.setValue(response.body());

                        } else {
                            error.setValue("Email atau password salah");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<LoginResponse> call,
                            Throwable t
                    ) {
                        error.setValue(
                                "Koneksi gagal: " + t.getMessage()
                        );
                    }
                });
    }

    public void logout(
            String token,
            MutableLiveData<ApiMessageResponse> result,
            MutableLiveData<String> error
    ) {

        ApiClient.getApiService()
                .logout("Bearer " + token)
                .enqueue(new Callback<ApiMessageResponse>() {

                    @Override
                    public void onResponse(
                            Call<ApiMessageResponse> call,
                            Response<ApiMessageResponse> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            result.setValue(response.body());

                        } else {
                            error.setValue("Logout gagal");
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiMessageResponse> call,
                            Throwable t
                    ) {
                        error.setValue(
                                "Koneksi gagal: " + t.getMessage()
                        );
                    }
                });
    }
}
