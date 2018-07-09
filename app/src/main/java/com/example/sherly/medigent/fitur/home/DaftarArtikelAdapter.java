package com.example.sherly.medigent.fitur.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.artikel.DetailArticleModel;
import com.example.sherly.medigent.model.home.DaftarArtikelModel;
import com.example.sherly.medigent.model.mainmenu.DataRole;
import com.example.sherly.medigent.model.mainmenu.RoleModel;

import java.util.ArrayList;

public class DaftarArtikelAdapter extends RecyclerView.Adapter<DaftarArtikelAdapter.ItemRowHolder>{

    private Activity activity;
    ArrayList<DetailArticleModel> data;

    public DaftarArtikelAdapter(Activity activity, ArrayList<DetailArticleModel> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_daftar_artikel, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {

        final DetailArticleModel article = data.get(i);
        holder.tvJudulAr.setText(article.getJudul());
        holder.tvSumberAr.setText(article.getSumber());
        Glide.with(activity).load(article.getGambar()).into(holder.ivFotoAr);

        holder.cvDaftarArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected ImageView ivFotoAr;
        protected TextView tvJudulAr;
        protected TextView tvSumberAr;
        protected CardView cvDaftarArtikel;

        public ItemRowHolder(View view) {
            super(view);
            this.ivFotoAr = (ImageView) view.findViewById(R.id.ivFotoAr);
            this.tvJudulAr = (TextView) view.findViewById(R.id.tvJudulAr);
            this.tvSumberAr = (TextView)view.findViewById(R.id.tvSumberAr);
            this.cvDaftarArtikel = (CardView) view.findViewById(R.id.cvDaftarArtikel);
        }
    }

}
