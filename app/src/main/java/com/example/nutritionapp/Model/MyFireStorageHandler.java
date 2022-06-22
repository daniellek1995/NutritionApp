package com.example.nutritionapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.nutritionapp.GlideApp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MyFireStorageHandler {

    final private static String TAG = "MyFireStoreHandler";

    public enum EnumStatus {SUCCESS, FAILED, IN_PROGRESS};

    public interface RequestCallback {
        void OnResponse(EnumStatus status, String message, double uploadProgress);
    }

    public static void uploadPicture(String pictureNameInFirestorage, ImageView sourceImageView, RequestCallback callback) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'images/picture_name.jpg'
        StorageReference imageRef = storageRef.child("images/chats/" + pictureNameInFirestorage);

        sourceImageView.setDrawingCacheEnabled(true);
        sourceImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) sourceImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                callback.OnResponse(EnumStatus.IN_PROGRESS, "progress", progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                callback.OnResponse(EnumStatus.FAILED, "failed", -1);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                callback.OnResponse(EnumStatus.SUCCESS, "failed", -1);
            }
        });

    }

    // pictureInFirestorage should be like: "images/chats/mypic.jpeg"
    // or like: ïmages/recipes/mypic2.jpg"
    public static void downloadPictureIntoImageview(Context context,
                                    String pictureInFirestorage,
                                    ImageView imageView, RequestCallback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference picRef = storageRef.child(pictureInFirestorage);

        Log.d("firebasePic", "pic name: " + pictureInFirestorage);

        GlideApp.with(context)
                .load(picRef)
                .into(imageView);

        callback.OnResponse(EnumStatus.SUCCESS, "success", -1);
    }

    public static void deletePicture(String pictureNameInFirestorage, RequestCallback callback) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'images/picture_name.jpg'
        StorageReference imageRef = storageRef.child("images/chats/" + pictureNameInFirestorage);

        imageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.OnResponse(EnumStatus.SUCCESS, "success", -1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.OnResponse(EnumStatus.FAILED, "failed", -1);
                    }
                });
    }
}
