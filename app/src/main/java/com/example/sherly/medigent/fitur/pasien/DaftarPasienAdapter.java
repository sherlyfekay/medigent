package com.example.sherly.medigent.fitur.pasien;

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
import com.example.sherly.medigent.model.pasien.PostPatientModel;

import java.util.ArrayList;

public class DaftarPasienAdapter extends RecyclerView.Adapter<DaftarPasienAdapter.ItemRowHolder>{

    ArrayList<PostPatientModel> dataPasien;
    private Activity activity;

    public DaftarPasienAdapter(Activity activity, ArrayList<PostPatientModel> dataPasien) {
        this.dataPasien = dataPasien;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_daftar_pasien, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
        final PostPatientModel patientModel = dataPasien.get(i);

        holder.tvNamaPasien.setText(patientModel.getNama_lengkap());
        holder.tvHubungan.setText(" ("+patientModel.getHubungan()+")");
        holder.tvDiagnosa.setText("Diagnosa : "+patientModel.getDiagnosa());
        holder.tvJk.setText(patientModel.getJk());

        holder.cvDaftarPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(activity, DetailHistoryActivity.class);
//                intent.putExtra("id_orderoffer", detailHistoryModel.get_id());
//                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataPasien ? dataPasien.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvNamaPasien, tvHubungan, tvDiagnosa, tvJk;
        protected ImageView ivEdit, ivDelete;
        protected CardView cvDaftarPasien;

        public ItemRowHolder(View view) {
            super(view);
            this.tvNamaPasien = (TextView) view.findViewById(R.id.tvNamaPasien);
            this.tvHubungan = (TextView) view.findViewById(R.id.tvHubungan);
            this.tvDiagnosa = (TextView) view.findViewById(R.id.tvDiagnosa);
            this.tvJk = (TextView) view.findViewById(R.id.tvJk);
            this.cvDaftarPasien = (CardView) view.findViewById(R.id.cvDaftarPasien);
            this.ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
            this.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }
    }

}
