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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class AddJournal extends Activity implements LocationListener {

    private static Button closePopUp;
    private TextView dateView, locationView;
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
        clickImage1();
        //clickImage2();
    }

    public void createPopUp() {

        DisplayMetrics displayMetric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetric);

        int width = displayMetric.widthPixels;
        int height = displayMetric.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .8));

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


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, getRequestCode());



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

    public void clickImage1(){
        image1 = (ImageView) findViewById(R.id.image1);
        activeImage = (ImageView) findViewById(R.id.activeImage);
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Uri imageUri = imageUris.get(0);
                activeImage.setImageBitmap(createImage(imageUri));
            }
        });
    }

}
