package com.example.nutritionapp;

import com.example.nutritionapp.model.MyFireStoreHandler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "RegisterActivity";

    EditText fullName, email, password, phone;
    Button registerBtn;
    TextView loginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    String todayStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullName = findViewById(R.id.FullName);
        phone = findViewById(R.id.Phone);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        registerBtn = findViewById(R.id.RegisterBtn);
        loginBtn = findViewById(R.id.LoginBtn);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance(); // This is Singleton design pattern
        // fAuth = FirebaseAuth.getInstance(); // This call will return the same object

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (TextUtils.isEmpty(phone.getText().toString().trim())) {
                    phone.setError("Phone is Required");
                    return;
                }

                if (TextUtils.isEmpty(Email)) {
                    email.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Password must be at least 6 characters");
                    return;
                }

                // progressBar.setVisibility(View.VISIBLE);

                // Register the user in firebase
                fAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object>
                                    user = new HashMap<>();
                            // user.put("birthdate", new Date());
                            user.put("fullName", fullName.getText().toString());
                            // user.put("gender", 1);
                            user.put("phone", phone.getText().toString());
                            user.put("paid", false);
                            Date today = new Date();
                            todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
                            user.put("registeredAt", todayStr);

                            MyFireStoreHandler.createUserData(user, Email, new MyFireStoreHandler.RequestCallback() {
                                @Override
                                public void OnResponse(Boolean success, String message) {

                                    if (success) {
                                        Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("email", Email);
                                        myEdit.putString("fullName", fullName.getText().toString());
                                        myEdit.putString("phone", phone.getText().toString());
                                        myEdit.putString("registeredAt", todayStr);
                                        myEdit.putBoolean("paid", false);
                                        myEdit.commit();

                                        startActivity(new Intent(getApplicationContext(), MenuScreenActivity.class));
                                    } else {
                                        showAlert(message);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    public void showAlert(String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
                // set title
                .setTitle(title)
                // set message
                // .setMessage("Exiting will call finish() method")
                // set positive button
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked

                    }
                })
                // set negative button
                .show();
    }
}