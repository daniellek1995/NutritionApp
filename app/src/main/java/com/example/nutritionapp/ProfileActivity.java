package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.nutritionapp.model.MyFireStoreHandler;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "ProfileActivity";

    TextView tvProfileFullName;
    TextView tvProfileActiveSince;
    TextView tvProfileUserPaid;

    String email;
    String fullName;
    String registeredAtDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        fullName = sharedPreferences.getString("fullName", "no name");
        registeredAtDateStr = sharedPreferences.getString("registeredAt", "1900-01-01");

        tvProfileFullName = findViewById(R.id.tvProfileFullName);
        tvProfileFullName.setText(fullName);

        tvProfileActiveSince = findViewById(R.id.tvProfileActiveSince);
        tvProfileActiveSince.setText( " פעיל/ה מתאריך " + registeredAtDateStr);

        tvProfileUserPaid = findViewById(R.id.tvProfileUserPaid);
        tvProfileUserPaid.setText("כרגע אין מידע");

        MyFireStoreHandler.getUserData(email, new MyFireStoreHandler.RequestDataCallback() {
                    @Override
                    public void OnResponse(Boolean success, Map<String, Object> userData) {
                        Log.d(TAG, "in OnResponse: email is: " + email);

                        if (success) {
                            Log.d("LoginActivity", "in OnResponse-success: email is: " + email);
                            if (userData.get("paid") == null) {
                                tvProfileUserPaid.setTextColor(Color.RED);
                                tvProfileUserPaid.setText("מצב תשלום: בדיקת מצב תשלום נכשלה!");
                                return;
                            }
                            boolean userPaid = (boolean)userData.get("paid");
                            if (userPaid) {
                                tvProfileUserPaid.setTextColor(Color.GREEN);
                                tvProfileUserPaid.setText("מצב תשלום: שולם");
                            }
                            else {
                                tvProfileUserPaid.setTextColor(Color.rgb(255, 127, 80));
                                tvProfileUserPaid.setText("מצב תשלום: לא שולם");
                            }
                        } else {
                            Log.d("LoginActivity", "in OnResponse-success: Failed to check if user paid. email is: " + email);
                            tvProfileUserPaid.setTextColor(Color.RED);
                            tvProfileUserPaid.setText("מצב תשלום: בדיקת מצב תשלום נכשלה!");
                        }
                    }
             });
    }
}