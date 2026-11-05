package com.example.uts_raudha.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uts_raudha.model.*;
import com.example.uts_raudha.repository.AuthRepository;

public class LoginViewModel extends ViewModel {

    private final AuthRepository repository =
            new AuthRepository();

    private final MutableLiveData<LoginResponse> loginResult =
            new MutableLiveData<>();

    private final MutableLiveData<ApiMessageResponse> logoutResult =
            new MutableLiveData<>();

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    public LiveData<LoginResponse> getLoginResult() {
        return loginResult;
    }

    public LiveData<ApiMessageResponse> getLogoutResult() {
        return logoutResult;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void login(String email, String password) {

        error.setValue(null);

        LoginRequest request =
                new LoginRequest(email, password);

        repository.login(request, loginResult, error);
    }

    public void logout(String token) {

        error.setValue(null);

        repository.logout(token, logoutResult, error);
    }
}
