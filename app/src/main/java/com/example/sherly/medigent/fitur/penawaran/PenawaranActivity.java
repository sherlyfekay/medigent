package com.example.sherly.medigent.fitur.penawaran;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sherly.medigent.fitur.alamat.MapsActivity;
import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.pasien.TambahPasienActivity;
import com.example.sherly.medigent.model.alamat.AddressModel;
import com.example.sherly.medigent.model.alamat.GetAddressModel;
import com.example.sherly.medigent.model.pasien.GetPatientModel;
import com.example.sherly.medigent.model.pasien.PostPatientModel;
import com.example.sherly.medigent.model.penawaran.OfferModel;
import com.example.sherly.medigent.model.penawaran.PostOfferModel;
import com.example.sherly.medigent.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenawaranActivity extends AppCompatActivity {

    Spinner spNamaPasien, spAlamatPasien, spBiaya;
    ImageView ivTbhPasien, ivTbhAlamat;
    String[] biaya = {"Rp. 100.000 - Rp. 200.000", "Rp. 200.000 - Rp. 500.000"};
    ArrayList<PostPatientModel> patients;
    ArrayList<AddressModel> addresses;
    ArrayList<String> id_patient;
    ArrayList<String> id_address;
    RadioButton rbLk, rbPr;
    EditText etJmlShift, etTglMulai, etInfoLain;
    Button btnSearch;
    String selectedJk, selectedAddress, selectedPatient, selectedBiaya;
    SharedPreferences pref;
    String id_user, token;
    Calendar mCalendar, today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penawaran);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Penawaran");

        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        spNamaPasien = (Spinner) findViewById(R.id.spNamaPasien);
        spAlamatPasien = (Spinner) findViewById(R.id.spAlamatPasien);
        spBiaya = (Spinner) findViewById(R.id.spBiaya);
        ivTbhAlamat = (ImageView) findViewById(R.id.ivTbhAlamat);
        ivTbhPasien = (ImageView) findViewById(R.id.ivTbhPasien);
        id_patient = new ArrayList<>();
        id_address = new ArrayList<>();
        rbLk = (RadioButton) findViewById(R.id.rbLk);
        rbPr = (RadioButton) findViewById(R.id.rbPr);
        etJmlShift = (EditText) findViewById(R.id.etJmlShift);
        etTglMulai = (EditText) findViewById(R.id.etTglMulai);
        etInfoLain = (EditText) findViewById(R.id.etInfoLain);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        mCalendar = Calendar.getInstance();
        today = Calendar.getInstance();

        etTglMulai.setFocusable(false);
        etTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PenawaranActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //datePicker.setMinDate(mCalendar.getTime()-(mCalendar.getTime()%(24*60*60*1000)));
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String dateFormat = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

                        //today.setTimeInMillis();
                        if(mCalendar.after(Calendar.getInstance()) || DateUtils.isToday(mCalendar.getTimeInMillis())) {
                            etTglMulai.setText(simpleDateFormat.format(mCalendar.getTime()));
                        }
                        else {
                            Toast.makeText(PenawaranActivity.this, "Tolong pilih tanggal hari ini atau setelah hari ini", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<String> adapterBiaya = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, biaya);
        spBiaya.setAdapter(adapterBiaya);

        spNamaPasien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPatient = id_patient.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spAlamatPasien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAddress = id_address.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spBiaya.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBiaya = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(PenawaranActivity.this, ""+selectedBiaya, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivTbhPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tbhPasien = new Intent(PenawaranActivity.this, TambahPasienActivity.class);
                startActivity(tbhPasien);
            }
        });

        ivTbhAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tbhAlamat = new Intent(PenawaranActivity.this, MapsActivity.class);
                startActivity(tbhAlamat);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbLk.isChecked()) {
                    selectedJk = rbLk.getText().toString();
                }
                else if(rbPr.isChecked()) {
                    selectedJk = rbPr.getText().toString();
                }
                else {
                    Toast.makeText(PenawaranActivity.this, "Mohon lengkapi data penawaran Anda", Toast.LENGTH_SHORT).show();
                }

                if (etJmlShift.getText().toString().isEmpty() || etTglMulai.getText().toString().isEmpty() || etInfoLain.getText().toString().isEmpty() || selectedAddress.isEmpty() || selectedPatient.isEmpty()) {
                    Toast.makeText(PenawaranActivity.this, "Mohon lengkapi data penawaran Anda", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer jml_shift = Integer.parseInt(etJmlShift.getText().toString());
                    String tgl_mulai = etTglMulai.getText().toString();
                    String info = etInfoLain.getText().toString();
                    Integer jenis = 2;
                    Integer status = 1;
                    String created_at = "20 Mei 2018";

                    PostOfferModel postOfferModel = new PostOfferModel(selectedJk, tgl_mulai, selectedBiaya, info, id_user, selectedAddress, selectedPatient, created_at, jml_shift, jenis, status);

                    ApiService.service_post.postOffer("Bearer "+token, postOfferModel).enqueue(new Callback<OfferModel>() {
                        @Override
                        public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {
                            if(response.isSuccessful()) {
                                String status = response.body().getStatus();

                                if(status.equals("100")) {
                                    Toast.makeText(PenawaranActivity.this, "Data penawaran berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(PenawaranActivity.this, ""+"Terjadi kesalahan", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(PenawaranActivity.this, ""+"Data tidak ditemukan", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OfferModel> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //GET nama pasien untuk spinner
        ApiService.service_get.getPatientByUser("Bearer "+ pref.getString("token", "null"), pref.getString("id_user", "null")).enqueue(new Callback<GetPatientModel>() {
            @Override
            public void onResponse(Call<GetPatientModel> call, Response<GetPatientModel> response) {
                if(response.isSuccessful()) {
                    patients = response.body().getPatients();
                    ArrayList<String> namaPasien = new ArrayList<>();

                    for (int i=0; i < response.body().getCount(); i++) {
                        namaPasien.add(patients.get(i).getNama_lengkap());
                        id_patient.add(patients.get(i).get_id());
                    }

                    ArrayAdapter<String> adapterNama = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, namaPasien);
                    spNamaPasien.setAdapter(adapterNama);
                    adapterNama.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(PenawaranActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPatientModel> call, Throwable t) {

            }
        });

        //GET alamat untuk spinner
        ApiService.service_get.getAddressByUser("Bearer "+ pref.getString("token", "null"), pref.getString("id_user", "null")).enqueue(new Callback<GetAddressModel>() {
            @Override
            public void onResponse(Call<GetAddressModel> call, Response<GetAddressModel> response) {
                if(response.isSuccessful()) {
                    addresses = response.body().getAddresses();
                    ArrayList<String> alamatPasien = new ArrayList<>();

                    for (int i=0; i < response.body().getCount(); i++) {
                        alamatPasien.add(addresses.get(i).getAlamat_lengkap());
                        id_address.add(addresses.get(i).get_id());
                    }

                    ArrayAdapter<String> adapterAlamat = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, alamatPasien);
                    spAlamatPasien.setAdapter(adapterAlamat);
                    adapterAlamat.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(PenawaranActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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
