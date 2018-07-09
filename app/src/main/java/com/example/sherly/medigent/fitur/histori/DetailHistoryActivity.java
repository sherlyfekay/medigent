package com.example.sherly.medigent.fitur.histori;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.histori.DetailHistoryModel;
import com.example.sherly.medigent.model.shift.ShiftModel;
import com.example.sherly.medigent.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHistoryActivity extends AppCompatActivity {

    private ArrayList<DetailHistoryModel> detailHistori = new ArrayList<>();
    private RecyclerView rvDetailHistori;
    private DetailHistoriAdapter historiAdapter;
    private TextView tvCreatedAt, tvNamaPasien, tvDiagnosa, tvRole, tvNamaAgent, tvJumlahShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Histori");

        Intent intent = getIntent();
        String id_orderoffer = intent.getStringExtra("id_orderoffer");

        final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);
        final String id_user = pref.getString("id_user", "null");
        final String token = pref.getString("token", "null");

        rvDetailHistori = (RecyclerView) findViewById(R.id.rvDetailHistori);
        tvCreatedAt = (TextView) findViewById(R.id.tvCreatedAt);
        tvNamaPasien = (TextView) findViewById(R.id.tvNamaPasien);
        tvDiagnosa = (TextView) findViewById(R.id.tvDiagnosa);
        tvRole = (TextView) findViewById(R.id.tvRole);
        tvNamaAgent = (TextView) findViewById(R.id.tvNamaAgent);
        tvJumlahShift = (TextView) findViewById(R.id.tvJumlahShift);

        ApiService.service_get.getHistoryByOO("Bearer "+token, id_orderoffer).enqueue(new Callback<DetailHistoryModel>() {
            @Override
            public void onResponse(Call<DetailHistoryModel> call, Response<DetailHistoryModel> response) {
                if(response.isSuccessful()) {
                    tvCreatedAt.setText(response.body().getCreated_at());
                    tvNamaPasien.setText(response.body().getNama_pasien());
                    tvDiagnosa.setText(response.body().getDiagnosa());
                    tvRole.setText(response.body().getRole());
                    tvNamaAgent.setText(response.body().getNama_agent());
                    tvJumlahShift.setText(""+response.body().getJml_shift());
                }
                else {
                    Toast.makeText(DetailHistoryActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DetailHistoryModel> call, Throwable t) {

            }
        });

        ApiService.service_get.getShiftByOO("Bearer "+token, id_orderoffer).enqueue(new Callback<ShiftModel>() {
            @Override
            public void onResponse(Call<ShiftModel> call, Response<ShiftModel> response) {
                if(response.isSuccessful()) {
                    historiAdapter = new DetailHistoriAdapter(DetailHistoryActivity.this, response.body().getShifts());
                    rvDetailHistori.setLayoutManager(new LinearLayoutManager(DetailHistoryActivity.this));
                    rvDetailHistori.setFocusable(false);
                    rvDetailHistori.setNestedScrollingEnabled(false);
                    rvDetailHistori.setAdapter(historiAdapter);
                }
                else {
                    Toast.makeText(DetailHistoryActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ShiftModel> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
