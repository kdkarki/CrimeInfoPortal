package com.example.nyy.crime_info_portal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.nyy.crime_info_portal.model.Auth;
import com.example.nyy.crime_info_portal.model.City;
import com.example.nyy.crime_info_portal.model.Info;
import com.example.nyy.crime_info_portal.model.State;
import com.example.nyy.crime_info_portal.parser.Login_XMLParser;
import com.example.nyy.crime_info_portal.parser.StateCity_XMLParser;


public class RegisterActivity extends Activity {

    public final static String EXTRA_MESSAGE = "com.example.nyy.test.MESSAGE";
    public final static String EXTRA_NAME = "com.example.nyy.test.name";
    public final static String EXTRA_CITYNAME = "com.example.nyy.test.cityname";

    EditText usrname_text;
    EditText psw_text;
    EditText zipcode_text;

    List<State> states;
    List<City> cities;

    Spinner city_Spinner;
    Spinner state_Spinner;
    ProgressBar pb1;
    ProgressBar pb2;
    List<MyTask> tasks1;
    List<MyTask2> tasks2;
    ArrayList<String> state_options = new ArrayList<String>();
    ArrayList<String> city_options = new ArrayList<String>();

    Info result = new Info();


    State state_op = new State();
    City city_opt = new City();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usrname_text = (EditText) findViewById(R.id.name_input);
        psw_text = (EditText) findViewById(R.id.password_input);
        zipcode_text = (EditText) findViewById(R.id.zip_input);

        pb1 = (ProgressBar) findViewById(R.id.progressBar1);
        pb1.setVisibility(View.INVISIBLE);
        pb2 = (ProgressBar) findViewById(R.id.progressBar1);
        pb2.setVisibility(View.INVISIBLE);

        city_Spinner = (Spinner) findViewById(R.id.spinner_city);
        state_Spinner = (Spinner) findViewById(R.id.spinner_state);

        tasks1 = new ArrayList<>();
        tasks2 = new ArrayList<>();

        if (isOnline()) {
            requestData("http://52.11.140.62/cipwebservice/service.svc/" +
                    "GetStatesByCountryCode?countrycode=us");

        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

    }

    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    private void requestCity(String uri) {
        MyTask2 task2 = new MyTask2();
        task2.execute("http://52.11.140.62/cipwebservice/service.svc/GetCitiesByCountryCodeStateCode?countrycode=us&statecode="+uri);
    }


    private void getStates() {
        for (State state : states) {
            state_options.add(state.getName());
        }
        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, state_options);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_Spinner.setAdapter(state_adapter);
        //state_Spinner.setOnItemSelectedListener(this);
        state_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state_code = states.get(position).getCode();
                requestCity(state_code);
                result.set_state_id(states.get(position).getId());
                result.set_state_name(states.get(position).getName());
                result.set_state_code(states.get(position).getCode());
                Log.d("MyTag",result.get_state_name());
                Log.d("MyTag",result.get_state_code());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
    }

    private void getCities() {
        city_options.clear();
        for (City city : cities) {
            city_options.add(city.getName());
        }
        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city_options);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_Spinner.setAdapter(city_adapter);
        city_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                result.set_city_id(cities.get(position).getId());
                result.set_city_name(cities.get(position).getName());
                Log.d("MyTag",result.get_city_name());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
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

    public void submit(View view){
        result.set_usr_name(usrname_text.getText().toString());
        result.set_password(Integer.parseInt(psw_text.getText().toString()));
        String uri_newusr = "http://52.11.140.62/cipwebservice/service.svc//CreateNewUser?"
            +"uname="+result.get_usr_name()
            +"&pwd="+result.get_password();
        RegisterTask task = new RegisterTask(this);
        task.execute(uri_newusr);


    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            if (tasks1.size() == 0) {
                pb1.setVisibility(View.VISIBLE);
            }
            tasks1.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            states = StateCity_XMLParser.parseFeed(result);
            getStates();

            tasks1.remove(this);
            if (tasks1.size() == 0) {
                pb1.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

    }

    private class MyTask2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");

            if (tasks2.size() == 0) {
                pb2.setVisibility(View.VISIBLE);
            }
            tasks2.add(this);
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            cities = StateCity_XMLParser.parseFeed_city(result);
            getCities();

            tasks2.remove(this);
            if (tasks2.size() == 0) {
                pb2.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

    }

    public class RegisterTask extends AsyncTask<String, String, String> {
        private Activity activity;

        public RegisterTask(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            HttpManager.postData(params[0]);
            String uri_addr = "http://52.11.140.62/cipwebservice/service.svc///CreateNewUserAddressByUsername?"
                    +"uname="+result.get_usr_name()
                    +"cityid="+result.get_city_id();
            HttpManager.postData(uri_addr);
            return "ok";



        }

        @Override
        protected void onPostExecute(String ok) {

            Toast.makeText(getApplicationContext(), "Redirecting...",
                 Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, ReportActivity.class);
            intent.putExtra(EXTRA_NAME,result.get_usr_name());
            intent.putExtra(EXTRA_MESSAGE,result.get_city_id());
            intent.putExtra(EXTRA_CITYNAME,result.get_city_name());
                activity.startActivity(intent);
            }

        }
}


