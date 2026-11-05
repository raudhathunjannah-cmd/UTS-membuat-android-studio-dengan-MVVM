package com.example.uts_raudha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uts_raudha.model.CampaignRequest;
import com.example.uts_raudha.viewmodel.MarketingViewModel;

import java.util.Calendar;
import java.util.Locale;

public class AddCampaignActivity extends AppCompatActivity {

    private EditText etKode, etNama, etDeskripsi;
    private EditText etTanggalMulai, etTanggalSelesai;
    private EditText etAnggaran;

    private Spinner etJenis, etStatus;
    private Button btnSimpan;

    private MarketingViewModel viewModel;
    private String token;

    private String tanggalMulaiApi = "";
    private String tanggalSelesaiApi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);

        etKode = findViewById(R.id.etKode);
        etNama = findViewById(R.id.etNama);
        etJenis = findViewById(R.id.etJenis);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        etTanggalMulai = findViewById(R.id.etTanggalMulai);
        etTanggalSelesai = findViewById(R.id.etTanggalSelesai);
        etAnggaran = findViewById(R.id.etAnggaran);
        etStatus = findViewById(R.id.etStatus);
        btnSimpan = findViewById(R.id.btnSimpan);

        SharedPreferences preferences =
                getSharedPreferences("UserSession", MODE_PRIVATE);

        token = preferences.getString("token", null);

        viewModel = new ViewModelProvider(this)
                .get(MarketingViewModel.class);

        // Pilihan jenis promosi
        String[] jenisPromosi = {
                "Pilih Jenis Promosi",
                "Diskon",
                "Bundling",
                "Iklan",
                "Event",
                "Lainnya"
        };

        ArrayAdapter<String> adapterJenis = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                jenisPromosi
        );

        adapterJenis.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        etJenis.setAdapter(adapterJenis);

        // Pilihan status kampanye
        String[] statusKampanye = {
                "Pilih Status Kampanye",
                "Direncanakan",
                "Aktif",
                "Selesai",
                "Dibatalkan"
        };

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statusKampanye
        );

        adapterStatus.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        etStatus.setAdapter(adapterStatus);

        // Kalender tanggal mulai
        etTanggalMulai.setOnClickListener(v -> pilihTanggal(true));

        // Kalender tanggal selesai
        etTanggalSelesai.setOnClickListener(v -> pilihTanggal(false));

        btnSimpan.setOnClickListener(v -> simpanKampanye());

        viewModel.getAddedCampaign().observe(this, campaign -> {
            if (campaign != null) {
                Toast.makeText(
                        this,
                        "Kampanye berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                btnSimpan.setEnabled(true);

                Toast.makeText(
                        this,
                        error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // Membuka kalender dan mengubah tanggal ke format Laravel
    private void pilihTanggal(boolean tanggalMulai) {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    // Format yang ditampilkan di HP: dd/MM/yyyy
                    String tanggalTampil = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );

                    // Format yang dikirim ke Laravel: yyyy-MM-dd
                    String tanggalApi = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            dayOfMonth
                    );

                    if (tanggalMulai) {
                        tanggalMulaiApi = tanggalApi;
                        etTanggalMulai.setText(tanggalTampil);
                    } else {
                        tanggalSelesaiApi = tanggalApi;
                        etTanggalSelesai.setText(tanggalTampil);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    // Menyimpan data kampanye
    private void simpanKampanye() {

        String kode = etKode.getText().toString()
                .trim()
                .toUpperCase(Locale.ROOT);

        String nama = etNama.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String anggaranText = etAnggaran.getText().toString().trim();

        if (kode.isEmpty()
                || nama.isEmpty()
                || etJenis.getSelectedItemPosition() == 0
                || tanggalMulaiApi.isEmpty()
                || anggaranText.isEmpty()
                || etStatus.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Lengkapi semua data wajib",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!tanggalSelesaiApi.isEmpty()
                && tanggalSelesaiApi.compareTo(tanggalMulaiApi) < 0) {

            Toast.makeText(
                    this,
                    "Tanggal selesai tidak boleh sebelum tanggal mulai",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (token == null) {
            Toast.makeText(
                    this,
                    "Silakan login kembali",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        try {
            double anggaran = Double.parseDouble(anggaranText);

            String jenis = etJenis.getSelectedItem().toString();

            // Samakan huruf kecil dengan nilai status di Laravel
            String status = etStatus.getSelectedItem()
                    .toString()
                    .toLowerCase(Locale.ROOT);

            CampaignRequest request = new CampaignRequest(
                    kode,
                    nama,
                    jenis,
                    deskripsi.isEmpty() ? null : deskripsi,
                    tanggalMulaiApi,
                    tanggalSelesaiApi.isEmpty()
                            ? null : tanggalSelesaiApi,
                    anggaran,
                    status
            );

            btnSimpan.setEnabled(false);

            viewModel.addCampaign(token, request);

        } catch (NumberFormatException e) {
            Toast.makeText(
                    this,
                    "Anggaran harus berupa angka",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}