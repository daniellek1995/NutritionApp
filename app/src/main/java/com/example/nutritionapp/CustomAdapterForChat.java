package com.example.nutritionapp;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.MyFireStoreHandler;

import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterForChat extends BaseAdapter {

    private final String TAG = "CustomAdapterForChat";

    ArrayList<String> listSortedChatMsgsByDates;
    ArrayList<Date> listSortedChaDates;
    Context context;
    MyListener myListener;
    String email;
    String chatOwnerEmail;
    String fullName;
    TextView tvMsg;
    ImageView ivChatPicture;
    Button btnDeleteMsg;
    Button btnEditMsg;
    TextView tvMsgFullName;

    public interface MyListener {
        public void onButtonUpdateRowClicked(int row);
        public void onButtonDeleteRowClicked(int row);
    }

    public CustomAdapterForChat(Context context, String email, String chatOwnerEmail,
                                String fullName,
                                ArrayList<String> listSortedChatMsgsByDates,
                                ArrayList<Date> listSortedChatDates,
                                MyListener myListener) {
        this.context = context;
        this.email = email;
        this.chatOwnerEmail = chatOwnerEmail;
        this.fullName = fullName;
        this.listSortedChatMsgsByDates = listSortedChatMsgsByDates;
        this.listSortedChaDates = listSortedChatDates;
        this.myListener = myListener;
    }

    @Override
    public int getCount() {
        return listSortedChatMsgsByDates.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false);
            Log.d(TAG, "creating new view for row" + position);
        } else {
            Log.d(TAG, "reusing old view for row" + position);
        }

        tvMsg = convertView.findViewById(R.id.tvChatMsg);
        ivChatPicture = convertView.findViewById(R.id.ivChatPicture);
        btnDeleteMsg = convertView.findViewById(R.id.btnDeleteMsg);
        btnEditMsg = convertView.findViewById(R.id.btnEditMsg);
        tvMsgFullName = convertView.findViewById(R.id.tvMsgFullName);

        btnDeleteMsg.setVisibility(View.GONE);
        btnEditMsg.setVisibility(View.GONE);

        String msg =  listSortedChatMsgsByDates.get(position);

        int posStartOfFullName = "yyyy/MM/dd HH:mm:ss ".length();
        int posEndOfFullName = msg.indexOf( ":", posStartOfFullName);
        if (posEndOfFullName > posStartOfFullName + 2) {
            String fullNameFromMsg = msg.substring(posStartOfFullName, posEndOfFullName);

            //tvMsgFullName.setText("[" + fullNameFromMsg + "]--[" + fullName + "]" + "-->" + fullName.equalsIgnoreCase(fullNameFromMsg));

            if (fullName.equalsIgnoreCase(fullNameFromMsg)) {
                btnEditMsg.setVisibility(View.VISIBLE);
                btnDeleteMsg.setVisibility(View.VISIBLE);
            }
        }

        btnEditMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adapter notifies ChatActivity that the row should be edited.
                myListener.onButtonUpdateRowClicked(position);
            }
        });

        btnDeleteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adapter notifies ChatActivity that the row should be deleted.
                myListener.onButtonDeleteRowClicked(position);
            }
        });

        ivChatPicture.setVisibility(View.GONE);
        ivChatPicture.setImageDrawable(null);

        tvMsg.setText(msg);

        String pictureNameInChatMsg = MyFireStoreHandler.getPicNameFromChatMsg(msg);

        if (pictureNameInChatMsg != null) {
                ivChatPicture.setVisibility(View.VISIBLE);
                btnEditMsg.setVisibility(View.GONE);

                tvMsg.setText(msg.substring(0, msg.indexOf("__")-2) + " תמונה ");

            String pictureInChatMsg = "images/chats/" + pictureNameInChatMsg;
            MyFireStorageHandler.downloadPictureIntoImageview(context, pictureInChatMsg,
                    ivChatPicture, new MyFireStorageHandler.RequestCallback() {
                @Override
                public void OnResponse(MyFireStorageHandler.EnumStatus status, String message, double uploadProgress) {
                    if (status == MyFireStorageHandler.EnumStatus.SUCCESS) {
                        Log.d(TAG, "Pic downloaded. pic name: " + pictureNameInChatMsg);
                    }
                    else
                    {
                        Log.e(TAG, "Failed to download pic. pic name: " + pictureNameInChatMsg);
                    }
                }
            });

        }
        return convertView;
    }
}
