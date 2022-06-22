package com.example.nutritionapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.RecipeItem;
import com.google.gson.Gson;

public class RecipeDetailsActivity extends AppCompatActivity {

    private final String TAG = "RecipeDetailsActivity";

    TextView type,name,ins,headline;
    Button back;
    ImageView recipeImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        type = findViewById(R.id.recipeTypeTv);
        ins = findViewById(R.id.recipeInsTv);
        recipeImage = findViewById(R.id.recipeDetailsIv);
        headline = findViewById(R.id.recipeCardHeadline);
        back = findViewById(R.id.backButtonRecipeDetails);
        back.setOnClickListener((v) -> finish());

        if(getIntent()!=null) {
            Gson g = new Gson();

            RecipeItem recipe = g.fromJson(getIntent().getStringExtra("recipe"),  RecipeItem.class);
            type.setText(recipe.getType());
            ins.setText(recipe.getInstructions());
            headline.setText(recipe.getName());
            if(!recipe.getImage().equals("none")) {
                String pictureInChatMsg = "images/recipes/" + recipe.getImage();
                MyFireStorageHandler.downloadPictureIntoImageview(
                        RecipeDetailsActivity.this, pictureInChatMsg,
                        this.recipeImage, new MyFireStorageHandler.RequestCallback() {
                            @Override
                            public void OnResponse(MyFireStorageHandler.EnumStatus status, String message, double uploadProgress) {
                                if (status == MyFireStorageHandler.EnumStatus.SUCCESS) {
                                    Log.d(TAG, "Pic downloaded. pic: " + pictureInChatMsg);
                                } else {
                                    Log.e(TAG, "Failed to download pic. pic: " + pictureInChatMsg);
                                }
                            }
                        });
            }
        }
    }
}
