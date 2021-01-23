package com.example.journalapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class JournalListForSelectedDate extends AppCompatActivity {
    private ImageView startDateCalendarIcon, endDateCalendarIcon;
    private TextView startDate, endDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener1, mDateSetListener2;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_list_for_selected_date);
        openDatePickerDialog1();
        openDatePickerDialog2();
        search();
    }

    public void openDatePickerDialog1(){
        startDateCalendarIcon = (ImageView) findViewById(R.id.startDateCalendarIcon);
        startDate = (TextView)findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar();
            }
        });
        startDateCalendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar();
            }
        });
        mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                startDate.setText(date);
            }
        };
    }

    public void openDatePickerDialog2(){
        endDateCalendarIcon = (ImageView) findViewById(R.id.endDateCalendarIcon);
        endDate = (TextView)findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar2();
            }
        });
        endDateCalendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar2();
            }
        });
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                endDate.setText(date);
            }
        };
    }

    public void createCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                JournalListForSelectedDate.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener1,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void createCalendar2(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                JournalListForSelectedDate.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener2,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void search(){

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView startDate = (TextView) findViewById(R.id.startDate);
                TextView endDate = (TextView) findViewById(R.id.endDate);


                Intent intent = new Intent(JournalListForSelectedDate.this, ListJournalActivity.class);
                intent.putExtra("startDate", startDate.getText());
                intent.putExtra("endDate", endDate.getText());
                JournalListForSelectedDate.this.startActivity(intent);


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(JournalListForSelectedDate.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            case R.id.statistic:
                Intent journalStatisticActivity = new Intent(JournalListForSelectedDate.this, JournalStatisticActivity.class);
                startActivity(journalStatisticActivity);
                return true;
            case R.id.forAll:
                Intent listJournalActivity = new Intent(JournalListForSelectedDate.this, ListJournalActivity.class);
                startActivity(listJournalActivity);
                return true;
            case R.id.forDay:
                Intent journalListForDayActivity = new Intent(JournalListForSelectedDate.this, JournalListForDayActivity.class);
                startActivity(journalListForDayActivity);
                return true;
            case R.id.forMonth:
                Intent journalListForMonthActivity = new Intent(JournalListForSelectedDate.this, JournalListForMonthActivity.class);
                startActivity(journalListForMonthActivity);
                return true;
            case R.id.forYear:
                Intent journalListForYearActivity = new Intent(JournalListForSelectedDate.this, JournalListForYearActivity.class);
                startActivity(journalListForYearActivity);
                return true;
            case R.id.statisticForTags:
                Intent journalStatisticForTagsActivity = new Intent(JournalListForSelectedDate.this, JournalStatisticForTagsActivity.class);
                startActivity(journalStatisticForTagsActivity);
                return true;
            case R.id.forSelectedDate:
                Intent journalListForSelectedDate = new Intent(JournalListForSelectedDate.this, JournalListForSelectedDate.class);
                startActivity(journalListForSelectedDate);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
