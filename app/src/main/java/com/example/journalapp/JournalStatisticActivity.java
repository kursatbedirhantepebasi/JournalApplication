package com.example.journalapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JournalStatisticActivity extends AppCompatActivity {

    AnyChartView anyChartView;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> earns = new ArrayList<Integer>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_statistic);
        anyChartView = findViewById(R.id.chart);
        createChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createChart(){

        String[] fileList = this.fileList();
        ArrayList<String> journals = new ArrayList<String>();
        ArrayList<String> formattedDates = new ArrayList<String>();

        for (String fileName:fileList) {
            journals.add(getDates(fileName));
        }

        Log.i("result","journals = "+journals);
        formattedDates = formatDate(journals);

        Map<String, Long> result =
                formattedDates.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );


        for (int i=0; i<result.values().size(); i++){
            earns.add(((Long) result.values().toArray()[i]).intValue());
            names.add(((String) result.keySet().toArray()[i]).toString());

        }

        setupPieChart();

    }

    public ArrayList<String> formatDate ( ArrayList<String> dates){

        ArrayList<String> formattedDates = new ArrayList<String>();

        for (int i = 0; i<dates.size();i++){
             String[] tempValues = dates.get(i).split("/");
            formattedDates.add(tempValues[1]+'/'+tempValues[2]);
        }
        return formattedDates;
    }

    public String getDates(String fileName){

        try {
            FileInputStream fileInputStream =  openFileInput(fileName);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }

            String properties[] = buffer.toString().split("#");
            String date = properties[3];

            return date;
        } catch (Exception e) {
                return "";
        }

    }

    public  void setupPieChart(){

      Pie pie = AnyChart.pie();
      List<DataEntry> dataEntryList = new ArrayList<>();

      for (int i = 0; i< names.size(); i++){
          dataEntryList.add(new ValueDataEntry(names.get(i),earns.get(i)));
      }

      pie.data(dataEntryList);
      anyChartView.setChart(pie);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu,menu);
        MenuItem item = menu.findItem(R.id.forSearchItem);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(JournalStatisticActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            case R.id.statistic:
                Intent journalStatisticActivity = new Intent(JournalStatisticActivity.this, JournalStatisticActivity.class);
                startActivity(journalStatisticActivity);
                return true;
            case R.id.forAll:
                Intent listJournalActivity = new Intent(JournalStatisticActivity.this, ListJournalActivity.class);
                startActivity(listJournalActivity);
                return true;
            case R.id.forDay:
                Intent journalListForDayActivity = new Intent(JournalStatisticActivity.this, JournalListForDayActivity.class);
                startActivity(journalListForDayActivity);
                return true;
            case R.id.forMonth:
                Intent journalListForMonthActivity = new Intent(JournalStatisticActivity.this, JournalListForMonthActivity.class);
                startActivity(journalListForMonthActivity);
                return true;
            case R.id.forYear:
                Intent journalListForYearActivity = new Intent(JournalStatisticActivity.this, JournalListForYearActivity.class);
                startActivity(journalListForYearActivity);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
