package com.yjkim.util;

import com.yjkim.dugout.R;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jehyeok on 2/8/15.
 */
public class TeamMapper {
    private static final TeamMapper teamMapper = new TeamMapper();
    private ArrayList<Integer> teamDrawableWaterNumberList = new ArrayList<Integer>();

    private ArrayList<Integer> teamDrawableNumberList = new ArrayList<Integer>();

    private HashMap<String, String> teamNameMap = new HashMap<String, String>();

    public static TeamMapper getInstance() {
        return teamMapper;
    }

    private TeamMapper() {
//        이미지 넘버 세팅
        teamDrawableNumberList.add(R.drawable.ic_0);
        teamDrawableNumberList.add(R.drawable.ic_1);
        teamDrawableNumberList.add(R.drawable.ic_2);
        teamDrawableNumberList.add(R.drawable.ic_3);
        teamDrawableNumberList.add(R.drawable.ic_4);
        teamDrawableNumberList.add(R.drawable.ic_5);
        teamDrawableNumberList.add(R.drawable.ic_6);
        teamDrawableNumberList.add(R.drawable.ic_7);
        teamDrawableNumberList.add(R.drawable.ic_8);
        teamDrawableNumberList.add(R.drawable.ic_9);
//        이미지 워터마크 세팅
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
        teamDrawableWaterNumberList.add(R.drawable.ic_water_bears_0);
//        팀네임 세팅
        teamNameMap.put("0", "베어즈");
        teamNameMap.put("1", "이글즈");
        teamNameMap.put("2", "타이거즈");
        teamNameMap.put("3", "케이티");
        teamNameMap.put("4", "자이언츠");
        teamNameMap.put("5", "트윈즈");
        teamNameMap.put("6", "엔씨");
        teamNameMap.put("7", "히어로즈");
        teamNameMap.put("8", "라이언즈");
        teamNameMap.put("9", "더블유");
    }

    public String getTeamName(Integer groupNumber) {
        return teamNameMap.get(groupNumber + "");
    }

    public Integer getImageNumber(Integer groupNumber) {
        return teamDrawableNumberList.get(groupNumber);
    }
    public Integer getWaterImageNumber(Integer groupNumber) {
        return teamDrawableWaterNumberList.get(groupNumber);
    }
}