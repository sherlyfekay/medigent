package com.example.sherly.medigent.fitur.pasien;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.model.pasien.PatientModel;
import com.example.sherly.medigent.model.pasien.PostPatientModel;
import com.example.sherly.medigent.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPasienActivity extends AppCompatActivity {

    EditText etNamaPasien, etTglLahir, etBB, etTB, etHubungan, etAlatMedis, etDiagnosa, etKondisiLengkap;
    RadioButton rbLk, rbPr;
    RadioGroup rgJk;
    Button btnSimpan;
    String selectedJk;
    Calendar mCalendar, today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pasien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tambah Pasien");

        final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);

        etNamaPasien = (EditText) findViewById(R.id.etNamaPasien);
        etTglLahir = (EditText) findViewById(R.id.etTglLahir);
        etBB = (EditText) findViewById(R.id.etBB);
        etTB = (EditText) findViewById(R.id.etTB);
        etHubungan = (EditText) findViewById(R.id.etHubungan);
        etAlatMedis = (EditText) findViewById(R.id.etAlatMedis);
        etDiagnosa = (EditText) findViewById(R.id.etDiagnosa);
        etKondisiLengkap = (EditText) findViewById(R.id.etKondisiLengkap);
        rbLk = (RadioButton) findViewById(R.id.rbLk);
        rbPr = (RadioButton) findViewById(R.id.rbPr);
        rgJk = (RadioGroup) findViewById(R.id.rgJk);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        mCalendar = Calendar.getInstance();
        today = Calendar.getInstance();

        etTglLahir.setFocusable(false);
        etTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TambahPasienActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //datePicker.setMinDate(mCalendar.getTime()-(mCalendar.getTime()%(24*60*60*1000)));
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, day);

                        String dateFormat = "dd-MM-yyyy";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

                        etTglLahir.setText(simpleDateFormat.format(mCalendar.getTime()));
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_lengkap = etNamaPasien.getText().toString();
                String tgl_lahir = etTglLahir.getText().toString();
                String berat = etBB.getText().toString();
                String tinggi = etTB.getText().toString();
                String hubungan = etHubungan.getText().toString();
                String alat = etAlatMedis.getText().toString();
                String diagnosa = etDiagnosa.getText().toString();
                String kondisi = etKondisiLengkap.getText().toString();
                String id_user = pref.getString("id_user", "null");
                String token = pref.getString("token", "null");

                if (rbLk.isChecked()) {
                    selectedJk = rbLk.getText().toString();
                    //Toast.makeText(TambahPasienActivity.this, ""+selectedJk, Toast.LENGTH_SHORT).show();
                }
                else if(rbPr.isChecked()) {
                    selectedJk = rbPr.getText().toString();
                    //Toast.makeText(TambahPasienActivity.this, ""+selectedJk, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(TambahPasienActivity.this, "Mohon lengkapi data diri pasien dengan benar", Toast.LENGTH_SHORT).show();
                }

                if(nama_lengkap.isEmpty() || tgl_lahir.isEmpty() || berat.isEmpty() || tinggi.isEmpty() || hubungan.isEmpty() || alat.isEmpty() || diagnosa.isEmpty() || kondisi.isEmpty() || id_user.equals("null")){
                    Toast.makeText(TambahPasienActivity.this, "Mohon lengkapi data diri pasien dengan benar", Toast.LENGTH_SHORT).show();
                }
                else {

                    PostPatientModel postPatientModel = new PostPatientModel(nama_lengkap, selectedJk, tgl_lahir, berat, tinggi, hubungan, alat, diagnosa, kondisi, id_user);
                    //Toast.makeText(TambahPasienActivity.this, ""+id_user, Toast.LENGTH_SHORT).show();

                    ApiService.service_post.postPatient("Bearer "+token, postPatientModel).enqueue(new Callback<PatientModel>() {
                        @Override
                        public void onResponse(Call<PatientModel> call, Response<PatientModel> response) {

                            if(response.isSuccessful()) {
                                String status = response.body().getStatus();
                                //Toast.makeText(TambahPasienActivity.this, ""+status, Toast.LENGTH_LONG).show();

                                if(status.equals("100")) {
                                    Toast.makeText(TambahPasienActivity.this, "Pasien baru telah ditambahkan", Toast.LENGTH_LONG).show();
                                    //Toast.makeText(TambahPasienActivity.this, ""+response.body().getId_patient(), Toast.LENGTH_LONG).show();
                                    finish();
                                    //Intent masuk = new Intent(RegisterActivity.this, LoginActivity.class);
                                    //startActivity(masuk);
                                }
                                else {
                                    Toast.makeText(TambahPasienActivity.this, ""+"Terjadi kesalahan", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(TambahPasienActivity.this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<PatientModel> call, Throwable t) {
                            Toast.makeText(TambahPasienActivity.this, "Cek sambungan internet Anda", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
