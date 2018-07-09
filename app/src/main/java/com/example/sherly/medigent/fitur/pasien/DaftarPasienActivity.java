package com.example.sherly.medigent.fitur.pasien;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.pasien.GetPatientModel;
import com.example.sherly.medigent.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarPasienActivity extends AppCompatActivity {

    SharedPreferences pref;
    String id_user, token;
    DaftarPasienAdapter pasienAdapter;
    RecyclerView rvDaftarPasien;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pasien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Daftar Pasien");

        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        rvDaftarPasien = (RecyclerView) findViewById(R.id.rvDaftarPasien);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPasien = new Intent(DaftarPasienActivity.this, TambahPasienActivity.class);
                startActivity(addPasien);
            }
        });

        ApiService.service_get.getPatientByUser("Bearer "+ token, id_user).enqueue(new Callback<GetPatientModel>() {
            @Override
            public void onResponse(Call<GetPatientModel> call, Response<GetPatientModel> response) {
                if(response.isSuccessful()) {
                    pasienAdapter = new DaftarPasienAdapter(DaftarPasienActivity.this, response.body().getPatients());
                    rvDaftarPasien.setLayoutManager(new LinearLayoutManager(DaftarPasienActivity.this));
                    //rvDaftarHistori.setFocusable(false);
                    //rvDaftarHistori.setNestedScrollingEnabled(false);
                    rvDaftarPasien.setAdapter(pasienAdapter);
                    pasienAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(DaftarPasienActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPatientModel> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
