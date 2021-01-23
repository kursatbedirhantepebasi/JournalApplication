package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.models.Journal;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        MenuItem item = menu.findItem(R.id.forSearchItem);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        String tag = getIntent().getStringExtra("tag");
        Log.i("tag","tag === "+tag);

        if(tag != null){
            listJournalAdapter.getFilter().filter(tag);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listJournalAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return true;
    }

    public ArrayList<Journal> getStorageFilesAndCreateList(){

        String[] fileList = this.fileList();
        ArrayList<Journal> journals = new ArrayList<Journal>();

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        DateFormat f = new SimpleDateFormat("dd/mm/yyyy");


        for (String fileName:fileList) {

            Journal journal = createJournal(fileName);

            if(startDate != null && endDate != null){

                Date d1startDate = f.parse(startDate, new ParsePosition(0));
                Date d2endDate = f.parse(endDate, new ParsePosition(0));
                Date d3journalDate = f.parse(journal.date, new ParsePosition(0));
                Log.i("test","startDate = "+ startDate);
                Log.i("test","endDate = "+ endDate);
                Log.i("test","journal.date = "+ journal.date);

                Log.i("test","d1startDate.compareTo(d1startDate) = "+ d1startDate.compareTo(d1startDate));
                Log.i("test","d2endDate.compareTo(d3journalDate) = "+ d2endDate.compareTo(d3journalDate));


                if(d1startDate.compareTo(d3journalDate)<0 && d2endDate.compareTo(d3journalDate)>0){

                    journals.add(journal);

                }

            }

            else
                journals.add(journal);

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
            String tags = new String(properties[6].getBytes("ISO-8859-9"));

            ArrayList<String> images =  new ArrayList<String>();
            if(properties.length > 7){
                images.add(properties[7]);
            }if(properties.length > 8){
                images.add(properties[8]);
            } if(properties.length > 9){
                images.add(properties[9]);
            } if(properties.length > 10){
                images.add(properties[10]);
            } if(properties.length > 11){
                images.add(properties[11]);
            } if(properties.length > 12){
                images.add(properties[12]);
            } if(properties.length > 13){
                images.add(properties[13]);
            }

            return new Journal(journalId,title,subTitle, date, location, memoryText, tags, images);

        } catch (Exception e) {
            e.printStackTrace();
            return new Journal(null,null,null,null,null,null,
                    null,null);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(ListJournalActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            case R.id.statistic:
                Intent journalStatisticActivity = new Intent(ListJournalActivity.this, JournalStatisticActivity.class);
                startActivity(journalStatisticActivity);
                return true;
            case R.id.forAll:
                Intent listJournalActivity = new Intent(ListJournalActivity.this, ListJournalActivity.class);
                startActivity(listJournalActivity);
                return true;
            case R.id.forDay:
                Intent journalListForDayActivity = new Intent(ListJournalActivity.this, JournalListForDayActivity.class);
                startActivity(journalListForDayActivity);
                return true;
            case R.id.forMonth:
                Intent journalListForMonthActivity = new Intent(ListJournalActivity.this, JournalListForMonthActivity.class);
                startActivity(journalListForMonthActivity);
                return true;
            case R.id.forYear:
                Intent journalListForYearActivity = new Intent(ListJournalActivity.this, JournalListForYearActivity.class);
                startActivity(journalListForYearActivity);
                return true;
            case R.id.statisticForTags:
                Intent journalStatisticForTagsActivity = new Intent(ListJournalActivity.this, JournalStatisticForTagsActivity.class);
                startActivity(journalStatisticForTagsActivity);
                return true;
            case R.id.forSelectedDate:
                Intent journalListForSelectedDate = new Intent(ListJournalActivity.this, JournalListForSelectedDate.class);
                startActivity(journalListForSelectedDate);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
