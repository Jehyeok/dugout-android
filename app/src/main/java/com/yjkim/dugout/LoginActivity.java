package com.yjkim.dugout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;

import org.apache.http.cookie.Cookie;

import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        그냥 뒤로가기 눌렀을 때 앱 종료
        setResult(RESULT_CANCELED);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) (EditText) findViewById(R.id.password);
        Button signinBtn = (Button) findViewById(R.id.signinBtn);
        Button signupBtn = (Button) findViewById(R.id.signupBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MyApplication.host + "users/signin";
                String method = "POST";

                AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, LoginActivity.this);
                asyncTask.setListener(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String result) {
                        if (("success").equals(result.trim())) {
//                            인증 성공
//                            쿠키 저장 ( 세션 연동 )
                            SharedPreferences.Editor preferencesEditor = MyApplication.preferences.edit();
                            List<Cookie> cookies = MyApplication.cookieStore.getCookies();

                            if (!cookies.isEmpty()) {
                                for (int i = 0; i < cookies.size(); i++) {
                                    // Save cookie
                                    Cookie cookie = cookies.get(i);
                                    Log.i("LoginActivity", "name: " + cookie.getName() + ", value: " + cookie.getValue());
                                    preferencesEditor.putString(cookie.getName(), cookie.getValue());
                                }
                            }
                            preferencesEditor.commit();
                            setResult(RESULT_OK);
                            finish();
                        } else {
//                            인증 실패
                            toast = Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
                asyncTask.setUrl(url);
//                파라미터 세팅
                asyncTask.execute(
                        "email: " + email.getText().toString(),
                        "password: " + password.getText().toString()
                );
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity = new Intent(v.getContext(), SignupActivity.class);
                startActivity(signupActivity);
            }
        });
    }
}
