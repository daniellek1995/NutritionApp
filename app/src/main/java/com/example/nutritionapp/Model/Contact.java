package com.example.nutritionapp.model;

public class Contact {

    private String email;
    private String fullName;
    private String phone;
    private boolean paid;

    public Contact(String email, String fullName, String phone, boolean paid) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.paid = paid;
    }

    public Contact() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
