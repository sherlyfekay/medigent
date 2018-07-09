package com.example.sherly.medigent.fitur.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.pemesanan.PemesananActivity;
import com.example.sherly.medigent.fitur.penawaran.PenawaranActivity;
import com.example.sherly.medigent.model.mainmenu.DataRole;
import com.example.sherly.medigent.model.mainmenu.RoleModel;
import com.example.sherly.medigent.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {

    Button btnPenawaran, btnPemesanan;
    String id_role;
    ImageView ivIconRole;
    TextView tvNamaRole, tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);

        Intent intent = getIntent();
        id_role = intent.getStringExtra("id_role");

        btnPemesanan = (Button) findViewById(R.id.btnPemesanan);
        btnPenawaran = (Button) findViewById(R.id.btnPenawaran);
        ivIconRole = (ImageView) findViewById(R.id.ivIconRole);
        tvNamaRole = (TextView) findViewById(R.id.tvNamaRole);
        tvDesc = (TextView) findViewById(R.id.tvDesc);

        //Toast.makeText(MenuActivity.this, ""+id_role, Toast.LENGTH_LONG).show();

        ApiService.service_get.getDetailRole("Bearer "+ pref.getString("token", "null"), id_role).enqueue(new Callback<DataRole>() {
            @Override
            public void onResponse(Call<DataRole> call, Response<DataRole> response) {
                if(response.isSuccessful()) {
                    tvNamaRole.setText(response.body().getNama_role());
                    tvDesc.setText(response.body().getDesc());
                    Glide.with(getApplicationContext()).load(response.body().getIcon()).into(ivIconRole);
                }
                else {
                    Toast.makeText(MenuActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DataRole> call, Throwable t) {

            }
        });

        btnPenawaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent penawaran = new Intent(MenuActivity.this, PenawaranActivity.class);
                startActivity(penawaran);
            }
        });

        btnPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pemesanan = new Intent(MenuActivity.this, PemesananActivity.class);
                startActivity(pemesanan);
            }
        });
    }
}
