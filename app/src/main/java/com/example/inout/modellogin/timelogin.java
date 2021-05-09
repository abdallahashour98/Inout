package com.example.inout.modellogin;

import java.util.Date;

public class timelogin {
    private String day;
    private Date time;
    public timelogin() {
    }

    public timelogin(String address, String email, String day, String password, Date time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }
}
