package com.yjkim.dugout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String cookie = MyApplication.preferences.getString("_dugout_session", "");

        if ("".equals(cookie)) {
//            인증 안했으면 인증 페이지로
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivityForResult(loginActivity, 0);
        } else {
//            인증 했으면 바로 리스트 페이지
            Intent teamSelectActivity = new Intent(this, TeamSelectActivity.class);
            startActivity(teamSelectActivity);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (resCode == RESULT_OK) {
            Intent teamSelectActivity = new Intent(this, TeamSelectActivity.class);
            startActivity(teamSelectActivity);
        } else if (resCode == RESULT_CANCELED) {
            finish();
        }
    }
}
