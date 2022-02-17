package com.example.sevaarth;
import java.util.Map;

public class Org_Details {
    private String head;
    private String address;
    private String desc, phone,state, city;
    int strength,area,food,clothes,medicine,book,blood,money;
    private int details_verify, docs_verify, verify, docs_status, image_status, details_status;
    private Map<String, UploadInfo> images;
    public Org_Details() {

    }

    public Org_Details(String head, int area, String address, String city, int strength, int details_verify, int docs_verify, int food, int clothes, int medicine, int book, int blood, int money, String phone, String desc, String state, Map<String, UploadInfo> images, int docs_status, int image_status, int details_status) {
        this.head = head;
        this.address = address;
        this.city = city;
        this.strength = strength;
        this.area = area;
        this.food = food;
        this.clothes = clothes;
        this.medicine = medicine;
        this.book = book;
        this.blood = blood;
        this.money = money;
        this.phone = phone;
        this.desc = desc;
        this.details_verify = details_verify;
        this.docs_verify = docs_verify;
        this.verify = 0;
        this.state = state;
        this.images = images;
        this.docs_status  = docs_status;
        this.image_status = image_status;
        this.details_status = details_status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDetails_status() {
        return details_status;
    }

    public void setDetails_status(int details_status) {
        this.details_status = details_status;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setClothes(int clothes) {
        this.clothes = clothes;
    }

    public void setMedicine(int medicine) {
        this.medicine = medicine;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDetails_verify() {
        return details_verify;
    }

    public void setDetails_verify(int details_verify) {
        this.details_verify = details_verify;
    }

    public int getDocs_verify() {
        return docs_verify;
    }

    public void setDocs_verify(int docs_verify) {
        this.docs_verify = docs_verify;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public String getHead() {
        return head;
    }

    public String getAddress() {
        return address;
    }

    public int getStrength() {
        return strength;
    }

    public int getArea() {
        return area;
    }

    public int getFood() {
        return food;
    }

    public int getClothes() {
        return clothes;
    }

    public int getMedicine() {
        return medicine;
    }

    public int getBook() {
        return book;
    }

    public int getBlood() {
        return blood;
    }

    public int getMoney() {
        return money;
    }

    public int getDocs_status() {
        return docs_status;
    }

    public void setDocs_status(int docs_status) {
        this.docs_status = docs_status;
    }

    public int getImage_status() {
        return image_status;
    }

    public void setImage_status(int image_status) {
        this.image_status = image_status;
    }

    public Map<String, UploadInfo> getImages() {
        return images;
    }

    public void setImages(Map<String, UploadInfo> images) {
        this.images = images;
    }

}


