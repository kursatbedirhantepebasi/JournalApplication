package com.example.journalapp.models;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class Journal {
    public ArrayList<String> images;
    public String journalId, date, location, subTitle, title, memoryText;

    public Journal(String journalId, String title, String subTitle, String date, String location, String memoryText, ArrayList<String> images){
        this.journalId=journalId;
        this.title=title;
        this.subTitle=subTitle;
        this.date=date;
        this.location=location;
        this.memoryText=memoryText;
        this.images=images;
    }

}
