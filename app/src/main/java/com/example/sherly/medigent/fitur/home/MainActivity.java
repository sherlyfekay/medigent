package com.example.sherly.medigent.fitur.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.histori.HistoryActivity;
import com.example.sherly.medigent.fitur.login.LoginActivity;
import com.example.sherly.medigent.fitur.profil.ProfilActivity;
import com.example.sherly.medigent.model.artikel.ArticleModel;
import com.example.sherly.medigent.model.home.DaftarArtikelModel;
import com.example.sherly.medigent.model.mainmenu.RoleModel;
import com.example.sherly.medigent.model.user.UserModel;
import com.example.sherly.medigent.service.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDaftarArtikel;
    private DaftarArtikelAdapter artikelAdapter;
    private ExpandableHeightGridView gvMenuUtama;
    private ImageView ivHistori, ivSetting;
    private TextView tvName;
    SharedPreferences pref;
    String id_user, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        rvDaftarArtikel = (RecyclerView) findViewById(R.id.rvDaftarArtikel);
        ivHistori = (ImageView) findViewById(R.id.ivHistori);
        ivSetting = (ImageView) findViewById(R.id.ivSetting);
        tvName = (TextView) findViewById(R.id.tvName);

        gvMenuUtama = (ExpandableHeightGridView) findViewById(R.id.gvMenuUtama);

        createDaftarArtikel();

        ApiService.service_get.getRole().enqueue(new Callback<RoleModel>() {
            @Override
            public void onResponse(Call<RoleModel> call, Response<RoleModel> response) {
                if(response.isSuccessful()) {
                    GridMenuAdapter menuAdapter = new GridMenuAdapter(getApplicationContext(), response.body().getRoles());
                    gvMenuUtama.setAdapter(menuAdapter);
                    gvMenuUtama.setExpanded(true);
                    menuAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RoleModel> call, Throwable t) {

            }
        });

        ivHistori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSet = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intentSet);
            }
        });
    }

    private void createDaftarArtikel() {

        ApiService.service_get.getArticle("Bearer "+token).enqueue(new Callback<ArticleModel>() {
            @Override
            public void onResponse(Call<ArticleModel> call, Response<ArticleModel> response) {
                if(response.isSuccessful()) {
                    artikelAdapter = new DaftarArtikelAdapter(MainActivity.this, response.body().getArticles());
                    rvDaftarArtikel.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvDaftarArtikel.setAdapter(artikelAdapter);
                    rvDaftarArtikel.getAdapter().notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApiService.service_get.getDetailUser("Bearer "+token, pref.getString("id_user", "null")).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if(response.isSuccessful()) {
                    tvName.setText(response.body().getNama_lengkap());
                }
                else {
                    Toast.makeText(MainActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }
}
