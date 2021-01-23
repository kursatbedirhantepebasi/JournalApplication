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
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JournalStatisticForTagsActivity extends AppCompatActivity {

    AnyChartView anyChartView;

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Integer> earns = new ArrayList<Integer>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_statistic_for_tags);
        anyChartView = findViewById(R.id.chart);
        createChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createChart(){

        String[] fileList = this.fileList();
        ArrayList<String> allTags = new ArrayList<String>();
        ArrayList<String> formattedTags = new ArrayList<String>();
        ArrayList<String> formattedTrimTags = new ArrayList<String>();


        for (String fileName:fileList) {
            allTags.add(getTags(fileName));
        }

        formattedTags = formatTags(allTags);

        for(int i = 0; i<formattedTags.size();i++){
           if(!formattedTags.get(i).equals("")){
               formattedTrimTags .add(formattedTags.get(i));
           }
        }

        Map<String, Long> result =
                formattedTrimTags.stream().map(String::trim).collect(
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

    public ArrayList<String> formatTags(ArrayList<String> tages){

        ArrayList<String> formattedTags = new ArrayList<String>();

        for (int i = 0; i<tages.size();i++){
             String[] tempValues = tages.get(i).split("@");
             for(int j=0; j<tempValues.length; j++){
                     formattedTags.add(tempValues[j]);
             }
        }

        return formattedTags;
    }

    public String getTags(String fileName){

        try {
            FileInputStream fileInputStream =  openFileInput(fileName);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }

            String properties[] = buffer.toString().split("#");
            String tag = properties[6];

            return tag;
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

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                String tag = event.getData().get("x");
                Intent intent = new Intent(JournalStatisticForTagsActivity.this, ListJournalActivity.class);
                intent.putExtra("tag", tag);
                JournalStatisticForTagsActivity.this.startActivity(intent);
            }
        });

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
                Intent addJournalActivity = new Intent(JournalStatisticForTagsActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            case R.id.statistic:
                Intent journalStatisticActivity = new Intent(JournalStatisticForTagsActivity.this, JournalStatisticActivity.class);
                startActivity(journalStatisticActivity);
                return true;
            case R.id.forAll:
                Intent listJournalActivity = new Intent(JournalStatisticForTagsActivity.this, ListJournalActivity.class);
                startActivity(listJournalActivity);
                return true;
            case R.id.forDay:
                Intent journalListForDayActivity = new Intent(JournalStatisticForTagsActivity.this, JournalListForDayActivity.class);
                startActivity(journalListForDayActivity);
                return true;
            case R.id.forMonth:
                Intent journalListForMonthActivity = new Intent(JournalStatisticForTagsActivity.this, JournalListForMonthActivity.class);
                startActivity(journalListForMonthActivity);
                return true;
            case R.id.forYear:
                Intent journalListForYearActivity = new Intent(JournalStatisticForTagsActivity.this, JournalListForYearActivity.class);
                startActivity(journalListForYearActivity);
                return true;
            case R.id.statisticForTags:
                Intent journalStatisticForTagsActivity = new Intent(JournalStatisticForTagsActivity.this, JournalStatisticForTagsActivity.class);
                startActivity(journalStatisticForTagsActivity);
                return true;
            case R.id.forSelectedDate:
                Intent journalListForSelectedDate = new Intent(JournalStatisticForTagsActivity.this, JournalListForSelectedDate.class);
                startActivity(journalListForSelectedDate);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
