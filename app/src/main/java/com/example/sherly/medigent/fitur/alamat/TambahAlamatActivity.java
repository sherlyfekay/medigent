package com.example.sherly.medigent.fitur.alamat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.sherly.medigent.R;

public class TambahAlamatActivity extends AppCompatActivity {

    Spinner spProvinsi, spKota, spKecamatan, spDesa;
    String[] provinsi = {"-Pilih-", "Jawa Timur", "Jawa Tengah", "Jawa Barat"};
    String[] kota = {"-Pilih-", "Kota Surabaya", "Kota Madiun", "Kota Kediri"};
    String[] kecamatan = {"-Pilih-", "Sukolilo", "Wonokromo", "Rungkut"};
    String[] desa = {"-Pilih-", "Keputih", "Semolowaru", "Klampisngasem"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_alamat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tambah Alamat");

        spProvinsi = (Spinner) findViewById(R.id.spProvinsi);
        spKota = (Spinner) findViewById(R.id.spKota);
        spKecamatan = (Spinner) findViewById(R.id.spKecamatan);
        spDesa = (Spinner) findViewById(R.id.spDesa);

        ArrayAdapter<String> adapterProv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinsi);
        spProvinsi.setAdapter(adapterProv);

        ArrayAdapter<String> adapterKota = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kota);
        spKota.setAdapter(adapterKota);

        ArrayAdapter<String> adapterKec = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kecamatan);
        spKecamatan.setAdapter(adapterKec);

        ArrayAdapter<String> adapterDesa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, desa);
        spDesa.setAdapter(adapterDesa);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
