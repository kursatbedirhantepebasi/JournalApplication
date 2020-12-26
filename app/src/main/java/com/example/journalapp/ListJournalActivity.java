package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.models.Journal;

import java.io.FileInputStream;
import java.util.ArrayList;

public class ListJournalActivity  extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListJournalAdapter listJournalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<Journal> journals = getStorageFilesAndCreateList();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_journal);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        listJournalAdapter = new ListJournalAdapter(journals);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listJournalAdapter);
        listJournalAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu,menu);
        return true;
    }

    public ArrayList<Journal> getStorageFilesAndCreateList(){

        String[] fileList = this.fileList();
        ArrayList<Journal> journals = new ArrayList<Journal>();

        for (String fileName:fileList) {
            journals.add(createJournal(fileName));
        }

        return journals;

    }

    public Journal createJournal(String fileName){

        try {
            FileInputStream fileInputStream =  openFileInput(fileName);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }

            String properties[] = buffer.toString().split("#");

            String journalId = new String(properties[0].getBytes("ISO-8859-9"));
            String title = new String(properties[1].getBytes("ISO-8859-9"));
            String subTitle = new String(properties[2].getBytes("ISO-8859-9"));
            String date = new String(properties[3].getBytes("ISO-8859-9"));
            String location = new String(properties[4].getBytes("ISO-8859-9"));
            String memoryText = new String(properties[5].getBytes("ISO-8859-9"));
            ArrayList<String> images =  new ArrayList<String>();
            if(properties.length > 6){
                images.add(properties[6]);
            }if(properties.length > 7){
                images.add(properties[7]);
            } if(properties.length > 8){
                images.add(properties[8]);
            } if(properties.length > 9){
                images.add(properties[9]);
            } if(properties.length > 10){
                images.add(properties[10]);
            } if(properties.length > 11){
                images.add(properties[11]);
            } if(properties.length > 12){
                images.add(properties[12]);
            }

            return new Journal(journalId,title,subTitle, date, location, memoryText, images);


        } catch (Exception e) {
            Log.i("journalrList","journalrList catch = ");

            e.printStackTrace();
            return new Journal(null,null,null,null,null,null,null);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(ListJournalActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
