package com.example.nutritionapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.MyFireStoreHandler;
import com.example.nutritionapp.viewModel.StatusViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class StatusFragment extends Fragment {

    private StatusViewModel mViewModel;

    private final String TAG = "StatusFragment";

    private String email;
    private TextView tvFullName;
    private EditText etUserWeight;
    private TextView tvStatusSaveWeight;
    private TextView tvStatusFatPercentage;
    private EditText etStatusFatPercentage;
    private Button btnSaveWeight;
    private Button btnShowProgress;

    String progressOwnerEmail;
    String progressOwnerFullname;

    boolean isValidInput = false;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        View view = inflater.inflate(R.layout.status_fragment, container, false);

        // Using Safe Args to get data from the caller menuFragment
        progressOwnerEmail = getArguments().getString("email of progress owner");
        progressOwnerFullname = getArguments().getString("fullname of progress owner");


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        tvFullName = view.findViewById(R.id.tvStatusFullName3);
        tvStatusSaveWeight = view.findViewById(R.id.tvStatusSaveWeight3);
        etUserWeight = view.findViewById(R.id.etUserWeight3);
        tvStatusFatPercentage = view.findViewById(R.id.tvEnterFatPercentage3);
        etStatusFatPercentage = view.findViewById(R.id.etEnterFatPercentage3);
        btnSaveWeight = view.findViewById(R.id.btnSaveWeight3);
        btnShowProgress = view.findViewById(R.id.btnShowProgress3);

        if (email.equals(progressOwnerEmail)) {
            tvStatusFatPercentage.setVisibility(View.GONE);
            etStatusFatPercentage.setVisibility(View.GONE);
        }

        tvFullName.setText(progressOwnerFullname);
        btnSaveWeight.setEnabled(false);


        btnSaveWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnSaveWeightClicked3(v);
            }
        });

        btnShowProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnShowProgressClicked(v);
            }
        });

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

        return view;
    }

    public void onBtnSaveWeightClicked3(View view) {
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
        Intent intent = new Intent(getActivity(), ShowWeightProgressActivity.class);
        intent.putExtra("email of progress owner", progressOwnerEmail);
        intent.putExtra("fullname of progress owner", progressOwnerFullname);
        startActivity(intent);
    }
}