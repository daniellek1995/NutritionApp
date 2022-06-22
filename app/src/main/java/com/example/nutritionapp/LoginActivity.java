package com.example.nutritionapp;

import com.example.nutritionapp.model.MyFireStoreHandler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutritionapp.model.App;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";

    EditText etEmail,etPassword;
    Button btnLoginBtn;
    TextView createBtn;
    FirebaseAuth fAuth;
    TextView tvLoginStatus;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);
        createBtn = findViewById(R.id.createText);
        btnLoginBtn = findViewById(R.id.LoginBtn);
        tvLoginStatus = findViewById(R.id.tvLoginStatus);
        progressBar = findViewById(R.id.progressBar);

        setView();
    }

    public void setView() {

        fAuth = FirebaseAuth.getInstance();

        btnLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim().toLowerCase();
                String Password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    etPassword.setError("Password is Required");
                    return;
                }

                if (etPassword.length() < 6) {
                    etPassword.setError("Password must be at least 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate the user
                fAuth.signInWithEmailAndPassword(email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    MyFireStoreHandler.getUserData(email, new MyFireStoreHandler.RequestDataCallback() {
                                        @Override
                                        public void OnResponse(Boolean success, Map<String, Object> userData) {
                                            Log.d(TAG, "in OnResponse: email is: " + email);

                                            if (success) {
                                                Log.d(TAG, "in OnResponse-success: email is: " + email);
                                                Toast.makeText(LoginActivity.this, " are logged in.", Toast.LENGTH_SHORT).show();

                                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                Log.d(TAG, "email: " + email);
                                                Log.d(TAG, "full name: " + (String)userData.get("fullName"));

                                                myEdit.putString("email", email);
                                                myEdit.putString("fullName", (String)userData.get("fullName"));
                                                myEdit.putString("registeredAt", (String)userData.get("registeredAt"));
                                                //UserDetailsSingleton.getInstance().setFullName((String)userData.get("fullName"));
                                                myEdit.commit();
                                                //progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(getApplicationContext(), MenuScreenActivity.class));
                                            }
                                            else {
                                                Log.d(TAG, "in OnResponse-success: Invalid login. email is: " + email);
                                                // Toast.makeText(LoginActivity.this, "Sorry, invalid Login!", Toast.LENGTH_LONG).show();
                                                tvLoginStatus.setTextColor(Color.RED);
                                                tvLoginStatus.setText("Sorry, invalid login!");
                                            }
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });

                                } else {
                                    Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void onLogoIconClicked(View view) {
        etEmail.setText(App.getAdminEmail());
    }
}