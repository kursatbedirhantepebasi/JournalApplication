package com.example.journalapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.journalapp.models.Journal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class AddJournal extends Activity implements LocationListener {

    private String journalId;
    private boolean isEdit;

    private static Button closePopUp, deleteButton,sharedButton;
    private TextView dateView, locationView, modalHeader;
    private ImageView calendarIcon, addPhotoIcon, image1, image2, image3, image4,
            image5, image6, image7, activeImage;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    protected LocationManager locationManager;
    ArrayList<Uri> imageUris = new ArrayList<Uri>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);
        createPopUp();
        closePopUp();
        setDefaultDate();//bu günün tarihi default olarak setlenir, kullanıcı datepicker dialog ile tarihi değiştirebilir.
        setDefaultLocation();// bulunulan konum default olarak eklenir.
        openDatePickerDialog();
        addPhoto();
        clickImage();
        saveJournal();
        isEditControlAndBuildForm();
        deleteJournal();
        sharedButtonListenner();
    }

    public void isEditControlAndBuildForm(){

        journalId = getIntent().getStringExtra("journalId");
        if (journalId != null){
            isEdit = true;
            buildForm();
        }
        else {
            isEdit=false;
            deleteButton = findViewById(R.id.deleteButton);
            deleteButton.setVisibility(View.GONE);
            sharedButton = findViewById(R.id.sharedButton);
            sharedButton.setVisibility(View.GONE);
        }

    }

    public void buildForm(){
        //başlık setlenmeli
        modalHeader = (TextView) findViewById(R.id.modalHeader);
        modalHeader.setText("Edit Journal");
        //editlenicek Journal formu setlenmeli
        Journal journal = getJournal();

        EditText title = (EditText) findViewById(R.id.title);
        title.setText(journal.title);

        EditText subTitle = (EditText) findViewById(R.id.subTitle);
        subTitle.setText(journal.subTitle);

        dateView = (TextView) findViewById(R.id.date);
        dateView.setText(journal.date);

        TextView locationView = (TextView) findViewById(R.id.locationView);
        locationView.setText(journal.location);

        EditText memoryTextArea = (EditText) findViewById(R.id.memoryTextArea);
        memoryTextArea.setText(journal.memoryText);

        EditText tags = (EditText) findViewById(R.id.tags);
        tags.setText(journal.tags);

        //resimler setlenmeli
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        image7 = (ImageView) findViewById(R.id.image7);

        activeImage = (ImageView) findViewById(R.id.activeImage);
        Log.i("resimler","journal.images === "+journal.images);

        for (int i=0; i<journal.images.size(); i++){
            Uri imageUri = Uri.parse(journal.images.get(i));
            this.imageUris.add(imageUri);
            if(i==0)
            {
                activeImage.setImageBitmap(createImage(imageUri));
                image1.setImageBitmap(createImage(imageUri));
            }
            else if(i==1)
                image2.setImageBitmap(createImage(imageUri));
            else if(i==2)
                image3.setImageBitmap(createImage(imageUri));
            else if(i==3)
                image4.setImageBitmap(createImage(imageUri));
            else if(i==4)
                image5.setImageBitmap(createImage(imageUri));
            else if(i==5)
                image6.setImageBitmap(createImage(imageUri));
            else if(i==6)
                image7.setImageBitmap(createImage(imageUri));
            else
                Log.d("photoPickerIntent","Error");
        }

    }

    public Journal getJournal(){

        try {
            FileInputStream fileInputStream =  openFileInput(journalId+".txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }

            String properties[] = buffer.toString().split("#");

            String journalId = properties[0];
            String title = properties[1];
            String subTitle = properties[2];
            String date = properties[3];
            String location = properties[4];
            String memoryText = properties[5];
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


    public void createPopUp() {

        DisplayMetrics displayMetric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetric);

        int width = displayMetric.widthPixels;
        int height = displayMetric.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }

    public void closePopUp() {
        closePopUp = findViewById(R.id.closePopUp);
        closePopUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }

    public void setDefaultDate() {
        dateView = (TextView) findViewById(R.id.date);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dateView.setText(formatter.format(date));
    }

    public void setDefaultLocation() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void openDatePickerDialog(){

        calendarIcon = (ImageView) findViewById(R.id.calendarIcon);
        dateView = (TextView)findViewById(R.id.date);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar();
            }
        });

        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCalendar();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                dateView.setText(date);
            }
        };

    }

    public void createCalendar(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                AddJournal.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addPhoto(){
        addPhotoIcon = (ImageView) findViewById(R.id.addPhotoIcon);
        addPhotoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, getRequestCode());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        locationView = (TextView) findViewById(R.id.locationView);
        String cityAndCountry="";
        Geocoder geocoder = new Geocoder(AddJournal.this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            cityAndCountry = addresses.get(0).getCountryName()+"/"+ addresses.get(0).getSubAdminArea();
            locationView.setText(cityAndCountry);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @TargetApi(Build.VERSION_CODES.M)
    public int getRequestCode() {
        Set<Integer> unique = new HashSet<>();

        while (unique.size() != 10) {
            int randInt = ThreadLocalRandom.current().nextInt(1, 2000);
            unique.add(randInt);
        }

        return unique.hashCode();
    }


    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        image7 = (ImageView) findViewById(R.id.image7);

        if (resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                if(image1.getDrawable()==null){
                    imageUris.add(imageUri);
                    image1.setImageBitmap(createImage(imageUri));
                }
                else if(image2.getDrawable()==null){
                    imageUris.add(imageUri);
                    image2.setImageBitmap(createImage(imageUri));
                }
                else if(image3.getDrawable()==null){
                    imageUris.add(imageUri);
                    image3.setImageBitmap(createImage(imageUri));
                }
                else if(image4.getDrawable()==null){
                    imageUris.add(imageUri);
                    image4.setImageBitmap(createImage(imageUri));
                }
                else if(image5.getDrawable()==null){
                    imageUris.add(imageUri);
                    image5.setImageBitmap(createImage(imageUri));
                }
                else if(image6.getDrawable()==null){
                    imageUris.add(imageUri);
                    image6.setImageBitmap(createImage(imageUri));
                }
                else if(image7.getDrawable()==null){
                    imageUris.add(imageUri);
                    image7.setImageBitmap(createImage(imageUri));
                }
                else {
                    Log.d("photoPickerIntent","Error");
                }

        }else {
            Log.d("photoPickerIntent","Error");
        }
    }

    public Bitmap createImage(final Uri imageUri){
        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return selectedImage;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public void clickImage(){
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        image6 = (ImageView) findViewById(R.id.image6);
        image7 = (ImageView) findViewById(R.id.image7);

        activeImage = (ImageView) findViewById(R.id.activeImage);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>0) {
                    final Uri imageUri = imageUris.get(0);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>1) {
                    final Uri imageUri = imageUris.get(1);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>2){
                    final Uri imageUri = imageUris.get(2);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>3){
                    final Uri imageUri = imageUris.get(3);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>4){
                    final Uri imageUri = imageUris.get(4);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>5){
                    final Uri imageUri = imageUris.get(5);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUris.size()>6){
                    final Uri imageUri = imageUris.get(6);
                    activeImage.setImageBitmap(createImage(imageUri));
                }
            }
        });
    }

    public void saveJournal()  {

        Button saveJournalButton = (Button) findViewById(R.id.saveJournalButton);

        saveJournalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID uniqueKey = UUID.randomUUID();
                try {

                    EditText title = (EditText) findViewById(R.id.title);
                    EditText subTitle = (EditText) findViewById(R.id.subTitle);
                    TextView date = (TextView) findViewById(R.id.date);
                    TextView location = (TextView) findViewById(R.id.locationView);
                    TextView memoryText = (TextView) findViewById(R.id.memoryTextArea);
                    TextView tags = (TextView) findViewById(R.id.tags);

                    if(isEdit){
                        //dosyayı silip yeni dosya yaratma planlanmıştır.
                        File dir = getFilesDir();
                        File file = new File(dir, journalId+".txt");
                        file.delete();
                    }

                    FileOutputStream fileout=openFileOutput(uniqueKey+".txt", MODE_PRIVATE);
                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
                    outputWriter.write(uniqueKey.toString()+"#");
                    outputWriter.write(title.getText().toString()+"#");
                    outputWriter.write(subTitle.getText().toString()+"#");
                    outputWriter.write(date.getText().toString()+"#");
                    outputWriter.write(location.getText().toString()+"#");
                    outputWriter.write(memoryText.getText().toString()+"#");
                    outputWriter.write(tags.getText().toString()+"#");

                    if(imageUris.size()>0)
                        outputWriter.write(imageUris.get(0).toString()+"#");
                    if(imageUris.size()>1)
                        outputWriter.write(imageUris.get(1).toString()+"#");
                    if(imageUris.size()>2)
                        outputWriter.write(imageUris.get(2).toString()+"#");
                    if(imageUris.size()>3)
                        outputWriter.write(imageUris.get(3).toString()+"#");
                    if(imageUris.size()>4)
                        outputWriter.write(imageUris.get(4).toString()+"#");
                    if(imageUris.size()>5)
                        outputWriter.write(imageUris.get(5).toString()+"#");
                    if(imageUris.size()>6)
                        outputWriter.write(imageUris.get(6).toString()+"#");

                    outputWriter.close();

                    Toast toast =  Toast.makeText(AddJournal.this,"Successfully Saved",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //liste yenilenmesi için main activitiy adımlarındaki kontroller yapılmalı
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);

                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error !!!",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

    }

    public void deleteJournal(){
        deleteButton  = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //data silinmeli
                        File dir = getFilesDir();
                        File file = new File(dir, journalId+".txt");
                        file.delete();

                        //liste yenilenmesi için main activitiy adımlarındaki kontroller yapılmalı
                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivity);

                        Toast toast =  Toast.makeText(AddJournal.this,"Successfully Deleted",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();

                    }
                });
    }

    public void sharedButtonListenner(){

        sharedButton = findViewById(R.id.sharedButton);
        sharedButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);

                        EditText title = (EditText) findViewById(R.id.title);
                        EditText subTitle = (EditText) findViewById(R.id.subTitle);
                        TextView date = (TextView) findViewById(R.id.date);
                        TextView location = (TextView) findViewById(R.id.locationView);
                        TextView memoryText = (TextView) findViewById(R.id.memoryTextArea);

                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Journal Title :"+" "+title.getText().toString()+"\n"+
                                        "Journal Sub Title :"+subTitle.getText().toString()+"\n"+
                                        "Date : "+date.getText().toString()+"\n"+
                                        "Location : "+location.getText().toString()+"\n"+
                                        "Memory Text : "+memoryText.getText().toString()+"\n"

                        );


                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                    }
                });

    }

}
