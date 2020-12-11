package com.example.journalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static Button addJournalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        journalListControl();//kayıt varsa listeye yönlendirilmeli.
        addJournal();//günlük sayfası eklemek için.
    }

    public void journalListControl(){
        /*if(dogru){
            listeye yönlendir.
        }*/
    }

    public void addJournal(){
        addJournalButton = findViewById(R.id.addJournalButton);
        addJournalButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent addReminderActivity = new Intent(getApplicationContext(), AddJournal.class);
                        startActivity(addReminderActivity);

                    }
                }
        );
    }

}
