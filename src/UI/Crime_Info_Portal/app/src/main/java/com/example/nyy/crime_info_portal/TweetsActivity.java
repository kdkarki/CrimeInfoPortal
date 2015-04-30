package com.example.nyy.crime_info_portal;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.Twitter;
import io.fabric.sdk.android.Fabric;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.core.services.params.Geocode;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Address;


public class TweetsActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.nyy.test.MESSAGE";
    public final static String EXTRA_NAME = "com.example.nyy.test.name";
    public final static String EXTRA_CITYNAME = "com.example.nyy.test.cityname";
    public final static String EXTRA_CRIME = "com.example.nyy.test.crime";

    TextView output;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "VD4P1VqpR0fo5Urnm2h8ue1fA";
    private static final String TWITTER_SECRET = "2v1PPYaIpcBqZ4SLGzJ3uJrAceyOMzN8XkUFldzSGQygA4Nafo";
    private static final String SEARCH_QUERY = "crime OR robbery";
    private static final int SEARCH_COUNT = 20;
    private static final String SEARCH_RESULT_TYPE = "recent";

    String name;
    String city_id;
    String city_name;
    String crime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        Intent intent = getIntent();
        city_id = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        name = intent.getStringExtra(LoginActivity.EXTRA_NAME);
        city_name = intent.getStringExtra(LoginActivity.EXTRA_CITYNAME);
        crime = intent.getStringExtra(LoginActivity.EXTRA_CRIME);
        final Geocode geocode = getFromCityName("Chantilly, VA");
        output = (TextView) findViewById(R.id.tweets_text);

        final Geocode gc = getFromCityName(city_name);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Twitter(authConfig));


        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {


                AppSession guestAppSession = (AppSession) appSessionResult.data;

                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
// Can also use Twitter directly: Twitter.getApiClient()
                //StatusesService statusesService = twitterApiClient.getStatusesService();
                SearchService service = twitterApiClient.getSearchService();
                service.tweets(SEARCH_QUERY, gc, "en", null, SEARCH_RESULT_TYPE, SEARCH_COUNT, null, null, null, true, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        //Do something with result, which provides a Tweet inside of result.data
                        final List<Tweet> tweets = result.data.tweets;
                        for (Tweet tweet:tweets) {
                            String x = tweet.text + "\n\n";
                            output.append(x);
                        }
                    }

                    public void failure(TwitterException exception) {
                        //Do something on failure
                        output.append("fail\n");
                    }
                });


            }

            @Override
            public void failure(TwitterException e) {
                // OOPS

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Geocode getFromCityName(String cityName){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        double latitude;
        double longtitude;
        Geocode geocode = new Geocode(0.0,0.0,10,null);
        try{
            List<Address> address = geocoder.getFromLocationName(cityName,1);
            for (int i = 0; i < 10; i++) {
                if (address == null) {
                    address = geocoder.getFromLocationName(cityName, 1);
                }
            }
            if (address.size()> 0){
                latitude = address.get(0).getLatitude();
                longtitude = address.get(0).getLongitude();
                //return Double.toString(latitude)+","+Double.toString(longtitude)+","+"10mi";
                geocode = new Geocode(latitude,longtitude,100, Geocode.Distance.MILES);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return geocode;

    }

    public void map_method(View view){
        Intent map_intent = new Intent(this,MapsActivity.class);
        map_intent.putExtra(EXTRA_MESSAGE,city_id);
        map_intent.putExtra(EXTRA_NAME,name);
        map_intent.putExtra(EXTRA_CITYNAME,city_name);
        startActivity(map_intent);
    }

    public void report_method(View view){
        Intent report_intent = new Intent(this,ReportActivity.class);
        report_intent.putExtra(EXTRA_MESSAGE,city_id);
        report_intent.putExtra(EXTRA_NAME,name);
        report_intent.putExtra(EXTRA_CITYNAME,city_name);
        startActivity(report_intent);
    }
}
