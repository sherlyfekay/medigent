package com.example.sherly.medigent.fitur.histori;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.histori.DetailHistoryModel;
import com.example.sherly.medigent.model.shift.DetailShiftModel;

import java.util.ArrayList;

public class DetailHistoriAdapter extends RecyclerView.Adapter<DetailHistoriAdapter.ItemRowHolder>{

    ArrayList<DetailShiftModel> detailShift;
    private Activity activity;

    public DetailHistoriAdapter(Activity activity, ArrayList<DetailShiftModel> detailShift) {
        this.detailShift = detailShift;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_detail_histori, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {

        holder.tvTindakan.setText(detailShift.get(i).getTindakan());
        holder.tvKondisi.setText(detailShift.get(i).getKondisi());

    }

    @Override
    public int getItemCount() {
        return (null != detailShift ? detailShift.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTindakan, tvKondisi;

        public ItemRowHolder(View view) {
            super(view);
            this.tvTindakan = (TextView) view.findViewById(R.id.tvTindakan);
            this.tvKondisi = (TextView)view.findViewById(R.id.tvKondisi);
        }
    }

}
