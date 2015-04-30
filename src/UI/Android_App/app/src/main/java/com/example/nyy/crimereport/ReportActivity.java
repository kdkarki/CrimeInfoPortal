package com.example.nyy.crimereport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nyy.crimereport.model.Address;
import com.example.nyy.crimereport.model.Crime;
import com.example.nyy.crimereport.parsers.Address_XMLParser;
import com.example.nyy.crimereport.parsers.Crime_XMLParser;

import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends ActionBarActivity {
    List<Address> addresses = new ArrayList<>();
    TextView report;
    List<Crime> crimes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        report = (TextView)findViewById(R.id.report_text);
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        String usrname_url = "http://52.11.140.62/cipwebservice/service.svc/" +
                "GetUserAddressByUserName?uname=" + message;
        report.setText("\n");
        if (isOnline()) {
            requestData(usrname_url);

        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

    private void get_city_id(){
        //for(Address address:addresses) {
        //city_ids.add(address.get_city_id());

    }

    public class ReportTask extends AsyncTask<String, String, String> {
        private Activity activity;

        public ReportTask(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(String... params) {

            // TODO: retrieve city_id by usr_name.
            String content = HttpManager.getData(params[0]);
            Address address = Address_XMLParser.parseFeed(content);
            String city_id = Integer.toString(address.get_city_id());

            // TODO: retrieve crime information by city_id.
            String report_uri = "http://52.11.140.62/cipwebservice/service.svc/GetCriminalRecordsByCityId?cityid=" + city_id;
            String report_xml = HttpManager.getData(report_uri);
            return report_xml;
        }

        @Override
        protected void onPostExecute(String result) {

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
}
