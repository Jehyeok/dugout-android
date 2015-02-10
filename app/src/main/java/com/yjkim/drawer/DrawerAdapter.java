package com.yjkim.drawer;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yjkim.board.Board;
import com.yjkim.dugout.R;
import com.yjkim.util.DateTimeConvertor;
import com.yjkim.util.TeamMapper;

import java.util.List;

/**
 * Created by jehyeok on 2/10/15.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerElement> {

    private List<DrawerElement> drawerElements;

    public DrawerAdapter(Context context, int resource, List<DrawerElement> drawerElements) {
        super(context, resource, drawerElements);
        this.drawerElements = drawerElements;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cv = convertView;

        if (cv == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cv = layoutInflater.inflate(R.layout.row_drawer_element, parent, false);
        }

        DrawerElement drawerElement = drawerElements.get(position);
//		Log.d("BoardListAdapter", "board: " + board.getTitle());
        if (drawerElement != null) {
            ImageView emblem = (ImageView) cv.findViewById(R.id.emblem);
            TextView teamName = (TextView) cv.findViewById(R.id.teamName);

            emblem.setImageResource(TeamMapper.getInstance().getImageNumber(drawerElement.getGroupNumber()));
            teamName.setText(TeamMapper.getInstance().getTeamName(drawerElement.getGroupNumber()));
        }
        return cv;
    }
}
