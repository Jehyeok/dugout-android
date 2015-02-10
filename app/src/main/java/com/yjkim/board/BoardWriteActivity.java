package com.yjkim.board;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;

public class BoardWriteActivity extends ActionBarActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        Button writeBtn = (Button) findViewById(R.id.boardWriteWriteBtn);

        final EditText title = (EditText) findViewById(R.id.boardWriteTitle);
        final EditText content = (EditText) findViewById(R.id.boardWriteContent);

        writeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = MyApplication.host + "groups/" + MyApplication.selectedGroupNumber + "/boards";
                String method = "POST";

                AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, BoardWriteActivity.this);
                asyncTask.setListener(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String result) {
                        if ("success".equals(result.trim())) {
//                            글쓰기 성공
                            setResult(RESULT_OK);
                            finish();
                        } else {
//                            글쓰기 실패
                            toast = Toast.makeText(getApplicationContext(), "글쓰기 실패했습니다", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
                asyncTask.setUrl(url);
//                파라미터 세팅
                asyncTask.execute(
                        "title: " + title.getText().toString(),
                        "content: " + content.getText().toString()
                );
            }
        });

        ImageButton boardDetailBackBtn = (ImageButton) findViewById(R.id.boardDetailBackBtn);
        boardDetailBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }
}
