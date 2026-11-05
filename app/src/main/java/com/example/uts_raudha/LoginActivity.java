package com.example.uts_raudha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uts_raudha.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        viewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Email dan password wajib diisi",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            btnLogin.setEnabled(false);

            viewModel.login(email, password);
        });

        viewModel.getLoginResult().observe(this, response -> {

            if (response != null
                    && response.isSuccess()
                    && response.getData() != null) {

                String token = response.getData().getToken();

                SharedPreferences preferences =
                        getSharedPreferences(
                                "UserSession",
                                MODE_PRIVATE
                        );

                preferences.edit()
                        .putString("token", token)
                        .apply();

                Toast.makeText(
                        this,
                        "Login berhasil",
                        Toast.LENGTH_SHORT
                ).show();

                startActivity(
                        new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        )
                );

                finish();
            }

            btnLogin.setEnabled(true);
        });

        viewModel.getError().observe(this, error -> {

            if (error != null) {

                btnLogin.setEnabled(true);

                Toast.makeText(
                        this,
                        error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}