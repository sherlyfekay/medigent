package com.example.sherly.medigent.fitur.pasien;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.DeleteModel;
import com.example.sherly.medigent.model.pasien.PostPatientModel;
import com.example.sherly.medigent.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

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

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences pref = activity.getSharedPreferences("medigent", MODE_PRIVATE);
                final String token = pref.getString("token", "null");

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                String msg = "Apakah Anda yakin ingin menghapus data pasien "+ patientModel.getNama_lengkap()+" ?";

                builder.setMessage(msg);

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        ApiService.service_delete.deletePatient("Bearer "+token, patientModel.get_id()).enqueue(new Callback<DeleteModel>() {
                            @Override
                            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                if(response.isSuccessful()){
                                    //Toast.makeText(activity, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(activity, "Data pasien berhasil dihapus", Toast.LENGTH_SHORT).show();

                                    activity.finish();
                                    activity.overridePendingTransition(0, 0);
                                    activity.startActivity(activity.getIntent());
                                    activity.overridePendingTransition(0, 0);
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteModel> call, Throwable t) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //return false;
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditPasienActivity.class);
                intent.putExtra("id_patient", patientModel.get_id());
                activity.startActivity(intent);
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
