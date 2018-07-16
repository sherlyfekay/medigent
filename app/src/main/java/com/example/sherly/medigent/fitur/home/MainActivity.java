package com.example.sherly.medigent.fitur.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.histori.DaftarHistoriActivity;
import com.example.sherly.medigent.fitur.histori.HistoryActivity;
import com.example.sherly.medigent.fitur.login.LoginActivity;
import com.example.sherly.medigent.fitur.profil.ProfilActivity;
import com.example.sherly.medigent.model.DeleteModel;
import com.example.sherly.medigent.model.artikel.ArticleModel;
import com.example.sherly.medigent.model.home.DaftarArtikelModel;
import com.example.sherly.medigent.model.mainmenu.RoleModel;
import com.example.sherly.medigent.model.user.UpdateFotoModel;
import com.example.sherly.medigent.model.user.UserModel;
import com.example.sherly.medigent.service.ApiService;
import com.example.sherly.medigent.service.FilePath;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDaftarArtikel;
    private DaftarArtikelAdapter artikelAdapter;
    private ExpandableHeightGridView gvMenuUtama;
    private ImageView ivHistori, ivSetting, imgProfile, ivEditFoto;
    private TextView tvName;
    SharedPreferences pref;
    String id_user, token;
    MultipartBody.Part fileFoto;

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
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        ivEditFoto = (ImageView) findViewById(R.id.ivEditFoto);

        gvMenuUtama = (ExpandableHeightGridView) findViewById(R.id.gvMenuUtama);

        createDaftarArtikel();

        ivEditFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(MainActivity.this);
                Toast.makeText(MainActivity.this, "Edit foto", Toast.LENGTH_SHORT).show();
            }
        });

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
                //Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                Intent intent = new Intent(MainActivity.this, DaftarHistoriActivity.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
            if(resultCode == RESULT_OK) {
                try {
                    //Toast.makeText(this, "Masuk", Toast.LENGTH_SHORT).show();
                    addFile(new Compressor(this).compressToFile(new File(FilePath.getPath(this, result.getUri()))));
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addFile(File file) {
        RequestBody foto = RequestBody.create(MediaType.parse("image/jpeg"), file);
        fileFoto = MultipartBody.Part.createFormData("foto", file.getName(), foto);

        //Toast.makeText(this, "sesuatu", Toast.LENGTH_SHORT).show();

        ApiService.service_post.updateFoto("Bearer "+token, id_user, fileFoto).enqueue(new Callback<UpdateFotoModel>() {
            @Override
            public void onResponse(Call<UpdateFotoModel> call, Response<UpdateFotoModel> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, ""+response.body().getFoto(), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else {
                    Toast.makeText(MainActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateFotoModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Glide.with(MainActivity.this).load(response.body().getFoto()).into(imgProfile);
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
