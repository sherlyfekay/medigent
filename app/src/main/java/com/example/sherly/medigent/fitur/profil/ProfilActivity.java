package com.example.sherly.medigent.fitur.profil;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.alamat.DaftarAlamatActivity;
import com.example.sherly.medigent.fitur.login.LoginActivity;
import com.example.sherly.medigent.fitur.pasien.DaftarPasienActivity;
import com.example.sherly.medigent.model.user.UpdateUserModel;
import com.example.sherly.medigent.model.user.UserModel;
import com.example.sherly.medigent.service.ApiService;
import com.example.sherly.medigent.service.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilActivity extends AppCompatActivity {

    TextView tvNamaLengkap, tvTglLahir, tvJk, tvTelepon, tvLogout;
    LinearLayout llNama, llTglLahir, llJk, llTelepon, lin_patient, lin_address;
    Session session;
    String nama_lengkap, jk, tgl_lahir, telepon;
    SharedPreferences pref;
    String id_user, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");

        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }

        tvNamaLengkap = (TextView) findViewById(R.id.tvNamaLengkap);
        tvTglLahir = (TextView) findViewById(R.id.tvTglLahir);
        tvJk = (TextView) findViewById(R.id.tvJk);
        tvTelepon = (TextView) findViewById(R.id.tvTelepon);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        llNama = (LinearLayout) findViewById(R.id.llNama);
        llTglLahir = (LinearLayout) findViewById(R.id.llTglLahir);
        llJk = (LinearLayout) findViewById(R.id.llJk);
        llTelepon = (LinearLayout) findViewById(R.id.llTelepon);
        lin_patient = (LinearLayout) findViewById(R.id.lin_patient);
        lin_address = (LinearLayout) findViewById(R.id.lin_address);

        kosong();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ApiService.service_get.getDetailUser("Bearer "+ pref.getString("token", "null"), pref.getString("id_user", "null")).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    nama_lengkap = response.body().getNama_lengkap();
                    tgl_lahir = response.body().getTgl_lahir();
                    jk = response.body().getJk();
                    telepon = response.body().getTelepon();

                    tvNamaLengkap.setText(nama_lengkap);
                    tvTglLahir.setText(tgl_lahir);
                    tvJk.setText(jk);
                    tvTelepon.setText(telepon);
                }
                else {
                    Toast.makeText(ProfilActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

        llNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);
                final EditText etEditNama = (EditText) dialogView.findViewById(R.id.etEdit);

                etEditNama.setText(nama_lengkap);

                builder.setCancelable(false);
                builder.setView(dialogView);
                builder.setMessage("Nama Lengkap");

                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String nama_lengkap = etEditNama.getText().toString();

                        UpdateUserModel newUpdate = new UpdateUserModel("nama_lengkap", nama_lengkap);
                        ApiService.service_patch.patchUser("Bearer "+pref.getString("token", "null"), pref.getString("id_user", "null"), newUpdate).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplication(), "Nama lengkap Anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    tvNamaLengkap.setText(nama_lengkap);
                                    dialogInterface.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplication(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

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
            }
        });

        llTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);
                final EditText etEditTglLahir = (EditText) dialogView.findViewById(R.id.etEdit);

                etEditTglLahir.setText(tgl_lahir);

                builder.setCancelable(false);
                builder.setView(dialogView);
                builder.setMessage("Tanggal Lahir");

                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String tgl_lahir = etEditTglLahir.getText().toString();

                        UpdateUserModel newUpdate = new UpdateUserModel("tgl_lahir", tgl_lahir);
                        ApiService.service_patch.patchUser("Bearer "+pref.getString("token", "null"), pref.getString("id_user", "null"), newUpdate).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplication(), "Tanggal lahir Anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    tvTglLahir.setText(tgl_lahir);
                                    dialogInterface.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplication(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

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
            }
        });

        llJk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);
                final EditText etEditJk = (EditText) dialogView.findViewById(R.id.etEdit);

                etEditJk.setText(jk);

                builder.setCancelable(false);
                builder.setView(dialogView);
                builder.setMessage("Jenis Kelamin");

                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String jk = etEditJk.getText().toString();

                        UpdateUserModel newUpdate = new UpdateUserModel("jk", jk);
                        ApiService.service_patch.patchUser("Bearer "+pref.getString("token", "null"), pref.getString("id_user", "null"), newUpdate).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplication(), "Jenis Kelamin Anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    tvJk.setText(jk);
                                    dialogInterface.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplication(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

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
            }
        });

        llTelepon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alertdialog_custom_view, null);
                final EditText etEditTelepon = (EditText) dialogView.findViewById(R.id.etEdit);

                etEditTelepon.setText(telepon);

                builder.setCancelable(false);
                builder.setView(dialogView);
                builder.setMessage("Telepon");

                builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String telepon = etEditTelepon.getText().toString();

                        UpdateUserModel newUpdate = new UpdateUserModel("telepon", telepon);
                        ApiService.service_patch.patchUser("Bearer "+pref.getString("token", "null"), pref.getString("id_user", "null"), newUpdate).enqueue(new Callback<UserModel>() {
                            @Override
                            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(getApplication(), "Telepon Anda berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    tvTelepon.setText(telepon);
                                    dialogInterface.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplication(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserModel> call, Throwable t) {

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
            }
        });

        lin_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pasien = new Intent(ProfilActivity.this, DaftarPasienActivity.class);
                startActivity(pasien);
            }
        });

        lin_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alamat = new Intent(ProfilActivity.this, DaftarAlamatActivity.class);
                startActivity(alamat);
            }
        });
    }

    private void logout(){
        session.setLoggedin(false, "null", "null");
        finish();
        startActivity(new Intent(ProfilActivity.this,LoginActivity.class));
    }

    private void kosong() {
        tvNamaLengkap.setText(null);
        tvTglLahir.setText(null);
        tvTelepon.setText(null);
        tvJk.setText(null);
        tvTelepon.setText(null);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
