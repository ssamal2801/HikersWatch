package com.swagatsamal.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
        else
        {
            Toast.makeText(this,"Can't locate  you without permission!",Toast.LENGTH_LONG).show();
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("YOUR LOCATION IS ",location.toString());
                locationUpdate(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationUpdate(lastKnownLocation);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationUpdate(lastKnownLocation);
        }


    }


    private void locationUpdate(Location lastKnownLocation)
    {

        String lat = "Latitude: "+(lastKnownLocation.getLatitude());
        String longt = "Longitude: "+(lastKnownLocation.getLongitude());
        String accuracy = "Accuracy: "+(lastKnownLocation.getAccuracy());
        String speedSpeed = "Your Speed: "+(lastKnownLocation.getSpeed()) + " Km/hr";
        String address = "You are somewhere near: ";

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = coder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),1);
            if (addressList!=null && addressList.size()>0)
            {
                if(addressList.size()>0) {
                    if (addressList.get(0).getSubLocality()!=null)
                    {
                        address += addressList.get(0).getSubLocality()+ ", ";
                    }
                    if (addressList.get(0).getLocality()!=null)
                    {
                        address += addressList.get(0).getLocality()+ ", ";
                    }
                    if (addressList.get(0).getAdminArea()!=null)
                    {
                        address +=  addressList.get(0).getAdminArea()+ ", ";
                    }
                    if (addressList.get(0).getPostalCode()!=null)
                    {
                        address += addressList.get(0).getPostalCode()+ ", ";
                    }
                    if (addressList.get(0).getCountryCode()!=null)
                    {
                        address += addressList.get(0).getCountryCode();
                    }
                }

            }

        } catch (IOException e) {
            Log.i("MESSAGE ","Error in GeoCoding");
        }


        TextView latitude = findViewById(R.id.latView);
        latitude.setText(lat);

        TextView longitude = findViewById(R.id.longView);
        longitude.setText(longt);

        TextView speedView = findViewById(R.id.speedView);
        speedView.setText(speedSpeed);

        TextView accuracyView = findViewById(R.id.accuracyView);
        accuracyView.setText(accuracy);

        TextView addressView = findViewById(R.id.addressView);
        addressView.setText(address);
    }
}