package com.yjkim.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yjkim.dugout.MainActivity;
import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;
import com.yjkim.util.TeamMapper;
import com.yjkim.util.ViewManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TeamSelectActivity extends ActionBarActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_select);

        String url = MyApplication.host + "groups";
        String method = "GET";

        final ArrayList<View> views = ViewManager.getViewsByTag((ViewGroup) findViewById(R.id.teamWrapper), "teamImageBtn");
//        Log.d("TeamSelectActivity", "views.size: " + views.size());
//        for (View v : views) {
//            Log.d("TeamSelectActivity", "tag: " + v.getTag());
//        }
        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, TeamSelectActivity.this);
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(result);
                }
                catch (	Exception e1) {
                    Log.e("UsersFragment", "JSON 변환 중 에러");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ImageButton teamImageBtn = (ImageButton) views.get(i);
                        int teamNumber = jsonObject.getInt("number");
                        teamImageBtn.setImageResource(TeamMapper.getInstance().getImageNumber(teamNumber));
//                        팀 넘버
                        teamImageBtn.setTag(teamNumber);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        asyncTask.setUrl(url);
//                파라미터 세팅
        asyncTask.execute();
    }

    public void teamBtnClick(final View v) {
        Log.d("TeamSelectActivity", "teamNumber: " + v.getTag());

        String url = MyApplication.host + "groups/select";
        String method = "POST";

        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, TeamSelectActivity.this);
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                if ("success".equals(result.trim())) {
                    // 좋아하는 구단 팀 선택
                    SharedPreferences.Editor preferencesEditor = MyApplication.preferences.edit();
                    preferencesEditor.putString("favoriteGroupNumber", v.getTag() + "");
                    MyApplication.favoriteGroupNumber = v.getTag() + "";
                    preferencesEditor.commit();

                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);
                } else {
                    toast = Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        asyncTask.setUrl(url);
//                파라미터 세팅
        asyncTask.execute("group_number: " + v.getTag());
    }
}
