package com.example.sevaarth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Donation_Data_Org {
    String name, food, clothes, books, medicines, blood, money, email, date_donated, key, date_received;
    boolean complete;
    UploadInfo uploaded_images;

    public Donation_Data_Org(){

    }

    public Donation_Data_Org(String key, String email, String name, String food, String clothes, String books, String medicines, String blood, String money, String date_donated, String date_received, boolean complete, UploadInfo uploaded_images) {
        this.key = key;
        this.email = email;
        this.name = name;
        this.food = food;
        this.clothes = clothes;
        this.books = books;
        this.medicines = medicines;
        this.blood = blood;
        this.money = money;
        this.date_donated = date_donated;
        this.date_received = date_received;
        this.complete = complete;
        this.uploaded_images = uploaded_images;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_received() {
        return date_received;
    }

    public void setDate_received(String date_received) {
        this.date_received = date_received;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
    }

    public String getBooks() {
        return books;
    }

    public void setBooks(String books) {
        this.books = books;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getMoney() {
        return money;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDate_donated() {
        return date_donated;
    }

    public void setDate_donated(String date_donated) {
        this.date_donated = date_donated;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public UploadInfo getUploaded_images() {
        return uploaded_images;
    }

    public void setUploaded_images(UploadInfo uploaded_images) {
        this.uploaded_images = uploaded_images;
    }
}
