package com.yjkim.comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.yjkim.board.BoardActivity;
import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.dugout.ViewPagerActivity;
import com.yjkim.util.DateTimeConvertor;
import com.yjkim.util.ViewManager;
import com.yjkim.util.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jehyeok on 2/11/15.
 */
public class CommentListFragment extends Fragment {
    private View rootView;
    private LinearLayout commentListView;
    private List<Comment> commentList = new ArrayList<Comment>();
    private String result;
    private String boardId;
    private Toast toast;
    private ImageLoader mImageLoader = VolleySingleton.getInstance().getImageLoader();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);
        commentListView = (LinearLayout) rootView.findViewById(R.id.commentListView);
        result = getArguments().getString("result");
        boardId = getArguments().getString("boardId");

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONObject(result).getJSONArray("ordered_comments");
        } catch (Exception e1) {
            Log.e("UsersFragment", "JSON 변환 중 에러");
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                ArrayList<String> imageNames = new ArrayList<String>();
                JSONArray jarray = jsonObject.getJSONArray("image_names");

                for (int j = 0; j < jarray.length(); j++) {
                    imageNames.add(jarray.get(j).toString());
                }

                commentList.add(new Comment(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("depth"),
                                jsonObject.getString("updated_at"),
                                jsonObject.getString("content"),
                                jsonObject.getString("user_nick_name"),
                                imageNames
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("CommentListFrag", "comments.size: " + commentList.size());

        for (final Comment comment : commentList) {
            Log.d("CommentListFrag", "comment.nicknamee:" + comment.getNickName());
            LayoutInflater rowInflater = null;
            rowInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View commentRow = rowInflater.inflate(R.layout.row_comment, null);

            final TextView commentContent = (TextView) commentRow.findViewById(R.id.commentContent);
            TextView commentNickName = (TextView) commentRow.findViewById(R.id.commentNickName);
            TextView commentUpdatedAt = (TextView) commentRow.findViewById(R.id.commentUpdatedAt);
            ImageView replyArrow = (ImageView) commentRow.findViewById(R.id.replyArrow);

//					공지 여부 설정
            commentContent.setText(comment.getContent().trim().replaceAll("<br/>", "\n"));
            commentNickName.setText(comment.getNickName().trim());
//					날짜 설정 !
            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'+09:00'";
            DateTimeConvertor dtc = new DateTimeConvertor(comment.getUpdatedAt(), format);
            if (dtc.isToday()) {
                commentUpdatedAt.setText(dtc.getTime());
            } else {
                commentUpdatedAt.setText(dtc.getDate());
            }

//            댑스가 있다면 화살표 표시
            if (comment.getDepth() == 0) {
                replyArrow.setVisibility(View.GONE);
            }

//            대댓글 달기
            TextView replyBtn = (TextView) commentRow.findViewById(R.id.replyBtn);
            replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BoardActivity) getActivity()).setCommentParent(comment.getId());
                    ((BoardActivity) getActivity()).focusCommentField();
                }
            });

//            사진 올리기
            final LinearLayout imagesWrapper = (LinearLayout) commentRow.findViewById(R.id.imagesWrapper);

            //                    이미지 세팅
            ArrayList<String> imageNames = comment.getImageNames();
            imagesWrapper.removeAllViews();

            if (imageNames.size() != 0) {
                imagesWrapper.setVisibility(View.VISIBLE);
            }

            final ArrayList<String> imageNamesList = new ArrayList<String>();
            for (int i = 0; i < imageNames.size(); i++) {
                final ImageView niv = new ImageView(getActivity());
                imageNamesList.add(imageNames.get(i).toString());
                String imageUrl = MyApplication.host + "/assets/" + imageNames.get(i);
                mImageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            niv.setImageBitmap(response.getBitmap());
                            imagesWrapper.addView(niv);
                            scaleImage(niv);
                            niv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent viewPagerActivity = new Intent(getActivity(), ViewPagerActivity.class);
                                    viewPagerActivity.putStringArrayListExtra("imageNames", imageNamesList);
                                    startActivity(viewPagerActivity);
                                }
                            });
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }

            commentListView.addView(commentRow);
        }

        return rootView;
    }

    private void scaleImage(ImageView niv) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) niv.getLayoutParams();
        lp.width = ViewManager.dpToPx(100);
        lp.height = ViewManager.dpToPx(100);
        niv.setScaleType(ImageView.ScaleType.FIT_XY);
        int margin = ViewManager.dpToPx(10);
        lp.setMargins(margin, margin, margin, margin);
        niv.setLayoutParams(lp);
    }

}
