package com.example.nyy.crime_info_portal;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.example.nyy.crime_info_portal.model.Address;
import com.example.nyy.crime_info_portal.model.Crime;
import com.example.nyy.crime_info_portal.parser.Address_XMLParser;
import com.example.nyy.crime_info_portal.parser.Crime_XMLParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.sdk.android.core.services.params.Geocode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {
    public final static String EXTRA_MESSAGE = "com.example.nyy.test.MESSAGE";
    public final static String EXTRA_NAME = "com.example.nyy.test.name";
    public final static String EXTRA_CITYNAME = "com.example.nyy.test.cityname";
    public final static String EXTRA_CRIME = "com.example.nyy.test.crime";
    List<Marker> mMarkers = new ArrayList<Marker>();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    String city_id;
    String name;
    String city_name;
    String crimeXML;
    List<Crime> crimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.d("MyTag","This is just a test");

        Intent intent = getIntent();
        city_id = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        name = intent.getStringExtra(LoginActivity.EXTRA_NAME);
        crimeXML = intent.getStringExtra(LoginActivity.EXTRA_CRIME);
        city_name = intent.getStringExtra(LoginActivity.EXTRA_CITYNAME);
        crimes = Crime_XMLParser.parseFeed(crimeXML);


        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        LatLng latLng = getFromCityName(city_name);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        /*for(int i = 0; i < crimes.size(); i++) {
            LatLng crime_address = getFromCityName(crimes.get(i).get_address());
            //if (crime_address.equals(new LatLng(0.0, 0.0))) continue;
            //else {
                Log.d("MyTag", crimes.get(i).get_address());
                Log.d("MyTag", crime_address.toString());
                mMap.addMarker(new MarkerOptions().position(crime_address).title(crimes.get(i).get_type()));

            //}
        }*/
        add_markers();
    }
    private void add_markers() {
        for (int i = 0; i < crimes.size();i++){
            LatLng crime_address = getFromCityName(crimes.get(i).get_address());
            Log.d("MyTag", crimes.get(i).get_address());
            Log.d("MyTag", crime_address.toString());
            if (!crime_address.equals(new LatLng(0.0,0.0))) {
                mMarkers.add(mMap.addMarker(new MarkerOptions().position(crime_address).title(crimes.get(i).get_type())
                        .snippet(crimes.get(i).get_date_arrested())));
            }
        }
    }
    public LatLng getFromCityName(String cityName){
        double latitude;
        double longtitude;

        LatLng geocode = new LatLng(0.0,0.0);
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try{
            List<android.location.Address> address = gc.getFromLocationName(cityName,1);
            for (int i = 0; i < 10; i++) {
                if (address == null) {
                    address = gc.getFromLocationName(cityName, 1);
                }
            }

            if (address.size()> 0){
                latitude = address.get(0).getLatitude();
                longtitude = address.get(0).getLongitude();
                String log_test = Double.toString(latitude)+","+Double.toString(longtitude)+","+"10mi";
                geocode = new LatLng(latitude,longtitude);
                Log.d("MyTag",log_test);

            }
            //return "There is a mistake1";

        }catch (IOException e) {
            e.printStackTrace();
            Log.d("MyTag","There is an IOException");
        }
        return geocode;

    }



    public void report_method(View view){
        Intent report_intent = new Intent(this,ReportActivity.class);
        report_intent.putExtra(EXTRA_MESSAGE,city_id);
        report_intent.putExtra(EXTRA_NAME,name);
        report_intent.putExtra(EXTRA_CITYNAME,city_name);
        report_intent.putExtra(EXTRA_CRIME,crimeXML);
        startActivity(report_intent);
    }

    public void tweets_method(View view){
        Intent tweets_intent = new Intent(this,TweetsActivity.class);
        tweets_intent.putExtra(EXTRA_MESSAGE,city_id);
        tweets_intent.putExtra(EXTRA_NAME,name);
        tweets_intent.putExtra(EXTRA_CITYNAME,city_name);
        tweets_intent.putExtra(EXTRA_CRIME,crimeXML);
        startActivity(tweets_intent);
    }
}
