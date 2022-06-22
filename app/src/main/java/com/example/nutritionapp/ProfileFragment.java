package com.example.nutritionapp;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nutritionapp.model.MyFireStoreHandler;
import com.example.nutritionapp.viewModel.ProfileViewModel;

import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel viewModel;

    private final String TAG = "ProfileFragment";

    TextView tvProfileFullName;
    TextView tvProfileActiveSince;
    TextView tvProfileUserPaid;

    String email;
    String fullName;
    String registeredAtDateStr;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        fullName = sharedPreferences.getString("fullName", "no name");
        registeredAtDateStr = sharedPreferences.getString("registeredAt", "1900-01-01");

        tvProfileFullName = getView().findViewById(R.id.tvProfileFullName);
        tvProfileFullName.setText(fullName);

        tvProfileActiveSince = getView().findViewById(R.id.tvProfileActiveSince);
        tvProfileActiveSince.setText( " פעיל/ה מתאריך " + registeredAtDateStr);

        tvProfileUserPaid = getView().findViewById(R.id.tvProfileUserPaid);
        tvProfileUserPaid.setText("כרגע אין מידע");

        MyFireStoreHandler.getUserData(email, new MyFireStoreHandler.RequestDataCallback() {
            @Override
            public void OnResponse(Boolean success, Map<String, Object> userData) {
                Log.d(TAG, "in OnResponse: email is: " + email);

                if (success) {
                    Log.d("LoginActivity", "in OnResponse-success: email is: " + email);
                    if (userData.get("paid") == null) {
                        tvProfileUserPaid.setTextColor(Color.RED);
                        tvProfileUserPaid.setText("מצב תשלום: חסר מצב תשלום בפרופיל הזה!");
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
                    // Toast.makeText(LoginActivity.this, "Sorry, invalid Login!", Toast.LENGTH_LONG).show();
                    tvProfileUserPaid.setTextColor(Color.RED);
                    tvProfileUserPaid.setText("מצב תשלום: בדיקת מצב תשלום נכשלה!");
                }
            }
        });
    }
}