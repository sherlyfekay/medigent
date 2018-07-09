package com.example.sherly.medigent.fitur.histori;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.histori.HistoryModel;
import com.example.sherly.medigent.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvDaftarHistori;
    private DaftarHistoriAdapter historiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Histori");

        final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);
        final String id_user = pref.getString("id_user", "null");
        final String token = pref.getString("token", "null");

        rvDaftarHistori = (RecyclerView) findViewById(R.id.rvDaftarHistori);

        ApiService.service_get.getHistoryByUser("Bearer "+token, id_user).enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if(response.isSuccessful()) {

                    historiAdapter = new DaftarHistoriAdapter(HistoryActivity.this, response.body().getDetailHistoryModels());
                    rvDaftarHistori.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    //rvDaftarHistori.setFocusable(false);
                    //rvDaftarHistori.setNestedScrollingEnabled(false);
                    rvDaftarHistori.setAdapter(historiAdapter);
                    historiAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(HistoryActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
