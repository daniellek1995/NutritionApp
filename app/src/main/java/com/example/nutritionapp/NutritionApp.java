package com.example.nutritionapp;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class NutritionApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
