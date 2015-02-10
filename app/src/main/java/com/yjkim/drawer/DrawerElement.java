package com.yjkim.drawer;

/**
 * Created by jehyeok on 2/10/15.
 */
public class DrawerElement {
    private String teamName;

    private Integer groupNumber;

    public DrawerElement(String teamName, Integer groupNumber) {
        this.teamName = teamName;
        this.groupNumber = groupNumber;
    }

    public String getTeamName() {
        return teamName;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }
}