package com.example.sherly.medigent.fitur.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherly.medigent.R;
import com.example.sherly.medigent.fitur.register.RegisterActivity;
import com.example.sherly.medigent.fitur.home.MainActivity;
import com.example.sherly.medigent.model.login.LoginModel;
import com.example.sherly.medigent.model.login.PostLoginModel;
import com.example.sherly.medigent.service.ApiService;
import com.example.sherly.medigent.service.Session;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvRegister;
    Button btnLogin;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);

        //final SharedPreferences pref = getSharedPreferences("medigent", MODE_PRIVATE);
        //final SharedPreferences.Editor edit = pref.edit();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if(session.loggedin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data_email = etEmail.getText().toString();
                String data_password = etPassword.getText().toString();

                if(data_email.isEmpty() || data_password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Mohon lengkapi form login Anda. Terima kasih", Toast.LENGTH_SHORT).show();
                }
                else {

                    PostLoginModel postLoginModel = new PostLoginModel(data_email, data_password);

                    ApiService.service_post.postLogin(postLoginModel).enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                            if(response.isSuccessful()) {
                                String status = response.body().getStatus();

                                if(status.equals("100")) {
                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

    //                                DatabaseService db = new DatabaseService(getApplicationContext());
    //                                db.setMember(response.body().getData());
                                    String id_user = response.body().getId_user();
                                    String token = response.body().getToken();

                                    session.setLoggedin(true, token, id_user);

                                    //edit.putString("token", token);
                                    //edit.putString("id_user", id_user);
                                    //edit.commit();

                                    //Toast.makeText(LoginActivity.this, ""+pref.getString("token", "null"), Toast.LENGTH_LONG).show();
                                    //Toast.makeText(LoginActivity.this, ""+pref.getString("id_user", "null"), Toast.LENGTH_LONG).show();

                                    Intent masuk = new Intent(LoginActivity.this, MainActivity.class);
                                    //masuk.putExtra("id_user", id_user);
                                    startActivity(masuk);
                                    finish();
                                }
                                else if (status.equals("101")) {
                                    Toast.makeText(LoginActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }

                                else if (status.equals("102")) {
                                    Toast.makeText(LoginActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Cek sambungan internet Anda", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
