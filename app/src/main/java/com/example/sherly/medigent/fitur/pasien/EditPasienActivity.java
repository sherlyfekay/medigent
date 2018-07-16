package com.example.sherly.medigent.fitur.pasien;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.sherly.medigent.model.DeleteModel;
import com.example.sherly.medigent.model.EditModel;
import com.example.sherly.medigent.model.pasien.PatientModel;
import com.example.sherly.medigent.model.pasien.PostPatientModel;
import com.example.sherly.medigent.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasienActivity extends AppCompatActivity {

    EditText etNamaPasien, etTglLahir, etBB, etTB, etHubungan, etAlatMedis, etDiagnosa, etKondisiLengkap;
    RadioButton rbLk, rbPr;
    RadioGroup rgJk;
    Button btnSimpan;
    String selectedJk;
    Calendar mCalendar, today;
    String id_user, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pasien);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Edit Pasien");

        Intent intent = getIntent();
        final String id_patient = intent.getStringExtra("id_patient");

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
        id_user = pref.getString("id_user", "null");
        token = pref.getString("token", "null");

        ApiService.service_get.getDetailPatient("Bearer "+token, id_patient).enqueue(new Callback<PostPatientModel>() {
            @Override
            public void onResponse(Call<PostPatientModel> call, Response<PostPatientModel> response) {
                if (response.isSuccessful()) {
                    etNamaPasien.setText(response.body().getNama_lengkap());
                    etTglLahir.setText(response.body().getTgl_lahir());
                    etBB.setText(response.body().getBerat());
                    etTB.setText(response.body().getTinggi());
                    etHubungan.setText(response.body().getHubungan());
                    etAlatMedis.setText(response.body().getAlat());
                    etDiagnosa.setText(response.body().getDiagnosa());
                    etKondisiLengkap.setText(response.body().getKondisi());
                    if(response.body().getJk().equals("Perempuan")) {
                        rbPr.setChecked(true);
                    }
                    else {
                        rbLk.setChecked(true);
                    }
                }
                else {
                    Toast.makeText(EditPasienActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostPatientModel> call, Throwable t) {

            }
        });

        etTglLahir.setFocusable(false);
        etTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(EditPasienActivity.this, new DatePickerDialog.OnDateSetListener() {
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


                if (rbLk.isChecked()) {
                    selectedJk = rbLk.getText().toString();
                    //Toast.makeText(TambahPasienActivity.this, ""+selectedJk, Toast.LENGTH_SHORT).show();
                }
                else if(rbPr.isChecked()) {
                    selectedJk = rbPr.getText().toString();
                    //Toast.makeText(TambahPasienActivity.this, ""+selectedJk, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditPasienActivity.this, "Mohon lengkapi data diri pasien dengan benar", Toast.LENGTH_SHORT).show();
                }

                if(nama_lengkap.isEmpty() || tgl_lahir.isEmpty() || berat.isEmpty() || tinggi.isEmpty() || hubungan.isEmpty() || alat.isEmpty() || diagnosa.isEmpty() || kondisi.isEmpty() || id_user.equals("null")){
                    Toast.makeText(EditPasienActivity.this, "Mohon lengkapi data diri pasien dengan benar", Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayList<EditModel> editModels = new ArrayList<>();
                    editModels.add(new EditModel("nama_lengkap", nama_lengkap));
                    editModels.add(new EditModel("jk", selectedJk));
                    editModels.add(new EditModel("tgl_lahir", tgl_lahir));
                    editModels.add(new EditModel("berat", berat));
                    editModels.add(new EditModel("tinggi", tinggi));
                    editModels.add(new EditModel("hubungan", hubungan));
                    editModels.add(new EditModel("alat", alat));
                    editModels.add(new EditModel("diagnosa", diagnosa));
                    editModels.add(new EditModel("kondisi", kondisi));

                    ApiService.service_patch.patchPatient("Bearer "+token, id_patient, editModels).enqueue(new Callback<DeleteModel>() {
                        @Override
                        public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(EditPasienActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(EditPasienActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<DeleteModel> call, Throwable t) {

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
