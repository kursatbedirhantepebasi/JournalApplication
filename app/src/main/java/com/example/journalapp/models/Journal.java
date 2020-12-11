package com.example.journalapp.models;

import java.util.Date;

public class Journal {
    public Date date;
    public String[] images;
    public String location, subTitle, title;

    public Journal(Date date, String[] images, String location, String subject, String title){
        this.date=date;
        this.images=images;
        this.location=location;
        this.subTitle =subject;
        this.title=title;
    }

}
