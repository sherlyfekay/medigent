package com.example.sherly.medigent.fitur.histori;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.histori.DetailHistoryModel;

import java.util.ArrayList;

public class DaftarHistoriMenungguAdapter extends RecyclerView.Adapter<DaftarHistoriMenungguAdapter.ItemRowHolder>{

    ArrayList<DetailHistoryModel> dataHistori;
    private Activity activity;

    public DaftarHistoriMenungguAdapter(Activity activity, ArrayList<DetailHistoryModel> dataHistori) {
        this.dataHistori = dataHistori;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_daftar_histori_menunggu, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
        final DetailHistoryModel detailHistoryModel = dataHistori.get(i);

        holder.tvTanggalHis.setText(dataHistori.get(i).getCreated_at());
        holder.tvJenisHis.setText(dataHistori.get(i).getJenis());
        holder.tvNamaPasien.setText(dataHistori.get(i).getNama_pasien());
        holder.tvDiagnosa.setText(dataHistori.get(i).getDiagnosa());
        holder.tvAlamat.setText(dataHistori.get(i).getAlamat_lengkap());

        if(dataHistori.get(i).getStatus() == 4) {
            holder.ivIcon.setImageResource(R.drawable.ic_tolak);
            holder.tvStatus.setText("Ditolak");
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.merah));
        }
        else if(dataHistori.get(i).getStatus() == 1) {
            holder.ivIcon.setImageResource(R.drawable.ic_wait);
            holder.tvStatus.setText("Menunggu");
            holder.tvStatus.setTextColor(activity.getResources().getColor(R.color.gray));
        }

        /*holder.cvDaftarHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailHistoryActivity.class);
                intent.putExtra("id_orderoffer", detailHistoryModel.get_id());
                activity.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (null != dataHistori ? dataHistori.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTanggalHis, tvJenisHis, tvNamaPasien, tvDiagnosa, tvStatus, tvAlamat;
        protected ImageView ivIcon;;
        protected CardView cvDaftarHis;

        public ItemRowHolder(View view) {
            super(view);
            this.tvTanggalHis = (TextView) view.findViewById(R.id.tvTanggalHis);
            this.tvJenisHis = (TextView)view.findViewById(R.id.tvJenisHis);
            this.tvNamaPasien = (TextView) view.findViewById(R.id.tvNamaPasien);
            this.tvDiagnosa = (TextView)view.findViewById(R.id.tvDiagnosa);
            this.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            this.tvAlamat = (TextView) view.findViewById(R.id.tvAlamat);
            this.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            this.cvDaftarHis = (CardView) view.findViewById(R.id.cvDaftarHis);
        }
    }

}

