package com.example.uts_raudha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uts_raudha.model.CampaignResponse;
import com.example.uts_raudha.network.ApiClient;
import com.example.uts_raudha.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCampaignActivity extends AppCompatActivity {

    private TextView tvKode, tvNama, tvJenis, tvDeskripsi;
    private TextView tvTanggalMulai, tvTanggalSelesai;
    private TextView tvAnggaran, tvStatus;

    private ApiService apiService;
    private String token;
    private int campaignId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_campaign);

        tvKode = findViewById(R.id.tvDetailKode);
        tvNama = findViewById(R.id.tvDetailNama);
        tvJenis = findViewById(R.id.tvDetailJenis);
        tvDeskripsi = findViewById(R.id.tvDetailDeskripsi);
        tvTanggalMulai = findViewById(R.id.tvDetailMulai);
        tvTanggalSelesai = findViewById(R.id.tvDetailSelesai);
        tvAnggaran = findViewById(R.id.tvDetailAnggaran);
        tvStatus = findViewById(R.id.tvDetailStatus);

        SharedPreferences preferences =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        token = preferences.getString("token", null);

        campaignId = getIntent().getIntExtra("campaign_id", -1);

        apiService = ApiClient.getApiService();

        if (token == null || campaignId == -1) {
            Toast.makeText(
                    this,
                    "Data kampanye tidak valid",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadDetail();
    }

    private void loadDetail() {

        apiService.getCampaignDetail(
                "Bearer " + token,
                campaignId
        ).enqueue(new Callback<CampaignResponse>() {

            @Override
            public void onResponse(
                    Call<CampaignResponse> call,
                    Response<CampaignResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()
                        && response.body().getData() != null) {

                    var campaign = response.body().getData();

                    tvKode.setText(campaign.kode_kampanye());
                    tvNama.setText(campaign.nama_kampanye());
                    tvJenis.setText(campaign.jenis_promosi());

                    tvDeskripsi.setText(
                            campaign.deskripsi() == null
                                    || campaign.deskripsi().isEmpty()
                                    ? "-"
                                    : campaign.deskripsi()
                    );

                    tvTanggalMulai.setText(
                            formatTanggal(campaign.tanggal_mulai())
                    );

                    tvTanggalSelesai.setText(
                            formatTanggal(campaign.tanggal_selesai())
                    );

                    tvAnggaran.setText("Rp " + campaign.anggaran());
                    tvStatus.setText(campaign.status());

                } else {
                    Toast.makeText(
                            DetailCampaignActivity.this,
                            "Gagal mengambil detail kampanye",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<CampaignResponse> call,
                    Throwable t
            ) {
                Toast.makeText(
                        DetailCampaignActivity.this,
                        "Koneksi gagal: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // Mengubah tanggal ISO Laravel menjadi dd/MM/yyyy
    private String formatTanggal(String tanggal) {

        if (tanggal == null || tanggal.isEmpty()) {
            return "-";
        }

        try {
            String tanggalSaja = tanggal.substring(0, 10);

            String[] bagian = tanggalSaja.split("-");

            return bagian[2] + "/" + bagian[1] + "/" + bagian[0];

        } catch (Exception e) {
            return tanggal;
        }
    }
}