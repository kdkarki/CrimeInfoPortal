package com.example.nyy.crimereport;

import com.example.nyy.crimereport.model.City;
import com.example.nyy.crimereport.model.State;
import com.example.nyy.crimereport.parsers.XMLParser;
import com.example.nyy.crimereport.model.Usr_info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SignupActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    TextView name;
    List<MyTask> tasks;

    List<State> states;
    List<City> cities;

    Spinner city_Spinner;
    Spinner state_Spinner;
    ArrayList<String> state_options = new ArrayList<String>();
    ArrayList<String> city_options = new ArrayList<String>();

    State state_opt = new State();
    City city_opt = new City();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (TextView) findViewById(R.id.name_input);

        city_Spinner = (Spinner) findViewById(R.id.spinner_city);
        state_Spinner = (Spinner) findViewById(R.id.spinner_state);

        tasks = new ArrayList<>();

        if (isOnline()) {
            requestData("http://52.11.140.62/cipwebservice/service.svc/" +
                    "GetStatesByCountryCode?countrycode=us");

        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
        }

        ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, city_options);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_Spinner.setAdapter(city_adapter);

        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, state_options);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_Spinner.setAdapter(state_adapter);

        city_Spinner.setOnItemSelectedListener(this);

        //state_Spinner.setOnItemSelectedListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		/*if (item.getItemId() == R.id.action_get_data) {
			if (isOnline()) {
				requestData("http://173.79.11.137:8282/cipwebservice/service.svc/GetStatesByCountryCode?countrycode=us");
			} else {
				Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
			}
		}*/
        return false;
    }


    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
        MyTask2 task2 = new MyTask2();
        task2.execute("http://52.11.140.62/cipwebservice/service.svc/GetCitiesByCountryCodeStateCode?countrycode=us&statecode=va");
    }

    private void resetData(String uri) {
        MyTask2 task_new = new MyTask2();
        task_new.execute(uri);
    }

	/*protected void updateDisplay() {

        for (State flower:flowerList) {
            output.append(flower.getName() + "\n");
        }
	}*/

    private void getStates() {
        for (State state : states) {
            state_options.add(state.getName());
            //output.append(state.getName() + "\n");
        }
    }

    private void getCities() {
        city_options.clear();
        for (City city : cities) {
            city_options.add(city.getName());
        }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selected_city = city_options.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //updateDisplay("Starting task");

        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            states = XMLParser.parseFeed(result);
            getStates();


        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }

    }

    private class MyTask2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            cities = XMLParser.parseFeed_city(result);
            getCities();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }

    }
}
