package com.cyberkyj.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    SupportMapFragment mapFragment;
    GoogleMap map;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, 0);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        MapsInitializer.initialize(this);
        startLocation();

    }

    public void startLocation() {
        long time = 10000;
        float distance = 0;
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            GPSListener gpsListener = new GPSListener();
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, distance, gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, time, distance, gpsListener);

        } catch (SecurityException e) {

        }
    }

    class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            showCurrentLocation(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    public void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        try {
            //map.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }

        showAllItems(35.83359854213146, 127.13794409754205, R.drawable.school,"백제","직업전문학교");

    }

    private void showAllItems(double latitude, double longitude, int id, String title, String snippet){
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(latitude, longitude));
        marker.title(title);
        marker.snippet(snippet);
        marker.draggable(true);
        marker.icon(BitmapDescriptorFactory.fromResource(id));
        map.addMarker(marker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"위성지도");
        menu.add(0,2,0,"일반지도");
        menu.add(0,3,0,"주소 검색창으로 이동");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                showCurrentLocation(latitude, longitude);
                break;
            case 2:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                showCurrentLocation(latitude, longitude);
                break;
            case 3:
                Intent intent = new Intent(getApplicationContext(), GeoCodingActivity.class);
                startActivityForResult(intent, 1000);
                break;

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000 && resultCode==RESULT_OK){
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);
            showCurrentLocation(latitude, longitude);
            Toast.makeText(this, "검색이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "검색이 취소되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}

















