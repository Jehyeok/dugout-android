package com.yjkim.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yjkim.board.BoardListFragment;
import com.yjkim.dugout.MainActivity;
import com.yjkim.dugout.MyApplication;
import com.yjkim.dugout.R;
import com.yjkim.util.AsyncHttpTask;
import com.yjkim.util.OnTaskCompleted;
import com.yjkim.util.TeamMapper;
import com.yjkim.util.ViewManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jehyeok on 2/10/15.
 */
public class MyInfoFragment extends Fragment {
    private View rootView;
    private EditText nickName;
    private Toast toast;
    private ImageButton selectedTeamImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_my_info, container, false);
        FrameLayout myBoardsContainer = (FrameLayout) rootView.findViewById(R.id.my_boards_container);

        BoardListFragment fragment = new BoardListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", "/my");
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.my_boards_container, fragment).commit();

        loadTeamData();

//        닉네임 기본 설정
        nickName = (EditText) rootView.findViewById(R.id.nickName);
        Button changeNickName = (Button) rootView.findViewById(R.id.changeNickName);
        changeNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(nickName.getText().toString().trim())) {
                    toast = Toast.makeText(getActivity(), "변경할 닉네임을 입력해주세요", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, "POST", getActivity());
                    asyncTask.setListener(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(String result) {
                            if ("success".equals(result.trim())) {
                                nickName.setText("");
                            } else {
                                toast = Toast.makeText(getActivity(), result.trim(), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                    });
                    asyncTask.setUrl(MyApplication.host + "users/change_nick_name");
//                파라미터 세팅
                    asyncTask.execute(
                            "will_nick_name: " + nickName.getText().toString()
                    );
                }
            }
        });

        return rootView;
    }

    public void loadTeamData() {
        String url = MyApplication.host + "groups";
        String method = "GET";

        final ArrayList<View> views = ViewManager.getViewsByTag((ViewGroup) rootView.findViewById(R.id.teamWrapper), "teamImageBtn");
//        Log.d("TeamSelectActivity", "views.size: " + views.size());
//        for (View v : views) {
//            Log.d("TeamSelectActivity", "tag: " + v.getTag());
//        }
        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, getActivity());
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = new JSONArray(result);
                }
                catch (	Exception e1) {
                    Log.e("UsersFragment", "JSON 변환 중 에러");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ImageButton teamImageBtn = (ImageButton) views.get(i);
                        int teamNumber = jsonObject.getInt("number");
                        teamImageBtn.setImageResource(TeamMapper.getInstance().getImageNumber(teamNumber));
//                        팀 넘버
                        teamImageBtn.setTag(teamNumber);

                        if ((teamNumber + "").equals(MyApplication.favoriteGroupNumber)) {
                            teamImageBtn.setBackgroundColor(Color.rgb(180, 180, 180));
                            selectedTeamImageButton = teamImageBtn;
                        }

                        teamImageBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                teamBtnClick(v);
                            }
                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        asyncTask.setUrl(url);
//                파라미터 세팅
        asyncTask.execute();
    }

    public void teamBtnClick(final View v) {
        Log.d("TeamSelectActivity", "teamNumber: " + v.getTag());

        String url = MyApplication.host + "groups/select";
        String method = "POST";

        AsyncHttpTask asyncTask = new AsyncHttpTask(MyApplication.httpClient, method, getActivity());
        asyncTask.setListener(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(String result) {
                if ("success".equals(result.trim())) {
                    // 좋아하는 구단 팀 선택
                    SharedPreferences.Editor preferencesEditor = MyApplication.preferences.edit();
                    preferencesEditor.putString("favoriteGroupNumber", v.getTag() + "");
                    selectedTeamImageButton.setBackgroundColor(Color.TRANSPARENT);
                    v.setBackgroundColor(Color.rgb(180, 180, 180));
                    selectedTeamImageButton = (ImageButton) v;
                    MyApplication.favoriteGroupNumber = v.getTag() + "";
                    preferencesEditor.commit();
                } else {
                    toast = Toast.makeText(getActivity(), result.trim(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        asyncTask.setUrl(url);
//                파라미터 세팅
        asyncTask.execute("group_number: " + v.getTag());
    }
}
