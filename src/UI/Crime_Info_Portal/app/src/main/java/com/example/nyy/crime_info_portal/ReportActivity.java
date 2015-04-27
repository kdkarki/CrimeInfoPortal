package com.example.nyy.crime_info_portal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.nyy.crime_info_portal.model.Address;
import com.example.nyy.crime_info_portal.model.Crime;
import com.example.nyy.crime_info_portal.parser.Address_XMLParser;
import com.example.nyy.crime_info_portal.parser.Crime_XMLParser;

public class ReportActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.nyy.test.MESSAGE";
    public final static String EXTRA_NAME = "com.example.nyy.test.name";
    public final static String EXTRA_CITYNAME = "com.example.nyy.test.cityname";
    public final static String EXTRA_CRIME = "com.example.nyy.test.crime";

    private TextView report;
    private List<Crime> crimes = new ArrayList<>();
    private Address address;
    private String city_id;
    private String city_name;
    private String name;
    private String crimeXML;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        report = (TextView)findViewById(R.id.report_text);
        Intent intent = getIntent();
        name = intent.getStringExtra(LoginActivity.EXTRA_NAME);
        String report_uri = "http://52.11.140.62/cipwebservice/service.svc/" +
                "GetUserAddressByUserName?uname=" + name;
        report.setText("\n");
        if (isOnline()) {
            requestData(report_uri);

        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changeAddr) {
            return true;
        }
        else if (id == R.id.changeType) {
            return true;
        }
        else if (id == R.id.changePwd) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        ReportTask task = new ReportTask(this);
        task.execute(uri);
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



    public class ReportTask extends AsyncTask<String, String, String> {
        private Activity activity;

        public ReportTask(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(String... params) {

            // TODO: retrieve city_id and city_name.
            String content = HttpManager.getData(params[0]);
            address = Address_XMLParser.parseFeed(content);
            city_id = Integer.toString(address.get_city_id());
            city_name = address.get_city_name();

            // TODO: retrieve crime XML by city_id.
            String usrname_uri = "http://52.11.140.62/cipwebservice/service.svc/GetCriminalRecordsByCityId?cityid=" + city_id;
            crimeXML = HttpManager.getData(usrname_uri);
            return crimeXML;


        }

        @Override
        protected void onPostExecute(String result) {
            // TODO: parse crime information from XML.
            crimes = Crime_XMLParser.parseFeed(result);
            for(int i= 0; i < crimes.size(); i++) {
                String fname = crimes.get(i).get_fname();
                String lname = crimes.get(i).get_lname();
                String type = crimes.get(i).get_type();
                if(type.equals("Other")) type = type + " crime";
                String address = crimes.get(i).get_address();
                String date = crimes.get(i).get_date_arrested();
                String sentence = date.substring(0,10) + "     "+ type + "   by   " + fname + " " + lname + "   at   " + address;
                report.append(sentence + "\n");
            }

        }
    }

    public void map_method(View view){
        Intent map_intent = new Intent(this,MapsActivity.class);
        map_intent.putExtra(EXTRA_MESSAGE,city_id);
        map_intent.putExtra(EXTRA_NAME,name);
        map_intent.putExtra(EXTRA_CRIME,crimeXML);
        map_intent.putExtra(EXTRA_CITYNAME,city_name);
        startActivity(map_intent);
    }

    public void tweets_method(View view){
        Intent tweets_intent = new Intent(this,TweetsActivity.class);
        tweets_intent.putExtra(EXTRA_NAME,name);
        tweets_intent.putExtra(EXTRA_MESSAGE,city_id);
        tweets_intent.putExtra(EXTRA_CRIME,crimeXML);
        tweets_intent.putExtra(EXTRA_CITYNAME,city_name);
        startActivity(tweets_intent);
    }
}
