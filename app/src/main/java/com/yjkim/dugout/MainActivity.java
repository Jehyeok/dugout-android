package com.yjkim.dugout;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.yjkim.board.Board;
import com.yjkim.board.BoardListFragment;
import com.yjkim.drawer.DrawerAdapter;
import com.yjkim.drawer.DrawerElement;
import com.yjkim.user.MyInfoFragment;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private View selectedButton;
    private DrawerAdapter drawerAdapter;
    private List<DrawerElement> drawerElementList;

    private ImageButton getBoardsBtn;
    private ImageButton getPopularBtn;
    private ImageButton getMyInfoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBoardsBtn = (ImageButton) findViewById(R.id.getBoardsBtn);

        getBoardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                BoardListFragment fragment = new BoardListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("groupNumber", MyApplication.favoriteGroupNumber + "");
                fragment.setArguments(bundle);
                changeFragment(button, fragment);
            }
        });

        getPopularBtn = (ImageButton) findViewById(R.id.getPopularBtn);

        getPopularBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                BoardListFragment fragment = new BoardListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", "/popular");
                fragment.setArguments(bundle);
                changeFragment(button, fragment);
            }
        });

        getMyInfoBtn = (ImageButton) findViewById(R.id.getMyInfoBtn);
        getMyInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                MyInfoFragment fragment = new MyInfoFragment();
                changeFragment(button, fragment);
            }
        });

    //        게시판이 첫 페이지
        getBoardsBtn.performClick();

//        드로우워 세팅
        drawerElementList = new ArrayList<DrawerElement>();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

        drawerAdapter = new DrawerAdapter(this, R.layout.row_drawer_element, drawerElementList);
        drawerListView.setAdapter(drawerAdapter);
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        loadData(MyApplication.host + "groups", "GET");
    }

    private void changeFragment(View button, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        setButtonState(button);
    }

    private void setButtonState(View button) {
        if (selectedButton != null) selectedButton.setSelected(false);
        button.setSelected(true);
        selectedButton = button;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                default:
                    BoardListFragment fragment = new BoardListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("groupNumber", position + "");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    drawerLayout.closeDrawers();
            }
        }
    }

    private void loadData(String url, String method) {
        drawerElementList.clear();
        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, MainActivity.this);
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(result);
                }
                catch (	Exception e1) {
                    Log.e("MainActivity", "JSON 변환 중 에러");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        drawerElementList.add(new DrawerElement(
                                jsonObject.getString("name"),
                                jsonObject.getInt("number")
                        ));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("MainActivity", "drawerElementSize: " + drawerElementList.size());

                for (DrawerElement drawerElement : drawerElementList) {
//                    Log.d("BoardListFrag", "board.title: " + board.getTitle());
                }
                drawerAdapter.notifyDataSetChanged();
            }
        });
        asyncTask.setUrl(url);
        asyncTask.execute();
    }
}
