package com.example.sherly.medigent.fitur.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.login.LoginActivity;
import com.example.sherly.medigent.model.login.LoginModel;
import com.example.sherly.medigent.model.register.PostRegisterModel;
import com.example.sherly.medigent.model.register.RegisterModel;
import com.example.sherly.medigent.service.ApiService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    TextView tvLogin;
    EditText etNamaLengkap, etEmail, etTelepon, etPassword, etKonfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvLogin = (TextView) findViewById(R.id.tvLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        etNamaLengkap = (EditText) findViewById(R.id.etNamaLengkap);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etTelepon = (EditText) findViewById(R.id.etTelepon);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etKonfPassword = (EditText) findViewById(R.id.etKonfPassword);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_lengkap = etNamaLengkap.getText().toString();
                String email = etEmail.getText().toString();
                String telepon = etTelepon.getText().toString();
                String password = etPassword.getText().toString();
                String konfPassword = etKonfPassword.getText().toString();

                if(nama_lengkap.isEmpty() || email.isEmpty() || telepon.isEmpty() || password.isEmpty() || konfPassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Mohon lengkapi form daftar Anda. Terima kasih", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!password.equals(konfPassword)){
                        Toast.makeText(RegisterActivity.this, "Kata Sandi yang Anda masukkan tidak cocok", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        PostRegisterModel postRegisterModel = new PostRegisterModel(nama_lengkap, email, telepon, password);

                        ApiService.service_post.postSignup(postRegisterModel).enqueue(new Callback<RegisterModel>() {
                            @Override
                            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {

                                if(response.isSuccessful()) {
                                    String status = response.body().getStatus();

                                    if(status.equals("100")) {
                                        Toast.makeText(RegisterActivity.this, "Anda berhasil membuat akun baru", Toast.LENGTH_LONG).show();
                                        Toast.makeText(RegisterActivity.this, ""+response.body().getId_new_user(), Toast.LENGTH_LONG).show();

                                        Intent masuk = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(masuk);
                                    }
                                    else if (status.equals("101")) {
                                        Toast.makeText(RegisterActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<RegisterModel> call, Throwable t) {
                                Toast.makeText(RegisterActivity.this, "Cek sambungan internet Anda", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
