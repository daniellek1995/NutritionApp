package com.example.nutritionapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomAdapterForUsersList extends BaseAdapter{

    ArrayList<String> arrayList;
    Context context;
    TextView tvUserDescription;

    public CustomAdapterForUsersList(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
            Log.d("TAG", "creating new view for row" + position);
        } else {
            Log.d("TAG", "reusing old view for row" + position);
        }

        tvUserDescription = convertView.findViewById(R.id.tvUserDescription);

        String userDesc = arrayList.get(position);

        tvUserDescription.setText(userDesc);

        return convertView;
    }
}
