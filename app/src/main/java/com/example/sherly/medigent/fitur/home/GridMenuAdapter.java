package com.example.sherly.medigent.fitur.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.mainmenu.MenuActivity;
import com.example.sherly.medigent.model.mainmenu.DataRole;

import java.util.ArrayList;

public class GridMenuAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataRole> data;
    LayoutInflater inflter;

    public GridMenuAdapter(Context applicationContext, ArrayList<DataRole> data) {
        this.context = applicationContext;
        this.data = data;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_grid_view_menu, null); // inflate the layout

        final DataRole role = data.get(i);

        ImageView icon = (ImageView) view.findViewById(R.id.ivGbrMenu); // get the reference of ImageView
        TextView jenis = (TextView) view.findViewById(R.id.tvJenisAgen);
        jenis.setText(role.getNama_role());
        Glide.with(context).load(role.getIcon()).into(icon);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, ""+role.get_id(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("id_role", role.get_id());
                context.startActivity(intent);
            }
        });

        return view;
    }
}
