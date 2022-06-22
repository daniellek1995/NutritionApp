package com.example.nutritionapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.MyFireStoreHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class UploadPictureActivity extends AppCompatActivity {

    private final String TAG = "UploadPictureActivity";

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_FROM_GALLERY = 101;

    ImageView ivChosenPic;
    TextView tvUploadPicStatus;

    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> activityResultLauncherForGallery;

    String email;
    String fullName;
    String chatOwnerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_picture);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        fullName = sharedPreferences.getString("fullName", "guest");

        Intent intent = getIntent();
        chatOwnerEmail = intent.getExtras().getString("email of chat owner");

        ivChosenPic = findViewById(R.id.ivChosenPic);
        tvUploadPicStatus = findViewById(R.id.tvUploadPicStatus);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            ivChosenPic.setImageBitmap(imageBitmap);
                        }
                    }
                }
        );

        activityResultLauncherForGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Uri imageUri = data.getData();
                            ivChosenPic.setImageURI(imageUri);
                        }
                    }
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onBtnCameraClicked(View view) {

        Toast.makeText(UploadPictureActivity.this, "Using camera",
                    Toast.LENGTH_LONG).show();

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(takePictureIntent);
//        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePicIntent, MY_CAMERA_REQUEST_CODE);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(ChoosePictureActivity.this, "Failed to start camera",
//                    Toast.LENGTH_LONG).show();
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == MY_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ivChosenPic.setImageBitmap(imageBitmap);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void onBtnUploadDisplayedPicClicked(View view) {

        String picNameInFirebaseStorage = UUID.randomUUID().toString() + ".jpg";
        MyFireStorageHandler.uploadPicture(picNameInFirebaseStorage, ivChosenPic, new MyFireStorageHandler.RequestCallback() {
            @Override
            public void OnResponse(MyFireStorageHandler.EnumStatus status, String message, double progress) {
                if (status == MyFireStorageHandler.EnumStatus.IN_PROGRESS) {
                    tvUploadPicStatus.setTextColor(Color.BLACK);
                    tvUploadPicStatus.setText("Uploading " + progress + "% done");
                }
                else if (status == MyFireStorageHandler.EnumStatus.SUCCESS) {
                    tvUploadPicStatus.setTextColor(Color.GREEN);
                    tvUploadPicStatus.setText("Picture uploaded");

                    Toast.makeText(UploadPictureActivity.this, "writing pic name to chat of: " + chatOwnerEmail, Toast.LENGTH_SHORT).show();
                    Date now = Calendar.getInstance().getTime();
                    String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
                    MyFireStoreHandler.addPictureNameToChat(chatOwnerEmail, nowStr, fullName, picNameInFirebaseStorage,
                            (success, message2) -> {
                                if (success) {
                                    Toast.makeText(UploadPictureActivity.this, "pic name written to chat", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UploadPictureActivity.this, "Failed to write picture name in chat", Toast.LENGTH_SHORT).show();
                                }
                            } );
                }
                else if (status == MyFireStorageHandler.EnumStatus.FAILED) {
                    Toast.makeText(UploadPictureActivity.this,"Failed to upload picture",
                                     Toast.LENGTH_LONG).show();
                    tvUploadPicStatus.setTextColor(Color.RED);
                    tvUploadPicStatus.setText("Failed to upload picture");
                }
            }
        });

//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//
//        //String picNameInFirebaseStorage = UUID.randomUUID().toString() + ".jpg";
//
//        // Create a reference to 'images/picture_name.jpg'
//        StorageReference imageRef = storageRef.child("images/" + picNameInFirebaseStorage);
//
//        ivChosenPic.setDrawingCacheEnabled(true);
//        ivChosenPic.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) ivChosenPic.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imageRef.putBytes(data);
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                tvUploadPicStatus.setTextColor(Color.BLACK);
//                tvUploadPicStatus.setText("Uploading " + progress + "% done");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//               //Toast.makeText(ChoosePictureActivity.this,"Failed to upload picture",
//               //                 Toast.LENGTH_LONG).show();
//                tvUploadPicStatus.setTextColor(Color.RED);
//                tvUploadPicStatus.setText("Failed to upload picture");
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
////                Toast.makeText(ChoosePictureActivity.this,
////                        "Picture uploaded",
////                        Toast.LENGTH_LONG).show();
//                tvUploadPicStatus.setTextColor(Color.GREEN);
//                tvUploadPicStatus.setText("Picture uploaded");
//
//                Toast.makeText(UploadPictureActivity.this, "writing pic name to chat of: " + chatOwnerEmail, Toast.LENGTH_SHORT).show();
//                Date now = Calendar.getInstance().getTime();
//                String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
//                MyFireStoreHandler.addPictureNameToChat(chatOwnerEmail, nowStr, fullName, picNameInFirebaseStorage,
//                        (success, message) -> {
//                            if (success) {
//                                Toast.makeText(UploadPictureActivity.this, "pic name written to chat", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(UploadPictureActivity.this, "Failed to write picture name in chat", Toast.LENGTH_SHORT).show();
//                            }
//                        } );
//            }
//        });
    }

    public void onBtnGalleryClicked(View view) {
//         Intent gallery = new Intent(Intent.ACTION_PICK,
//                                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//         startActivityForResult(gallery, PICK_IMAGE_FROM_GALLERY);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncherForGallery.launch(galleryIntent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
//            Uri imageUri = data.getData();
//            ivChosenPic.setImageURI(imageUri);
//        }
//    }

}
