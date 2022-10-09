package com.tuhin.hiker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;
    TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = (TextView) findViewById(R.id.textView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        String result = "";
        if (location != null) {
            Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
            Double alt = location.getAltitude();
            float bearing = location.getBearing();
            float accuracy = location.getAccuracy();
            float speed = location.getSpeed();
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
            long time = location.getTime();
            result = "Latitude : " + String.valueOf(lat) + "\r\n";
            result  += "Longitude : " + String.valueOf(lng) + "\r\n";
            result += "Altitude : " + String.valueOf(alt) + "\r\n";
            result += "Bearing : " + String.valueOf(bearing) + "\r\n";
            result += "Speed : " + String.valueOf(speed) + "\r\n";
            result += "Accuracy : " + String.valueOf(accuracy) + "\r\n";
            myText.setText(result);
        } else {
            Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_SHORT).show();
        }
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(address != null) {
                String data = "\r\n";
                for(int i=0;i<=address.get(0).getMaxAddressLineIndex();i++){
                    data += address.get(0).getAddressLine(i) + "\r\n";
                }
                result += data;
                myText.setText(result);
                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                Log.i("hello:", "world");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "NOT DONE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

