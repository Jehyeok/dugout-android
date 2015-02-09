package com.yjkim.dugout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;


public class SignupActivity extends ActionBarActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText nickName = (EditText) findViewById(R.id.nickName);

        Button signupBtn = (Button) findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MyApplication.host + "users/signup";
                String method = "POST";

                AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, SignupActivity.this);
//                AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, getApplicationContext());
                asyncTask.setListener(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String result) {
                        if ("success".equals(result.trim())) {
//                            가입 성공
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            builder.setMessage("가입이 완료되었습니다");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
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
                        "password: " + password.getText().toString(),
                        "nick_name: " + nickName.getText().toString()
                );
            }
        });
    }
}
