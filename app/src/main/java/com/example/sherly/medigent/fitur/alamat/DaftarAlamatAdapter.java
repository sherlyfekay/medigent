package com.example.sherly.medigent.fitur.alamat;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.DeleteModel;
import com.example.sherly.medigent.model.alamat.AddressModel;
import com.example.sherly.medigent.model.user.UpdateUserModel;
import com.example.sherly.medigent.service.ApiService;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DaftarAlamatAdapter extends RecyclerView.Adapter<DaftarAlamatAdapter.ItemRowHolder>{

    ArrayList<AddressModel> dataAlamat;
    private Activity activity;


    public DaftarAlamatAdapter(Activity activity, ArrayList<AddressModel> dataAlamat) {
        this.dataAlamat = dataAlamat;
        this.activity = activity;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_daftar_alamat, viewGroup, false);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
        final AddressModel addressModel = dataAlamat.get(i);

        holder.tvJudul.setText(addressModel.getJudul());
        holder.tvAlamat.setText("Alamat : "+addressModel.getAlamat_lengkap());
        holder.tvTambahan.setText("Info : "+addressModel.getTambahan());

        holder.cvDaftarAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(activity, DetailHistoryActivity.class);
//                intent.putExtra("id_orderoffer", detailHistoryModel.get_id());
//                activity.startActivity(intent);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);
                final EditText etTambahan = (EditText) dialogView.findViewById(R.id.etEdit);

                etTambahan.setText(addressModel.getTambahan());

                builder.setCancelable(false);
                builder.setView(dialogView);
                builder.setMessage("Informasi Tambahan : ");

                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final SharedPreferences pref = activity.getSharedPreferences("medigent", MODE_PRIVATE);
                        final String token = pref.getString("token", "null");

                        final String tambahan = etTambahan.getText().toString();
                        //final String tgl_lahir = etEditTglLahir.getText().toString();
                        Toast.makeText(activity, ""+tambahan+", "+addressModel.get_id(), Toast.LENGTH_SHORT).show();

                        UpdateUserModel newUpdate = new UpdateUserModel("tambahan", tambahan);
                        ApiService.service_patch.patchAddress("Bearer "+token, addressModel.get_id(), newUpdate).enqueue(new Callback<DeleteModel>() {
                            @Override
                            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(activity, "Berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    //tvTglLahir.setText(tgl_lahir);
                                    //dialogInterface.dismiss();
                                    activity.finish();
                                    activity.overridePendingTransition(0, 0);
                                    activity.startActivity(activity.getIntent());
                                    activity.overridePendingTransition(0, 0);
                                }
                                else {
                                    Toast.makeText(activity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences pref = activity.getSharedPreferences("medigent", MODE_PRIVATE);
                final String token = pref.getString("token", "null");

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                builder.setMessage("Apakah Anda yakin ingin menghapus alamat ini ?");

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        ApiService.service_delete.deleteAddress("Bearer "+token, addressModel.get_id()).enqueue(new Callback<DeleteModel>() {
                            @Override
                            public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                if(response.isSuccessful()){
                                    //Toast.makeText(activity, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(activity, "Alamat berhasil dihapus", Toast.LENGTH_SHORT).show();

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
    }

    @Override
    public int getItemCount() {
        return (null != dataAlamat ? dataAlamat.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvJudul, tvAlamat, tvTambahan;
        protected ImageView ivEdit, ivDelete;
        protected CardView cvDaftarAlamat;

        public ItemRowHolder(View view) {
            super(view);
            this.tvJudul = (TextView) view.findViewById(R.id.tvNamaPasien);
            //this.tvHubungan = (TextView) view.findViewById(R.id.tvHubungan);
            this.tvAlamat = (TextView) view.findViewById(R.id.tvDiagnosa);
            this.tvTambahan = (TextView) view.findViewById(R.id.tvJk);
            this.cvDaftarAlamat = (CardView) view.findViewById(R.id.cvDaftarPasien);
            this.ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
            this.ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        }
    }

}