package com.yjkim.board;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yjkim.dugout.R;
import com.yjkim.util.DateTimeConvertor;

import java.util.List;

/**
 * Created by jehyeok on 2/9/15.
 */
public class BoardListAdapter extends ArrayAdapter<Board> {
    private List<Board> boards;

    public BoardListAdapter(Context context, int resource, List<Board> boards) {
        super(context, resource, boards);
        this.boards = boards;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cv = convertView;

        if (cv == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cv = layoutInflater.inflate(R.layout.row_board, parent, false);
        }

        Board board = boards.get(position);
//		Log.d("BoardListAdapter", "board: " + board.getTitle());
        if (board != null) {
            TextView boardTitle = (TextView) cv.findViewById(R.id.boardTitle);
            TextView boardNickName = (TextView) cv.findViewById(R.id.boardNickName);
            TextView boardUpdatedAt = (TextView) cv.findViewById(R.id.boardUpdatedAt);
            TextView boardCount = (TextView) cv.findViewById(R.id.boardCount);
            TextView boardLike = (TextView) cv.findViewById(R.id.boardLike);

            String titleText = "";

//			공지 여부 설정
            if (board.isNotice()) {
                titleText += "<font color=#A776B5>[공지] </font>";
                cv.setBackgroundColor(Color.parseColor("#e6e6e6"));
            } else {
                cv.setBackgroundColor(Color.WHITE);
            }
            titleText += board.getTitle();
            Log.d("BoardListAdapter", "commentLength: " + board.getCommentSize());
            titleText += " <font color=#3daed5>[" + board.getCommentSize().toString().trim() + "]";
            boardTitle.setText(Html.fromHtml(titleText));
            boardNickName.setText(board.getNickName().trim());
//			날짜 설정 !
            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'+09:00'";
            DateTimeConvertor dtc = new DateTimeConvertor(board.getUpdatedAt(), format);
            Log.d("BoardListAdapter", "updatedAt: " + board.getUpdatedAt());
            if (dtc.isToday()) {
                boardUpdatedAt.setText(dtc.getTime());
            } else {
                boardUpdatedAt.setText(dtc.getDate());
            }
            boardCount.setText("조회 " + board.getCount());
            boardLike.setText("추천 " + board.getLike());

//            cv.setTag(board.getId());
        }
        return cv;
    }
}
