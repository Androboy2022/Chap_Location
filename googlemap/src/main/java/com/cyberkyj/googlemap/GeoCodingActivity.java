package com.cyberkyj.googlemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.sql.RowSetReader;

public class GeoCodingActivity extends AppCompatActivity {

    EditText edtAddress, edtLat, edtLng;
    double latitude, longitude;
    Geocoder gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_coding);

        gc = new Geocoder(this, Locale.KOREAN);

        edtAddress = findViewById(R.id.editText);
        edtLat = findViewById(R.id.editText2);
        edtLng = findViewById(R.id.editText3);

        Button btnAddress = findViewById(R.id.button2);
        Button btnLatLng = findViewById(R.id.button3);

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLocation(edtAddress.getText().toString());
            }
        });

        btnLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = Double.parseDouble(edtLat.getText().toString());
                longitude = Double.parseDouble(edtLng.getText().toString());
                searchLocation(latitude, longitude);

            }
        });


    }

    public void searchLocation(String str){
        List<Address> addressList = null;
        try {
            addressList = gc.getFromLocationName(str,1);
            if (addressList!=null){
                for (int i=0; i<addressList.size(); i++){
                    Address address = addressList.get(i);
                    latitude = address.getLatitude();
                    longitude = address.getLongitude();
                }

                Intent intent = getIntent();
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                setResult(RESULT_OK, intent);
                finish();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchLocation(double latitude, double longitude){
        Intent intent = getIntent();
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        setResult(RESULT_OK, intent);
        finish();
    }
}















