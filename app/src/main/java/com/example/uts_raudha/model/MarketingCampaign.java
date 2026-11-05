package com.example.uts_raudha.model;

public record MarketingCampaign (

        int id,
        String kode_kampanye,
        String nama_kampanye,
        String jenis_promosi,
        String deskripsi,
        String tanggal_mulai,
        String tanggal_selesai,
        String anggaran,
        String status
)  {}
