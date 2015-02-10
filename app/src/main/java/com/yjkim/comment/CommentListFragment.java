package com.yjkim.comment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjkim.dugout.R;
import com.yjkim.util.DateTimeConvertor;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);
        commentListView = (LinearLayout) rootView.findViewById(R.id.commentListView);
        result = getArguments().getString("result");

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONObject(result).getJSONArray("comments");
        } catch (Exception e1) {
            Log.e("UsersFragment", "JSON 변환 중 에러");
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                commentList.add(new Comment(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("depth"),
                                jsonObject.getString("updated_at"),
                                jsonObject.getString("content"),
                                jsonObject.getString("user_nick_name")
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("CommentListFrag", "comments.size: " + commentList.size());

        for (Comment comment : commentList) {
            Log.d("CommentListFrag", "comment.nicknamee:" + comment.getNickName());
            LayoutInflater rowInflater = null;
            rowInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View commentRow = rowInflater.inflate(R.layout.row_comment, null);

            TextView commentContent = (TextView) commentRow.findViewById(R.id.commentContent);
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

            commentListView.addView(commentRow);
        }

        return rootView;
    }
}
