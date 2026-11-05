package com.example.uts_raudha.model;

public class CampaignRequest {

    private String kode_kampanye;
    private String nama_kampanye;
    private String jenis_promosi;
    private String deskripsi;
    private String tanggal_mulai;
    private String tanggal_selesai;
    private double anggaran;
    private String status;

    public CampaignRequest(
            String kode_kampanye,
            String nama_kampanye,
            String jenis_promosi,
            String deskripsi,
            String tanggal_mulai,
            String tanggal_selesai,
            double anggaran,
            String status
    ) {
        this.kode_kampanye = kode_kampanye;
        this.nama_kampanye = nama_kampanye;
        this.jenis_promosi = jenis_promosi;
        this.deskripsi = deskripsi;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_selesai = tanggal_selesai;
        this.anggaran = anggaran;
        this.status = status;
    }
}