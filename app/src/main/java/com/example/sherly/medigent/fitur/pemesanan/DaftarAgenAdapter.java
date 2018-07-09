package com.example.sherly.medigent.fitur.pemesanan;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.agent.DataAgentModel;

import java.util.ArrayList;

public class DaftarAgenAdapter extends RecyclerView.Adapter<DaftarAgenAdapter.ItemRowHolder>{

    ArrayList<DataAgentModel> dataAgen;
    private Activity activity;

    public DaftarAgenAdapter(Activity activity, ArrayList<DataAgentModel> dataAgen) {
        this.dataAgen = dataAgen;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_daftar_agen, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
        final DataAgentModel agentModel = dataAgen.get(i);

        holder.tvNamaAgen.setText(dataAgen.get(i).getNama_lengkap());
        holder.tvJkAgen.setText(dataAgen.get(i).getJk());
        holder.tvAlamatAgen.setText(dataAgen.get(i).getAlamat_lengkap());
        holder.rbAgen.setRating(5);
        Glide.with(activity).load(dataAgen.get(i).getFoto()).into(holder.ivFotoAgen);

//        holder.cvDaftarHis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(activity, DetailHistoryActivity.class);
//                intent.putExtra("id_orderoffer", detailHistoryModel.get_id());
//                activity.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != dataAgen ? dataAgen.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvNamaAgen, tvJkAgen, tvAlamatAgen;
        protected ImageView ivFotoAgen;
        protected RatingBar rbAgen;
        //protected CardView cvDaftarHis;

        public ItemRowHolder(View view) {
            super(view);
            this.ivFotoAgen = (ImageView) view.findViewById(R.id.ivFotoAgen);
            this.tvNamaAgen = (TextView)view.findViewById(R.id.tvNamaAgen);
            this.tvJkAgen = (TextView) view.findViewById(R.id.tvJkAgen);
            this.tvAlamatAgen = (TextView)view.findViewById(R.id.tvAlamatAgen);
            this.rbAgen = (RatingBar) view.findViewById(R.id.rbAgen);
            //this.cvDaftarHis = (CardView) view.findViewById(R.id.cvDaftarHis);
        }
    }

}