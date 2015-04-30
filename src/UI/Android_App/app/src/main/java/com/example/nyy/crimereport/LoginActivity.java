package com.example.nyy.crimereport;

import com.example.nyy.crimereport.model.AuthanticateUsr;
import com.example.nyy.crimereport.parsers.Login_XMLParser;
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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "com.example.nyy.test.MESSAGE";
    private EditText usrname = null;
    private EditText password = null;

    private UserLoginTask task;
    private boolean login_result=false;

    private String name;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usrname = (EditText)findViewById(R.id.user_name);
        password = (EditText)findViewById(R.id.password);
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


    public void login_method(View view){
        name = usrname.getText().toString();
        String pwd = password.getText().toString();
        String url = "http://52.11.140.62/cipwebservice/service.svc/AuthenticateUser?uname="+name+"&pwd="+pwd;
        if (isOnline()) {
            requestData(url);

        } else {
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
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

    private void requestData(String uri) {
        task = new UserLoginTask(this);
        task.execute(uri);
    }
    public class UserLoginTask extends AsyncTask<String, String, String> {
        private Activity activity;

        public UserLoginTask(Activity activity) {
            this.activity = activity;
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.

            String content = HttpManager.getData(params[0]);
            return content;



        }

        @Override
        protected void onPostExecute(String result) {
            AuthanticateUsr auth = new AuthanticateUsr();
            auth = Login_XMLParser.parseFeed(result);
            login_result = auth.get_result();


            if (login_result) {
                Toast.makeText(getApplicationContext(), "Redirecting...",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, ReportActivity.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                activity.startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Wrong Credentials",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}
