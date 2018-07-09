package com.example.sherly.medigent.fitur.alamat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.alamat.GetAddressModel;
import com.example.sherly.medigent.service.ApiService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarAlamatActivity extends AppCompatActivity {

    private static final String TAG = "DaftarAlamatActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    SharedPreferences pref;
    String id_user, token;
    FloatingActionButton fab;
    DaftarAlamatAdapter alamatAdapter;
    RecyclerView rvDaftarAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_alamat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Daftar Alamat");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        rvDaftarAlamat = (RecyclerView) findViewById(R.id.rvDaftarAlamat);
        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");



        if(isServicesOK()) {
            init();
        }
    }

    private void init() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addAddress = new Intent(DaftarAlamatActivity.this, MapsActivity.class);
                startActivity(addAddress);
            }
        });
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DaftarAlamatActivity.this);

        if(available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DaftarAlamatActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(DaftarAlamatActivity.this, "You cant make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApiService.service_get.getAddressByUser("Bearer "+ token, id_user).enqueue(new Callback<GetAddressModel>() {
            @Override
            public void onResponse(Call<GetAddressModel> call, Response<GetAddressModel> response) {
                if(response.isSuccessful()) {
                    alamatAdapter = new DaftarAlamatAdapter(DaftarAlamatActivity.this, response.body().getAddresses());
                    rvDaftarAlamat.setLayoutManager(new LinearLayoutManager(DaftarAlamatActivity.this));
                    //rvDaftarHistori.setFocusable(false);
                    //rvDaftarHistori.setNestedScrollingEnabled(false);
                    rvDaftarAlamat.setAdapter(alamatAdapter);
                    alamatAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(DaftarAlamatActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetAddressModel> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
