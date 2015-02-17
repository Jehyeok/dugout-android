package com.yjkim.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yjkim.comment.CommentListFragment;
import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.DateTimeConvertor;
import com.yjkim.util.OnTaskCompleted;
import com.yjkim.util.ViewManager;
import com.yjkim.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardActivity extends ActionBarActivity {

    private Toast toast;
    private Bundle extras;
    private PullToRefreshScrollView boardDetailScrollView;
    private Integer parentCommentId = null;
    private Button writeCommentBtn;
    private EditText commentContent;
    private Button closeCommentContent;
    private LinearLayout contentImagesWrapper;
    private ImageLoader mImageLoader = VolleySingleton.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        contentImagesWrapper = (LinearLayout) findViewById(R.id.contentImagesWrapper);

        extras = getIntent().getExtras();

        ImageButton boardDetailBackBtn = (ImageButton) findViewById(R.id.boardDetailBackBtn);
        boardDetailBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        댓글 달기
        writeCommentBtn = (Button) findViewById(R.id.writeCommentBtn);
        commentContent = (EditText) findViewById(R.id.commentContent);
        closeCommentContent = (Button) findViewById(R.id.closeCommentContent);

        commentContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                focuseCommentField();
            }
        });

        closeCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didWriteComment();
            }
        });

        writeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = MyApplication.host + "boards/" + extras.getString("boardId") + "/comments";
                String method = "POST";

                AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, BoardActivity.this);
                asyncTask.setListener(new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(String result) {
                        if ("fail".equals(result.trim())) {
//                            댓글쓰기 실패
                            toast = Toast.makeText(getApplicationContext(), "댓글달기에 실패했습니다", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
//                            댓글쓰기 성공
                            didWriteComment();
                            loadData(MyApplication.host + "groups/0/boards/" + extras.getString("boardId"), "GET");
                        }
                    }
                });
                asyncTask.setUrl(url);
//                파라미터 세팅
                asyncTask.execute(
                        "content: " + commentContent.getText().toString(),
                        "comment_parent_id: " + (parentCommentId != null ? parentCommentId : "")
                );
            }
        });

//        pullToReresh

        boardDetailScrollView = (PullToRefreshScrollView) findViewById(R.id.boardDetailScrollView);
        boardDetailScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> scrollViewPullToRefreshBase) {
                loadData(MyApplication.host + "groups/0/boards/" + extras.getString("boardId"), "GET");
            }
        });

//        안타, 아웃
        LinearLayout like = (LinearLayout) findViewById(R.id.like);
        LinearLayout disLike = (LinearLayout) findViewById(R.id.disLike);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrDisklike(MyApplication.host + "boards/" + extras.getString("boardId") + "/like");
            }
        });

        disLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrDisklike(MyApplication.host + "boards/" + extras.getString("boardId") + "/dislike");
            }
        });

        //        키보드 숨기기
//        ViewManager.setupUI(findViewById(R.id.boardActivityLayout), BoardActivity.this);

//        게시글 & 댓글 로드
        loadData(MyApplication.host + "groups/0/boards/" + extras.getString("boardId"), "GET");
    }

    private void didWriteComment() {
        parentCommentId = null;
        commentContent.setText("");
        commentContent.clearFocus();
        ViewManager.hideSoftKeyboard(this);
        closeCommentContent.setVisibility(View.INVISIBLE);
    }

    public void likeOrDisklike(String url) {
        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, "POST", BoardActivity.this);
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                if ("success".equals(result.trim())) {
//                    좋아요 성공
                    loadData(MyApplication.host + "groups/0/boards/" + extras.getString("boardId"), "GET");
                } else {
                    //                            좋아요 실패
                    toast = Toast.makeText(getApplicationContext(), result.trim(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        asyncTask.setUrl(url);
//                파라미터 세팅
        asyncTask.execute();
    }

    public void loadData(String url, String method) {
//        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, getApplicationContext());
        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, BoardActivity.this);
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
//                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                } catch (Exception e1) {
                    Log.e("MainActivity", "JSON 변환 중 에러");
                }

                try {
                    TextView boardDetailTitle = (TextView) findViewById(R.id.boardDetailTitle);
                    TextView boardDetailNickName = (TextView) findViewById(R.id.boardDetailNickName);
                    TextView boardDetailLike = (TextView) findViewById(R.id.boardDetailLike);
                    TextView boardDetailCount = (TextView) findViewById(R.id.boardDetailCount);
                    TextView boardDetailUpdatedAt = (TextView) findViewById(R.id.boardDetailUpdatedAt);
                    TextView boardDetailContent = (TextView) findViewById(R.id.boardDetailContent);
                    TextView likeCount = (TextView) findViewById(R.id.likeCount);
                    TextView dislikeCount = (TextView) findViewById(R.id.dislikeCount);

                    String titleText = "";

                    //		공지 여부 설정
                    if (jsonObject.getString("level").equals("1"))
                        titleText += "<font color=#A776B5>[공지] </font>";
                    titleText += jsonObject.getString("title");
//                        댓글 개수 설정
                    titleText += " <font color=#3daed5>[" + jsonObject.getString("comment_size") + "]";
                    boardDetailTitle.setText(Html.fromHtml(titleText));
//                        닉네임 세팅
                    boardDetailNickName.setText(jsonObject.getString("user_nick_name").trim());
//                        추천 세팅
                    boardDetailLike.setText("추천 " + jsonObject.getString("like"));
                    //		날짜 설정 !
                    String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'+'";
                    DateTimeConvertor dtc = new DateTimeConvertor(jsonObject.getString("updated_at"), format);
                    if (dtc.isToday()) {
                        boardDetailUpdatedAt.setText(dtc.getTime());
                    } else {
                        boardDetailUpdatedAt.setText(dtc.getDate());
                    }
                    boardDetailCount.setText("조회 " + jsonObject.getString("count"));

                    //		내용 설정
                    boardDetailContent.setText(jsonObject.getString("content").trim().replaceAll("<br/>", "\n"));

                    likeCount.setText("안타 " + jsonObject.getString("like"));
                    dislikeCount.setText("아웃 " + jsonObject.getString("dislike"));

//                    댓글 설정
                    CommentListFragment fragment = new CommentListFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.commentContainer, fragment).commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", result);
                    bundle.putString("boardId", extras.getString("boardId"));
                    fragment.setArguments(bundle);

//                    이미지 세팅
                    JSONArray imageNames = jsonObject.getJSONArray("image_names");
                    contentImagesWrapper.removeAllViews();
                    for (int i = 0; i < imageNames.length(); i++) {
                        final ImageView niv = new ImageView(BoardActivity.this);
                        String imageUrl = MyApplication.host + "/assets/" + imageNames.get(i);
                        mImageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                                if (response.getBitmap() != null) {
                                    niv.setImageBitmap(response.getBitmap());
                                    contentImagesWrapper.addView(niv);
                                    scaleImage(niv);
                                }
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }

                    boardDetailScrollView.onRefreshComplete();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        asyncTask.setUrl(url);
        asyncTask.execute();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void setCommentParent(Integer id) {
        this.parentCommentId = id;
    }

    public void focuseCommentField() {
        closeCommentContent.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentContent, InputMethodManager.SHOW_IMPLICIT);
    }

    private void scaleImage(ImageView view) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();

        if (drawing == null) {
            return; // Checking for null & return, as suggested in comments
        }

        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//        int bounding = dpToPx(250);
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int bounding = metrics.widthPixels;

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.i("Test", "scaled width = " + Integer.toString(width));
        Log.i("Test", "scaled height = " + Integer.toString(height));

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.bottomMargin = 20;
        view.setLayoutParams(params);
    }
}
