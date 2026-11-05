
package com.example.uts_raudha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uts_raudha.model.MarketingCampaign;
import com.example.uts_raudha.viewmodel.LoginViewModel;
import com.example.uts_raudha.viewmodel.MarketingViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnLogout;
    private FloatingActionButton fabAdd;

    private MarketingViewModel marketingViewModel;
    private LoginViewModel loginViewModel;

    private CampaignAdapter adapter;
    private ArrayList<MarketingCampaign> campaignList;

    private SharedPreferences preferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnLogout = findViewById(R.id.btnLogout);
        fabAdd = findViewById(R.id.fabAdd);

        preferences = getSharedPreferences(
                "UserSession",
                MODE_PRIVATE
        );

        token = preferences.getString("token", null);

        if (token == null) {
            openLogin();
            return;
        }

        campaignList = new ArrayList<>();

        adapter = new CampaignAdapter(this, campaignList);
        listView.setAdapter(adapter);

        marketingViewModel = new ViewModelProvider(this)
                .get(MarketingViewModel.class);

        loginViewModel = new ViewModelProvider(this)
                .get(LoginViewModel.class);

        observeCampaigns();
        observeLogout();

        marketingViewModel.loadCampaigns(token);

        // Klik salah satu kampanye
        listView.setOnItemClickListener((parent, view, position, id) -> {

            MarketingCampaign campaign = campaignList.get(position);

            Intent intent = new Intent(
                    MainActivity.this,
                    DetailCampaignActivity.class
            );

            intent.putExtra("campaign_id", campaign.id());

            startActivity(intent);
        });

        // Tombol tambah kampanye
        fabAdd.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddCampaignActivity.class
            );

            startActivity(intent);
        });

        // Tombol logout
        btnLogout.setOnClickListener(v -> {

            btnLogout.setEnabled(false);
            loginViewModel.logout(token);
        });
    }

    private void observeCampaigns() {

        marketingViewModel.getCampaigns()
                .observe(this, campaigns -> {

                    if (campaigns == null) return;

                    campaignList.clear();
                    campaignList.addAll(campaigns);

                    adapter.notifyDataSetChanged();

                    if (campaignList.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Belum ada kampanye",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

        marketingViewModel.getError()
                .observe(this, error -> {

                    if (error != null) {
                        Toast.makeText(
                                this,
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    private void observeLogout() {

        loginViewModel.getLogoutResult()
                .observe(this, response -> {

                    if (response != null && response.isSuccess()) {

                        preferences.edit().clear().apply();

                        Toast.makeText(
                                this,
                                "Logout berhasil",
                                Toast.LENGTH_SHORT
                        ).show();

                        openLogin();
                    }
                });

        loginViewModel.getError()
                .observe(this, error -> {

                    if (error != null) {

                        btnLogout.setEnabled(true);

                        Toast.makeText(
                                this,
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (marketingViewModel != null && token != null) {
            marketingViewModel.loadCampaigns(token);
        }
    }

    private void openLogin() {

        startActivity(new Intent(
                MainActivity.this,
                LoginActivity.class
        ));

        finish();
    }
}
