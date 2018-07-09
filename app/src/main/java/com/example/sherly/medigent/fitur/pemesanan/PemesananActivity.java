package com.example.sherly.medigent.fitur.pemesanan;

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
import com.example.sherly.medigent.model.pemesanan.OrderModel;
import com.example.sherly.medigent.model.pemesanan.PostOrderModel;
import com.example.sherly.medigent.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananActivity extends AppCompatActivity {

    Spinner spNamaPasien, spAlamatPasien, spJnsLayanan;
    ImageView ivTbhPasien, ivTbhAlamat;
    String[] jnsLayanan = {"Kunjungan Rumah", "Rawat Jalan"};
    ArrayList<PostPatientModel> patients;
    ArrayList<AddressModel> addresses;
    ArrayList<String> id_patient;
    ArrayList<String> id_address;
    Button btnSearch;
    RadioButton rbLk, rbPr;
    EditText etJmlShift, etTglMulai;
    String selectedJk, selectedAddress, selectedPatient, selectedLayanan;
    SharedPreferences pref;
    String id_user, token;
    Calendar mCalendar, today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Pemesanan");

        pref = getSharedPreferences("medigent", MODE_PRIVATE);
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        spNamaPasien = (Spinner) findViewById(R.id.spNamaPasien);
        spAlamatPasien = (Spinner) findViewById(R.id.spAlamatPasien);
        spJnsLayanan = (Spinner) findViewById(R.id.spJnsLayanan);
        ivTbhAlamat = (ImageView) findViewById(R.id.ivTbhAlamat);
        ivTbhPasien = (ImageView) findViewById(R.id.ivTbhPasien);
        id_patient = new ArrayList<>();
        id_address = new ArrayList<>();
        btnSearch = (Button) findViewById(R.id.btnSearch);
        rbLk = (RadioButton) findViewById(R.id.rbLk);
        rbPr = (RadioButton) findViewById(R.id.rbPr);
        etJmlShift = (EditText) findViewById(R.id.etJmlShift);
        etTglMulai = (EditText) findViewById(R.id.etTglMulai);
        mCalendar = Calendar.getInstance();
        today = Calendar.getInstance();

        etTglMulai.setFocusable(false);
        etTglMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PemesananActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                            Toast.makeText(PemesananActivity.this, "Tolong pilih tanggal hari ini atau setelah hari ini", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<String> adapterJL = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jnsLayanan);
        spJnsLayanan.setAdapter(adapterJL);

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

        spJnsLayanan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLayanan = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(PemesananActivity.this, ""+selectedLayanan, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivTbhPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tbhPasien = new Intent(PemesananActivity.this, TambahPasienActivity.class);
                startActivity(tbhPasien);
            }
        });

        ivTbhAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tbhAlamat = new Intent(PemesananActivity.this, MapsActivity.class);
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
                    Toast.makeText(PemesananActivity.this, "Mohon lengkapi data pemesanan Anda", Toast.LENGTH_SHORT).show();
                }

                if (etJmlShift.getText().toString().isEmpty() || etTglMulai.getText().toString().isEmpty() || selectedAddress.isEmpty() || selectedPatient.isEmpty()) {
                    Toast.makeText(PemesananActivity.this, "Mohon lengkapi data pemesanan Anda", Toast.LENGTH_SHORT).show();
                }
                else {
                    Integer jml_shift = Integer.parseInt(etJmlShift.getText().toString());
                    String tgl_mulai = etTglMulai.getText().toString();
                    Integer jenis = 1;
                    Integer status = 1;
                    String created_at = "20 Mei 2018";

                    PostOrderModel postOrderModel = new PostOrderModel(jenis, id_user, selectedPatient, selectedAddress, selectedJk, tgl_mulai, selectedLayanan, jml_shift, status, created_at);

                    ApiService.service_post.postOrder("Bearer "+token, postOrderModel).enqueue(new Callback<OrderModel>() {
                        @Override
                        public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                            if(response.isSuccessful()) {
                                String status = response.body().getStatus();

                                if(status.equals("100")) {
                                    Toast.makeText(PemesananActivity.this, "Data pemesanan berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    //finish();
                                    Intent intent = new Intent(PemesananActivity.this, ResultMapsActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(PemesananActivity.this, ""+"Terjadi kesalahan", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(PemesananActivity.this, ""+"Data tidak ditemukan", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderModel> call, Throwable t) {

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
        ApiService.service_get.getPatientByUser("Bearer "+ pref.getString("token", "null"), id_user).enqueue(new Callback<GetPatientModel>() {
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
                    Toast.makeText(PemesananActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetPatientModel> call, Throwable t) {

            }
        });

        //GET alamat untuk spinner
        ApiService.service_get.getAddressByUser("Bearer "+ pref.getString("token", "null"), id_user).enqueue(new Callback<GetAddressModel>() {
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
                    Toast.makeText(PemesananActivity.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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
