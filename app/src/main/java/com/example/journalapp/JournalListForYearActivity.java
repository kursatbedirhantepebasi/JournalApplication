package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journalapp.models.Journal;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class JournalListForYearActivity extends AppCompatActivity {
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

    public ArrayList<Journal> getStorageFilesAndCreateList(){

        String[] fileList = this.fileList();
        ArrayList<Journal> journals = new ArrayList<Journal>();

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for (String fileName:fileList) {
            Journal tempJournal =  createJournal(fileName);
            String[] values = tempJournal.date.split("/");
            if(values[2].equals(Integer.toString(currentYear))){

                journals.add(tempJournal);

            }
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
            String tags = properties[6];

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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_menu,menu);
        MenuItem item = menu.findItem(R.id.forSearchItem);
        SearchView searchView = (SearchView) item.getActionView();

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(JournalListForYearActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            case R.id.statistic:
                Intent journalStatisticActivity = new Intent(JournalListForYearActivity.this, JournalStatisticActivity.class);
                startActivity(journalStatisticActivity);
                return true;
            case R.id.forAll:
                Intent listJournalActivity = new Intent(JournalListForYearActivity.this, ListJournalActivity.class);
                startActivity(listJournalActivity);
                return true;
            case R.id.forDay:
                Intent journalListForDayActivity = new Intent(JournalListForYearActivity.this, JournalListForDayActivity.class);
                startActivity(journalListForDayActivity);
                return true;
            case R.id.forMonth:
                Intent journalListForMonthActivity = new Intent(JournalListForYearActivity.this, JournalListForMonthActivity.class);
                startActivity(journalListForMonthActivity);
                return true;
            case R.id.forYear:
                Intent journalListForYearActivity = new Intent(JournalListForYearActivity.this, JournalListForYearActivity.class);
                startActivity(journalListForYearActivity);
                return true;
            case R.id.statisticForTags:
                Intent journalStatisticForTagsActivity = new Intent(JournalListForYearActivity.this, JournalStatisticForTagsActivity.class);
                startActivity(journalStatisticForTagsActivity);
                return true;
            case R.id.forSelectedDate:
                Intent journalListForSelectedDate = new Intent(JournalListForYearActivity.this, JournalListForSelectedDate.class);
                startActivity(journalListForSelectedDate);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
