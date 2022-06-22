package com.example.nutritionapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.nutritionapp.model.RecipeItem;
import com.example.nutritionapp.model.MyFireStoreHandler;

public class AddRecipeActivity extends AppCompatActivity {

    public static final int GALLERY_PERMISSION_REQUEST_CODE = 100;
    public static final int GALLERY_IMAGE_PICK_REQUEST_CODE = 101;

    EditText nameEt,instructionsEt,typeEt;
    Button submitButton;
    ImageView selectedImageView;
    TextView clickToSelect;
    Uri selectedImageUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Saves all data from activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        nameEt = findViewById(R.id.recipeNameEt);
        instructionsEt = findViewById(R.id.recipeInsEt);
        typeEt = findViewById(R.id.recipeTypeEt);
        selectedImageView = findViewById(R.id.recipeIv);
        submitButton = findViewById(R.id.submitRecipeBtn);
        clickToSelect = findViewById(R.id.clickToSelect);

        // first, check if the user has permissions to gallery,
        // if yes saves permission to the gallery, if not, ask the user for permissions

        View.OnClickListener openImageSelectorListener = v -> {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_PERMISSION_REQUEST_CODE);
            }else {
                // Save all type of only image
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,GALLERY_IMAGE_PICK_REQUEST_CODE);
            }
        };

        clickToSelect.setOnClickListener(openImageSelectorListener);
        selectedImageView.setOnClickListener(openImageSelectorListener);


        // Calls functions validate fields

        submitButton.setOnClickListener(v -> {
            if(isValidFields()) {
                MyFireStoreHandler.addNewRecipe(new RecipeItem(null,
                        nameEt.getText().toString(),
                        instructionsEt.getText().toString(),
                        typeEt.getText().toString(),
                        null), selectedImageUri, (success, message) -> {
                             if(success) {
                                 Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                 finish();
                             }else {
                                 Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                             }
                        });
            }else {
                Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Check if the fields from activity_add_recipe are empties(not image)
    // If so the user must put all inputs

    public boolean isValidFields() {
        if(typeEt.getText().toString().isEmpty()) {
            typeEt.requestFocus();
            return false;
        }
        if(nameEt.getText().toString().isEmpty()) {
            nameEt.requestFocus();
            return false;
        }
        if(instructionsEt.getText().toString().isEmpty()) {
            instructionsEt.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            Toast.makeText(this,"Permissions changed ",Toast.LENGTH_SHORT).show();
        }else if(requestCode == GALLERY_IMAGE_PICK_REQUEST_CODE) {
            if(data!=null) {
                 selectedImageUri = data.getData();
                 selectedImageView.setImageURI(selectedImageUri);
            }
        }
    }
}
