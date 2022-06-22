package com.example.nutritionapp.model;

import com.example.nutritionapp.R;

import java.util.LinkedList;
import java.util.List;

public class MenuItem {
    private String text;
    private int img;
    private String activityName;

    MenuItem(String text, int img, String activityName) {
        this.text = text;
        this.img = img;
        this.activityName = activityName;
    }

    public static List<MenuItem> generateItemList(boolean displayAdminMenu) {

        List<MenuItem> data = new LinkedList<>();
        if (displayAdminMenu) {
            data.add(new MenuItem("ניהול לקוחות", R.drawable.img1, "com.example.nutritionapp.ContactActivity"));
        }
        if (!displayAdminMenu) {
            data.add(new MenuItem("סטטוס אישי", R.drawable.img2, "StatusFragment"));
        }
        data.add(new MenuItem("מתכונים", R.drawable.img3, "com.example.nutritionapp.RecipeListActivity"));
        if (!displayAdminMenu) {
            data.add(new MenuItem("פרופיל", R.drawable.img4, "ProfileFragment"));
        }

        return data;
    }

    public int getImg() {
        return img;
    }

    public String getText() {
        return text;
    }

    public String getActivityName() {
        return activityName;
    }
}
