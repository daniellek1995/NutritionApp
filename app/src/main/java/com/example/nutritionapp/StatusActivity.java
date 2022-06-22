package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutritionapp.model.MyFireStoreHandler;

import java.util.Date;

public class StatusActivity extends AppCompatActivity {

    private final String TAG = "StatusActivity";

    private String email;
    private TextView tvFullName;
    private EditText etUserWeight;
    private TextView tvStatusSaveWeight;
    private TextView tvStatusFatPercentage;
    private EditText etStatusFatPercentage;
    private Button btnSaveWeight;

    String progressOwnerEmail;
    String progressOwnerFullname;

    boolean isValidInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Intent intent = getIntent();
        progressOwnerEmail = intent.getExtras().getString("email of progress owner");
        progressOwnerFullname = intent.getExtras().getString("fullname of progress owner");

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        tvFullName = findViewById(R.id.tvStatusFullName);
        tvStatusSaveWeight = findViewById(R.id.tvStatusSaveWeight);
        etUserWeight = findViewById(R.id.etUserWeight);
        tvStatusFatPercentage = findViewById(R.id.tvEnterFatPercentage);
        etStatusFatPercentage = findViewById(R.id.etEnterFatPercentage);
        btnSaveWeight = findViewById(R.id.btnSaveWeight);

        if (email.equals(progressOwnerEmail)) {
            tvStatusFatPercentage.setVisibility(View.GONE);
            etStatusFatPercentage.setVisibility(View.GONE);
        }

        tvFullName.setText(progressOwnerFullname);
        btnSaveWeight.setEnabled(false);

        TextWatcher textWatcher =
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        tvStatusSaveWeight.setText("");
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String weightStr = etUserWeight.getText().toString();
                        String fatPercentageStr = etStatusFatPercentage.getText().toString();

                        if (weightStr.trim().length() == 0 && fatPercentageStr.length() == 0)
                        {
                            tvStatusSaveWeight.setText("");
                            btnSaveWeight.setEnabled(false);
                            return;
                        }

                        if (weightStr.trim().length() > 0) {
                            float weight = Float.valueOf(weightStr);
                            if (weight < 30.0f || weight > 250) {
                                tvStatusSaveWeight.setTextColor(Color.RED);
                                tvStatusSaveWeight.setText("משקל חייב ליהיות בטווח 250..30");
                                btnSaveWeight.setEnabled(false);
                                return;
                            }
                        }

                        if (fatPercentageStr.trim().length()>0) {
                            float fatPercentage = Float.valueOf(fatPercentageStr);
                            if (fatPercentage < 6.0f || fatPercentage > 50) {
                                tvStatusSaveWeight.setTextColor(Color.RED);
                                tvStatusSaveWeight.setText("אחוז שומן חייב ליהיות בטווח 50..6");
                                btnSaveWeight.setEnabled(false);
                                return;
                            }
                        }

                        if (weightStr.trim().length() == 0 && fatPercentageStr.length() > 0)
                        {
                            tvStatusSaveWeight.setTextColor(Color.rgb(255, 127, 80));
                            tvStatusSaveWeight.setText("נא להקליד גם משקל");
                            btnSaveWeight.setEnabled(false);
                            return;
                        }

                        btnSaveWeight.setEnabled(true);
                        return;
                    }
                };
        etUserWeight.addTextChangedListener(textWatcher);
        etStatusFatPercentage.addTextChangedListener(textWatcher);
    }

    public void onBtnSaveWeightClicked(View view) {
        Date date = new Date();

        float weight = Float.parseFloat(etUserWeight.getText().toString());
        MyFireStoreHandler.addUserWeight(progressOwnerEmail, date, weight, new MyFireStoreHandler.RequestCallback() {
            @Override
            public void OnResponse(Boolean success, String message) {
                if (success) {
                    tvStatusSaveWeight.setTextColor(Color.GREEN);
                    tvStatusSaveWeight.setText("המשקל נשמר בהצלחה!");
                } else {
                    tvStatusSaveWeight.setTextColor(Color.RED);
                    tvStatusSaveWeight.setText("שמירת המשקל נכשלה!");
                }
            }
        });

        String fatPercentageStr = etStatusFatPercentage.getText().toString();
        if (fatPercentageStr.trim().length()==0) {
            return;
        }
        float fatPercentage = Float.valueOf(etStatusFatPercentage.getText().toString());
        MyFireStoreHandler.addUserFatPercentage(progressOwnerEmail, date, fatPercentage, new MyFireStoreHandler.RequestCallback() {
            @Override
            public void OnResponse(Boolean success, String message) {
                if (success) {
                    tvStatusSaveWeight.setTextColor(Color.GREEN);
                    tvStatusSaveWeight.setText("המשקל נשמר בהצלחה!");
                } else {
                    tvStatusSaveWeight.setTextColor(Color.RED);
                    tvStatusSaveWeight.setText("שמירת המשקל נכשלה!");
                }
            }
        });

    }

    public void onBtnShowProgressClicked(View view) {
        Intent intent = new Intent(StatusActivity.this, ShowWeightProgressActivity.class);
        intent.putExtra("email of progress owner", progressOwnerEmail);
        intent.putExtra("fullname of progress owner", progressOwnerFullname);
        startActivity(intent);
    }
}
