package com.example.nutritionapp.model;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.nutritionapp.model.RecipeItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MyFireStoreHandler {

    final private static String TAG = "MyFireStoreHandler";

    public interface RequestCallback {
        void OnResponse(Boolean success, String message);
    }

    public interface RequestDataCallback {
        void OnResponse(Boolean success, Map<String, Object> data);
    }

    public static void addNewRecipe(RecipeItem recipe, @Nullable Uri imageUri, RequestCallback cb) {

        if(imageUri!=null) {
            StorageReference newImageRef = FirebaseStorage.getInstance().getReference("/images/recipes/" + recipe.getName()+ "_" +recipe.getType());
            newImageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> newImageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                        recipe.setImageUrl(uri.toString());
                        DocumentReference newDocument = FirebaseFirestore.getInstance()
                                .collection("recipes").document();
                        recipe.setId(newDocument.getId());
                        newDocument.set(recipe)
                                .addOnSuccessListener(unused -> cb.OnResponse(true,"Successfully added recipe " + recipe.getName()))
                                .addOnFailureListener(e -> cb.OnResponse(false,e.getMessage()));
                    }).addOnFailureListener(e -> cb.OnResponse(false,e.getMessage()))
                            .addOnFailureListener(e ->  cb.OnResponse(false,e.getMessage())));

        }else {
            DocumentReference newDocument = FirebaseFirestore.getInstance()
                    .collection("recipes").document();
            recipe.setId(newDocument.getId());
            recipe.setImageUrl("none");
            newDocument.set(recipe)
                    .addOnSuccessListener(unused -> cb.OnResponse(true,"Successfully added recipe " + recipe.getName()))
                    .addOnFailureListener(e -> cb.OnResponse(false,e.getMessage()));
        }
    }

    public interface RecipesCallback {
        void onResponse(List<RecipeItem> recipeItems);
        void onError(Exception e);
    }

//    static ListenerRegistration registeredListener;
//    public static void getAllRecipes(RecipesCallback recipesCallback) {
//        if(registeredListener!=null)
//            return;
//        registeredListener = FirebaseFirestore.getInstance().collection("recipes")
//                .addSnapshotListener((value, error) -> {
//                    if(error!=null)
//                        recipesCallback.onError(error);
//                    List<RecipeItem> allRecipes = new ArrayList<>();
//                    assert value != null;
//                    for(DocumentSnapshot doc : value.getDocuments() ) {
//                        allRecipes.add(doc.toObject(RecipeItem.class));
//                    }
//                    recipesCallback.onResponse(allRecipes);
//                });
//    }


    public static void getAllRecipes(RecipesCallback recipesCallback) {
        FirebaseFirestore.getInstance().collection("recipes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RecipeItem> allRecipes = new ArrayList<>();
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments() ) {
                        allRecipes.add(doc.toObject(RecipeItem.class));
                    }
                    recipesCallback.onResponse(allRecipes);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed loading all contacts from Firestore", e );
                        recipesCallback.onError(e);
                    }
                });
    }

//    public static void removeRecipesListener() {
//        if(registeredListener!=null)
//            registeredListener.remove();
//    }

    public static void getAllContacts(ContactsCallback callback) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Contact> contacts = new ArrayList<>();
                    for(DocumentSnapshot contactSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Contact contact = contactSnapshot.toObject(Contact.class);
                        contact.setEmail(contactSnapshot.getId());
                        contacts.add(contact);
                    }
                    callback.onContactsReceived(contacts);
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed loading all contacts from Firestore", e );
                        callback.onContactsReceived(null);
                    }
                });
    }

    public static void createUserData(Map<String, Object> user, String email, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern
            db.collection("Users").document(email).set(user)
             .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "User " + email + " added in fireStore"  );
                    callback.OnResponse(true,null);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed creating user in fireStore", e );
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed creating user in fireStore", ex );
        }
    }

    public static void getUserData(String email, RequestDataCallback callback) {
        try{
            Log.d(TAG, "in getUserData: email is: " + email);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(email);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("fullName", documentSnapshot.getString("fullName"));
                                user.put("phone", documentSnapshot.getString("phone"));
                                user.put("registeredAt", documentSnapshot.getString("registeredAt"));
                                user.put("paid", documentSnapshot.getBoolean("paid"));
                                user.put("weights", documentSnapshot.get("weights"));
                                callback.OnResponse(true, user);
                            } else {
                                callback.OnResponse(false,null);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to get user from fireStore.", e  );
                    callback.OnResponse(false,null);
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed to get user from fireStore.", ex  );
        }
    }

    public static void updateUserPaidStatus(String email, boolean paid, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern

            db.collection("Users").document(email).update("paid", paid)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "User " + email + " paid field updated in fireStore to: " + paid  );
                            callback.OnResponse(true,null);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed updating user " + email + " paid field in fireStore", e);
                        callback.OnResponse(false,e.getMessage());
                    }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed updating user " + email + " paid field in fireStore", ex);
        }
    }

    public static void addUserWeight(String email, Date date, float weightInKilograms, RequestCallback callback){
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            db.collection("Users").document(email).update("weights." + dateStr, weightInKilograms)
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Updated user " + email + " weight in fireStore");
                    callback.OnResponse(true,null);
                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed updating user weight in fireStore", e );
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed updating user weight in fireStore", ex );
        }
    }

    public static void addUserFatPercentage(String email, Date date, float fatPercentage, RequestCallback callback){
        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            db.collection("Users").document(email).update("fatPercentage." + dateStr, fatPercentage)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Updated user " + email + " fatPercentage in fireStore");
                            callback.OnResponse(true,null);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed updating user fatPercentage in fireStore", e );
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed updating user fatPercentage in fireStore", ex );
        }
    }

    public static void addChatMsg(String chatOwnerEmail, String msgDate, String user, String msg, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern
            Map<String, Object> data = new HashMap<>();
            data.put(msgDate, user + ": " + msg);
            db.collection("chats").document(chatOwnerEmail).set(data, SetOptions.merge())
              .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "Chat message added in fireStore for user " + chatOwnerEmail  );
                        callback.OnResponse(true,null);
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Filed adding chat message in fireStore for user " + chatOwnerEmail , e );
                        callback.OnResponse(false,e.getMessage());
                    }
                });
        }
        catch (Exception ex){
            Log.d(TAG, "Filed adding chat message in fireStore for user " + chatOwnerEmail , ex );
        }
    }

    public static void deleteChatMsgByItsDate(String chatOwnerEmail, String chatDateStr, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern

            db.collection("chats").document(chatOwnerEmail).update(chatDateStr, "<< ההודעה נמחקה >>")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Chat msg deleted in chat document: " + chatOwnerEmail  );
                            callback.OnResponse(true,null);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed deleting msg in chat document: " + chatOwnerEmail ,e );
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed deleting msg in chat document: " + chatOwnerEmail, ex  );
        }
    }

    public static void updateChatMsgByItsDate(String chatOwnerEmail, String chatDateStr, String updatedMsg, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern

            db.collection("chats").document(chatOwnerEmail).update(chatDateStr, updatedMsg)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Chat msg updated in chat document: " + chatOwnerEmail  );
                            callback.OnResponse(true,null);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed updating msg in chat document: " + chatOwnerEmail, e);
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed updating msg in chat document: " + chatOwnerEmail, ex);
        }
    }

    public static void addPictureNameToChat(String chatOwnerEmail, String msgDate, String user, String pictureNameInFirebaseStorage, RequestCallback callback){

        try {
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // Singleton design pattern
            Map<String, Object> data = new HashMap<>();
            data.put(msgDate, user + ": __" + pictureNameInFirebaseStorage + "__");
            db.collection("chats").document(chatOwnerEmail).set(data, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d(TAG, "Picture name merged in chats collection into document: " + chatOwnerEmail  );
                            callback.OnResponse(true,null);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed merging picture name in chats collection into document: " + chatOwnerEmail, e);
                    callback.OnResponse(false,e.getMessage());
                }
            });
        }
        catch (Exception ex){
            Log.e(TAG, "Failed merging picture name in chats collection into document: " + chatOwnerEmail, ex);
        }
    }

    public static void continuouslyListenToChat(String chatOwnerEmail, RequestDataCallback callbackData) {

            Log.d(TAG, "in continuouslyListenToChat: email is: " + chatOwnerEmail);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("chats").document(chatOwnerEmail);

            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e(TAG, "Listening to chat failed. chat owner email is: " + chatOwnerEmail, e);
                        return;
                    }

                    String source = snapshot != null && snapshot.getMetadata().hasPendingWrites() ? "Local" : "Server";

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, source + "Listening to chat. data: " + snapshot.getData());
                        callbackData.OnResponse(true, snapshot.getData() );
                    } else {
                        Log.d(TAG, source + "\"Listening to chat. data is null");
                    }

                }

            });
        }

    public static void getChat(String chatOwnerEmail, RequestDataCallback callback) {

        Log.d(TAG, "in continuouslyListenToChat: email is: " + chatOwnerEmail);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("chats").document(chatOwnerEmail);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "Loaded chat text from fireStore"  );
                    callback.OnResponse(true, documentSnapshot.getData());
                } else {
                    Log.d(TAG, "chat text from fireStorE doesn't exist"  );
                    callback.OnResponse(false,null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to load chat text from fireStore" , e);
                callback.OnResponse(false,null);
            }
        });
    }

    public static String getPicNameFromChatMsg(String chatMsg) {
        int startPosOfPic = chatMsg.indexOf("__");
        if (startPosOfPic >= 0) {
            int endPosOfPic = chatMsg.indexOf("__", startPosOfPic + "__".length());
            if (endPosOfPic >= 0) {
                String pictureName = chatMsg.substring(startPosOfPic+2, endPosOfPic);
                return pictureName;
            }
        }

        return null;
    }

}


