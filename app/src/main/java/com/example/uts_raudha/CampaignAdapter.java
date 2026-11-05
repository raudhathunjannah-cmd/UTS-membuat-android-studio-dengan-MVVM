package com.example.uts_raudha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uts_raudha.model.MarketingCampaign;

import java.util.List;

public class CampaignAdapter extends BaseAdapter {

    private Context context;
    private List<MarketingCampaign> campaigns;

    public CampaignAdapter(
            Context context,
            List<MarketingCampaign> campaigns
    ) {
        this.context = context;
        this.campaigns = campaigns;
    }

    @Override
    public int getCount() {
        return campaigns.size();
    }

    @Override
    public Object getItem(int position) {
        return campaigns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return campaigns.get(position).id();
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent
    ) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(
                            R.layout.item_campaign,
                            parent,
                            false
                    );
        }

        TextView tvNama = convertView.findViewById(R.id.tvNama);
        TextView tvKode = convertView.findViewById(R.id.tvKode);
        TextView tvJenis = convertView.findViewById(R.id.tvJenis);
        TextView tvAnggaran = convertView.findViewById(R.id.tvAnggaran);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        MarketingCampaign campaign = campaigns.get(position);

        tvNama.setText(campaign.nama_kampanye());
        tvKode.setText("Kode: " + campaign.kode_kampanye());
        tvJenis.setText("Jenis: " + campaign.jenis_promosi());
        tvAnggaran.setText("Anggaran: Rp " + campaign.anggaran());
        tvStatus.setText("Status: " + campaign.status());

        return convertView;
    }
}