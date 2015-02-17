package com.yjkim.board;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;
import com.yjkim.util.ViewManager;

import java.util.ArrayList;

public class BoardWriteActivity extends ActionBarActivity {

    private Toast toast;
    private LinearLayout imagesWrapper;
    private ArrayList<Uri> images = new ArrayList<Uri>();
    private ArrayList<String> params = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);

        imagesWrapper = (LinearLayout) findViewById(R.id.imagesWrapper);
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
                params.add("title: " + title.getText().toString());
                params.add("content: " + content.getText().toString());

                if (images.size() != 0) {
                    for (int i = 0; i < images.size(); i++) {
//                        ImageView iv = images.get(i);
                        //Convert bitmap to byte array
//                        Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
//                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//                        byte[] bitmapdata = bos.toByteArray();
//                        Log.d("BoardWriteActivity", "path: " + images.get(i).getPath());
                        Log.d("BoardWriteActivity", "path: " + getRealPathFromURI(images.get(i)));
//                        params.add("#file" + i + ": " + images.get(i).getPath());
                        params.add("#file" + i + ": " + getRealPathFromURI(images.get(i)));

                    }
                }
                asyncTask.execute(params.toArray(new String[params.size()]));
            }
        });

        ImageButton boardDetailBackBtn = (ImageButton) findViewById(R.id.boardDetailBackBtn);
        boardDetailBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        사진 추가 버튼
        ImageButton galleryBtn = (ImageButton) findViewById(R.id.galleryBtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 200);
            }
        });
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            RelativeLayout rl = makeImagePreview(selectedImage);
            imagesWrapper.addView(rl, 0);

//            images.add(iv);
            images.add(selectedImage);
        }
    }

    private RelativeLayout makeImagePreview(final Uri selectedImage) {
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewManager.dpToPx(100), ViewManager.dpToPx(100)));

        ImageView iv = new ImageView(this);
        rl.addView(iv);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        lp.width = ViewManager.dpToPx(100);
        lp.height = ViewManager.dpToPx(100);
        int marginPX = ViewManager.dpToPx(10);
        lp.setMargins(marginPX, marginPX, marginPX, marginPX);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setImageURI(selectedImage);
        iv.setLayoutParams(lp);

        final ImageButton ib = new ImageButton(this);
        rl.addView(ib);

        lp = (RelativeLayout.LayoutParams) ib.getLayoutParams();
        lp.width = ViewManager.dpToPx(35);
        lp.height = ViewManager.dpToPx(35);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ib.setBackgroundColor(Color.TRANSPARENT);
        ib.setImageResource(R.drawable.ic_exit);
        ib.setTag(images.size());
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(selectedImage);
                RelativeLayout rl = ((RelativeLayout) ib.getParent());
                ((ViewGroup) rl.getParent()).removeView(rl);
            }
        });

        return rl;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public String getRealPathFromURI(Uri contentUri)
    {
        // can post image
        String [] proj={MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}
