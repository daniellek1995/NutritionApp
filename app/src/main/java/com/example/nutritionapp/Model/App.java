package com.example.nutritionapp.model;

public class App {

    private static String adminEmail = "eden1709@gmail.com";
    private static String adminDescription = "עדן תזונאית קלינית";

    public static boolean isAdmin(String email) {
        return email.equalsIgnoreCase(adminEmail);
    }

    public static String getAdminEmail() {
        return adminEmail;
    }

    public static String getAdminDescription() {
        return adminDescription;
    }
}
