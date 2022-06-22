package com.example.nutritionapp.model;

import com.example.nutritionapp.R;

import java.util.LinkedList;
import java.util.List;

public class InformationDataItem {
    private String id;
    private String text;
    private int img;

    public InformationDataItem(String id, String text, int img) {
        this.id = id;
        this.text = text;
        this.img = img;
    }

    public static List<InformationDataItem> generateItemList() {

        List<InformationDataItem> data = new LinkedList<>();
        data.add(new InformationDataItem("1", "מרקים", R.drawable.logo1));
        data.add(new InformationDataItem("2","עיקריות", R.drawable.logo1));
        data.add(new InformationDataItem("3","צמחוני", R.drawable.logo1));
        data.add(new InformationDataItem("4","קינוחים", R.drawable.logo1));

        return data;
    }

    public String getText() {
        return text;
    }

    public int getImg() {
        return img;
    }

    public String getId() {
        return id;
    }
}
