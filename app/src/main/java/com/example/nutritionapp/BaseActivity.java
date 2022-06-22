package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    public final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("email", "danitshabtay@gmail.com");
        myEdit.putString("fullName", "Danit Gabay");
        myEdit.putString("registeredAt", "2021-05-03");
        myEdit.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return  Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    public void onBtnNavigateClicked(View view) {
        Log.d("navv", "navigating now");
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navHostFragment.getNavController().navigate(R.id.action_menuFragment_to_profileFragment);
    }
}