package com.yjkim.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yjkim.dugout.R;

/**
 * Created by jehyeok on 2/10/15.
 */
public class MyInfoFragment extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_my_info, container, false);
        return rootView;
    }
}
