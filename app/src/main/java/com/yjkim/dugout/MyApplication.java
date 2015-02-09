package com.yjkim.dugout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
	public static final String host = "http://121.131.96.156:3000/";
    public static final String groupNumber = "0";
    public static SharedPreferences preferences;
    public static Context mAppContext;
    public static DefaultHttpClient httpClient = new DefaultHttpClient();
    public static BasicHttpContext localContext = new BasicHttpContext();
    public static BasicCookieStore cookieStore = (BasicCookieStore) httpClient.getCookieStore();

    @Override
    public void onCreate() {
        this.setAppContext(getApplicationContext());
        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Log.d("MyApplication", "onCreate call");

//        팀 이미지 drawable 세팅

        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_0);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_1);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_2);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_3);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_4);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_5);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_6);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_7);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_8);
        TeamMapper.teamDrawableNumberList.add(R.drawable.ic_9);
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}
