package com.example.nyy.crimereport;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * Created by nyy on 4/2/15.
 */
public class HttpManager {

    public static String getData(String uri) {
        AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
        HttpGet request = new HttpGet(uri);
        HttpResponse response;

        try {
            response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.close();
        }
    }
}
