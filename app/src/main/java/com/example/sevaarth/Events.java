package com.example.sevaarth;

public class Events {

    public Events() {
    }

    String Event_Details,Date,time,type,state,city;


    public Events(String event_Details, String date, String time, String type, String state, String city) {
        Event_Details = event_Details;
        Date = date;
        this.time = time;
        this.type = type;
        this.state = state;
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEvent_Details() {
        return Event_Details;
    }

    public void setEvent_Details(String event_Details) {
        Event_Details = event_Details;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
