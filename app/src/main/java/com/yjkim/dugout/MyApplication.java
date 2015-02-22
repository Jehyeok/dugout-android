package com.yjkim.dugout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.yjkim.util.TeamMapper;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;

/**
 * Created by jehyeok on 2/6/15.
 */
public class MyApplication extends Application {
//    public static final String host = "http://54.148.125.13:3000/";
//    public static final String host = "http://10.0.3.2:3000/";
	public static final String host = "http://222.116.21.42:3000/";
    public static SharedPreferences preferences;
    public static Context mAppContext;
    public static DefaultHttpClient httpClient = new DefaultHttpClient();
    public static BasicHttpContext localContext = new BasicHttpContext();
    public static BasicCookieStore cookieStore = (BasicCookieStore) httpClient.getCookieStore();
    public static String favoriteGroupNumber;
    public static String selectedGroupNumber;

    @Override
    public void onCreate() {
        this.setAppContext(getApplicationContext());
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Log.d("MyApplication", "onCreate call");
        favoriteGroupNumber = preferences.getString("favoriteGroupNumber", "0");
        selectedGroupNumber = favoriteGroupNumber;
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
