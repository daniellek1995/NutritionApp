package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutritionapp.model.App;
import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.MyFireStoreHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView tvChatTitle;
    Button btnSendMsg;
    EditText etMsg;

    ListView lvChat;
    CustomAdapterForChat adapter;

    String email;
    String chatOwnerEmail;
    String chatOwnerFullname;


    ArrayList<String> listChatEntriesSortedByDates = new ArrayList<String>();
    ArrayList<Date> listSortedChatDates = new ArrayList<Date>();;

    boolean disableChatScrollDownOnce = false;

    final private String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        chatOwnerEmail = intent.getExtras().getString("email of chat owner");
        chatOwnerFullname = intent.getExtras().getString("fullname of chat owner");

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        tvChatTitle = findViewById(R.id.tvChatTitle);
        etMsg = findViewById(R.id.etMsg);
        btnSendMsg = findViewById(R.id.btnSendMsg);

        if (email.equalsIgnoreCase(chatOwnerEmail)) {
            tvChatTitle.setText(fullName + " בצ'אט עם " + App.getAdminDescription());
        }
        else {
            tvChatTitle.setText(fullName + " בצ'אט עם " + chatOwnerFullname);
        }

        lvChat = findViewById(R.id.lvChat);
        adapter = new CustomAdapterForChat(
               ChatActivity.this,
                email, chatOwnerEmail,
                fullName,
                listChatEntriesSortedByDates, listSortedChatDates,
                new CustomAdapterForChat.MyListener() {

                    @Override
                    public void onButtonUpdateRowClicked(int row) {
                        updateChatRowUsingDialog(row);
                    }

                    @Override
                    public void onButtonDeleteRowClicked(int row) {
                        deleteChatRow(row);
                    }
                });
        lvChat.setAdapter(adapter);

        MyFireStoreHandler.continuouslyListenToChat(chatOwnerEmail, (success, data) -> {
            displayChatUi(success, data);
//            if (success) {
//                Map<String, Object> mapChatFromFireStore = data;
//                listSortedChatDates.clear();
//                Map<Date, String> mapChatFromFireStoreByDates = new HashMap<>();
//                for(String keyDateStr : mapChatFromFireStore.keySet()) {
//                    try {
//                        mapChatFromFireStoreByDates.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyDateStr),
//                                (String)mapChatFromFireStore.get(keyDateStr));
//                    } catch (ParseException e) {
//                        Log.e(TAG, "Failed to get chat info from Firestore", e);
//                        return;
//                    }
//                }
//
//                for(Date keyDate : mapChatFromFireStoreByDates.keySet()) {
//                    listSortedChatDates.add(keyDate);
//                }
//
//                // Collections.reverse(listToSort);
//                Collections.sort(listSortedChatDates, new Comparator<Date>() {
//                    @Override
//                    public int compare(Date o1, Date o2) {
//                        return +o1.compareTo(o2);
//                    }
//
//                    @Override
//                    public boolean equals(Object obj) {
//                        return false;
//                    }
//                });
//
//                listChatEntriesSortedByDates.clear();
//                for (Date dateMsg : listSortedChatDates) {
//                    String msgDateStr = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd").format(dateMsg);
//                    String chatMsg = mapChatFromFireStoreByDates.get(dateMsg);
//                    listChatEntriesSortedByDates.add(msgDateStr + " " + chatMsg);
//                }
//                Log.d(TAG, "Calling adapter.notifyDataSetChanged().\n" +
//                        "listChatEntries is:\n" + listChatEntriesSortedByDates);
//                adapter.notifyDataSetChanged();
//                lvChat.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!disableChatScrollDownOnce) {
//                            // Selects the last row in the listview so it will scroll to bottom
//                            lvChat.setSelection(adapter.getCount() - 1);
//                        }
//                        disableChatScrollDownOnce = false;
//                    }
//                });
//            }
        });
    }

    public void displayChatUi(boolean success, Map<String, Object> data) {
          if (success) {
                Map<String, Object> mapChatFromFireStore = data;
                listSortedChatDates.clear();
                Map<Date, String> mapChatFromFireStoreByDates = new HashMap<>();
                for(String keyDateStr : mapChatFromFireStore.keySet()) {
                    try {
                        mapChatFromFireStoreByDates.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(keyDateStr),
                                (String)mapChatFromFireStore.get(keyDateStr));
                    } catch (ParseException e) {
                        Log.e(TAG, "Failed to get chat info from Firestore", e);
                        return;
                    }
                }

                for(Date keyDate : mapChatFromFireStoreByDates.keySet()) {
                    listSortedChatDates.add(keyDate);
                }

                // Collections.reverse(listToSort);
                Collections.sort(listSortedChatDates, new Comparator<Date>() {
                    @Override
                    public int compare(Date o1, Date o2) {
                        return +o1.compareTo(o2);
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }
                });


                listChatEntriesSortedByDates.clear();
                for (Date dateMsg : listSortedChatDates) {
                    String msgDateStr = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd").format(dateMsg);
                    String chatMsg = mapChatFromFireStoreByDates.get(dateMsg);
                    listChatEntriesSortedByDates.add(msgDateStr + " " + chatMsg);
                }
                Log.d(TAG, "Calling adapter.notifyDataSetChanged().\n" +
                        "listChatEntries is:\n" + listChatEntriesSortedByDates);
                adapter.notifyDataSetChanged();
                lvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!disableChatScrollDownOnce) {
                            // Selects the last row in the listview so it will scroll to bottom
                            lvChat.setSelection(adapter.getCount() - 1);
                        }
                        disableChatScrollDownOnce = false;
                    }
                });
            }

    }

    public void onBtnSendMsgClicked(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        Date now = Calendar.getInstance().getTime();
        String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        String msg = etMsg.getText().toString();
        MyFireStoreHandler.addChatMsg(chatOwnerEmail, nowStr, fullName, msg,
                (success, message) -> {
                    if (success) {
                        Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_LONG).show();
                        etMsg.setText("");
                    }
                    else {
                        Toast.makeText(ChatActivity.this, "Failed to send message!", Toast.LENGTH_LONG).show();
                    }

                }
        );

        // Hide soft keyboard displayed at the bottom
        InputMethodManager imm = (InputMethodManager)ChatActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View viewInFocus = ChatActivity.this.getCurrentFocus();
        if (viewInFocus == null) {
            viewInFocus = new View(ChatActivity.this);
        }
        imm.hideSoftInputFromWindow(viewInFocus.getWindowToken(),0);
    }

    public void obBtnUploadPic(View view) {
        Intent intent = new Intent(ChatActivity.this, UploadPictureActivity.class);
        intent.putExtra("email of chat owner", chatOwnerEmail);
        startActivity(intent);
    }

    private void updateChatRow(int row, String updatedMsg) {
        Date msgDate =  listSortedChatDates.get(row);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msgDateStr = dateFormat.format(msgDate);

        Toast.makeText(ChatActivity.this, "Deleting msg " + row  + " with date: [" +
                msgDateStr + "]", Toast.LENGTH_SHORT).show();

        MyFireStoreHandler.updateChatMsgByItsDate(chatOwnerEmail, msgDateStr, updatedMsg,
                new MyFireStoreHandler.RequestCallback() {
                    @Override
                    public void OnResponse(Boolean success, String message) {
                        if (success) {
                            Toast.makeText(ChatActivity.this, "Message updated!", Toast.LENGTH_SHORT).show();
                            listChatEntriesSortedByDates.set(row, updatedMsg);
                            //adapter.notifyDataSetChanged();
                            MyFireStoreHandler.getChat(chatOwnerEmail, (success2, data) -> {
                                displayChatUi(success2, data);
                            });
                            lvChat.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Selects the last row in the listview so it will scroll to bottom
                                    lvChat.setSelection(row);
                                    disableChatScrollDownOnce = true;
                                }
                            });

                        }
                        else {
                            Toast.makeText(ChatActivity.this, "Failed to update message!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void deleteChatRow(int row) {
        Date msgDate =  listSortedChatDates.get(row);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String msgDateStr = dateFormat.format(msgDate);

        Toast.makeText(ChatActivity.this, "Deleting msg " + row  + " with date: [" +
                msgDateStr + "]", Toast.LENGTH_SHORT).show();

        String chatMsg = listChatEntriesSortedByDates.get(row);
        String pictureNameInChatMsg = MyFireStoreHandler.getPicNameFromChatMsg(chatMsg);

        MyFireStoreHandler.deleteChatMsgByItsDate(chatOwnerEmail, msgDateStr,
                new MyFireStoreHandler.RequestCallback() {
                    @Override
                    public void OnResponse(Boolean success, String message) {
                        if (success) {
                            Toast.makeText(ChatActivity.this, "Message deleted!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ChatActivity.this, "Failed to delete message!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        disableChatScrollDownOnce = true;

        if (pictureNameInChatMsg != null) {
            MyFireStorageHandler.deletePicture(pictureNameInChatMsg, (status, message, uploadProgress) -> {
                if (status == MyFireStorageHandler.EnumStatus.SUCCESS) {
                    Toast.makeText(ChatActivity.this, "Pic deleted!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ChatActivity.this, "Failed to delete picture!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void updateChatRowUsingDialog(int row) {
        Log.d(TAG, "Update msg: " + listChatEntriesSortedByDates.get(row));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("עריכת הודעה");
        //Set up the dialog input text
        final EditText etMsgEdit = new EditText(this);
        etMsgEdit.setInputType(InputType.TYPE_CLASS_TEXT);
        String wholeMsg = listChatEntriesSortedByDates.get(row);
        int posOfFourthColon = 0;
        for (int i=0; i<3; i++) {
            posOfFourthColon = wholeMsg.indexOf(":", posOfFourthColon+1);
        }
        String msgPreface = wholeMsg.substring(0, posOfFourthColon);
        String msgSender = msgPreface.substring(
                "yyyy-MM-dd hh:mm:ss".length()+1, msgPreface.length());
        String msgBody = wholeMsg.substring(posOfFourthColon+2, wholeMsg.length());
        etMsgEdit.setText(msgBody);
        LinearLayout linearLayout = new LinearLayout(ChatActivity.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(etMsgEdit);
        ViewGroup.MarginLayoutParams marginParams =
                (ViewGroup.MarginLayoutParams)etMsgEdit.getLayoutParams();
        marginParams.setMargins(0,120,0,80);
        builder.setView(linearLayout);
        // Set up the dialog buttons
        builder.setPositiveButton("עדכן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedMsg = msgSender + ": " + etMsgEdit.getText().toString();
                updateChatRow(row, updatedMsg);
            }
        });
        builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}