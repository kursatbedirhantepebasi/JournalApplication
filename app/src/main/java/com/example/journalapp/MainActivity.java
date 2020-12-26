package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static Button addJournalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        journalListControl();//kayıt varsa listeye yönlendirilmeli.
        addJournal();//günlük sayfası eklemek için.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.forSearchItem);
        searchItem.setVisible(false);
        MenuItem menuItemItem = menu.findItem(R.id.menuItem);
        menuItemItem.setVisible(false);
        return true;
    }

    public void journalListControl(){
        String[] fileList = this.fileList();
        if(fileList != null && fileList.length>0){
            Intent listActivity = new Intent(MainActivity.this, ListJournalActivity.class);
            startActivity(listActivity);
        }
    }

    public void addJournal(){
        addJournalButton = findViewById(R.id.addJournalButton);
        addJournalButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent addJournalActivity = new Intent(getApplicationContext(), AddJournal.class);
                        startActivity(addJournalActivity );

                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addJournal:
                Intent addJournalActivity = new Intent(MainActivity.this, AddJournal.class);
                startActivity(addJournalActivity);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
