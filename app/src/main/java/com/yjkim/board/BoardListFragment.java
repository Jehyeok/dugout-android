package com.yjkim.board;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.network.AsyncHttpTask;
import com.yjkim.network.OnTaskCompleted;
import com.yjkim.util.TeamMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoardListFragment extends Fragment{
    private View rootView;
    private PullToRefreshListView boardListView;
    //	private ListView boardListView;
    private String groupNumber;
    private BoardListAdapter boardsAdapter;
    private AsyncHttpTask asyncTask;
    private List<Board> boardList;
    private LinearLayout navbar;
    private ImageButton drawerToggleBtn;
    private ImageButton writeBtn;
    private TextView title;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_board_list, container, false);
        this.groupNumber = getArguments().getString("groupNumber");


        drawerToggleBtn = (ImageButton) rootView.findViewById(R.id.drawerToggleBtn);
        writeBtn = (ImageButton) rootView.findViewById(R.id.writeBtn);
        title = (TextView) rootView.findViewById(R.id.title);
        navbar = (LinearLayout) rootView.findViewById(R.id.navbar);

        if (getArguments().getString("url") == null) {
            url = MyApplication.host + "groups/" + this.groupNumber + "/boards";
//            메뉴 보이게
            MyApplication.selectedGroupNumber = this.groupNumber;
            setNavBarVisibility(View.VISIBLE);
            setNavBar("게시판", View.VISIBLE);
            //        워터마크 세팅
            ImageView waterMark = (ImageView) rootView.findViewById(R.id.waterMark);
            waterMark.setBackgroundResource(TeamMapper.getInstance().getWaterImageNumber(new Integer(groupNumber)));
        } else if (getArguments().getString("url").equals("/my")){
            url = MyApplication.host + "boards" + getArguments().getString("url");
            setNavBarVisibility(View.GONE);
        } else {
            url = MyApplication.host + "boards" + getArguments().getString("url");
//            메뉴 제거
            setNavBarVisibility(View.VISIBLE);
            setNavBar("인기글", View.GONE);
        }

        final String method = "GET";

        boardList = new ArrayList<Board>();
        boardsAdapter = new BoardListAdapter(getActivity(), R.layout.row_board, boardList);

        loadBoardData(url, method);

        boardListView = (PullToRefreshListView) rootView.findViewById(R.id.boardListView);
//		boardListView = (ListView) rootView.findViewById(R.id.boardListView);

        boardListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                Log.d("BoardListFrag", "refreshed");
                loadBoardData(url, method);
            }
        });

        boardListView.setAdapter(boardsAdapter);
        boardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent boardActivity = new Intent(getActivity(), BoardActivity.class);
                Log.d("BoardListFrag", "position: " + position);
////                왜 포지션은 1부터 시작하지 ?
                Board board = boardList.get(position - 1);
                boardActivity.putExtra("boardId", board.getId().toString());

                startActivity(boardActivity);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        ImageButton writeBtn = (ImageButton) rootView.findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent boardWriteActivity = new Intent(getActivity(), BoardWriteActivity.class);
                startActivityForResult(boardWriteActivity, 0);
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        final DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawerToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        return rootView;
    }

    private void setNavBarVisibility(int visibility) {
        navbar.setVisibility(visibility);
    }

    private void setNavBar(String title, int visibility) {
        drawerToggleBtn.setVisibility(visibility);
        writeBtn.setVisibility(visibility);
        this.title.setText(title);
    }

    private void loadBoardData(String url, String method) {
//        boardList = new ArrayList<Board>();
        boardList.clear();
        asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, getActivity());
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(result);
                }
                catch (	Exception e1) {
                    Log.e("BoardListFragment", "JSON 변환 중 에러");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.d("BoardListFrag", "comment_length: " + jsonObject.getInt("comment_size"));
                        boardList.add(new Board(
                                jsonObject.getInt("id"),
                                jsonObject.getInt("like"),
                                jsonObject.getInt("dislike"),
                                jsonObject.getInt("count"),
                                jsonObject.getInt("level"),
                                jsonObject.getInt("group_id"),
                                jsonObject.getString("title"),
                                jsonObject.getString("content"),
                                jsonObject.getInt("comment_size"),
                                jsonObject.getString("user_nick_name"),
                                jsonObject.getString("updated_at")
                        ));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                boardListView.onRefreshComplete();
                for (Board board : boardList) {
                    Log.d("BoardListFrag", "board.title: " + board.getTitle());
                }
                boardsAdapter.notifyDataSetChanged();
            }
        });
        asyncTask.setUrl(url);
        asyncTask.execute();
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (resCode == getActivity().RESULT_OK) {
//            boardsAdapter.notifyDataSetChanged();
            loadBoardData(url, "GET");
        }
    }
}
