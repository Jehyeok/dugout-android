package com.yjkim.comment;

import java.util.ArrayList;

/**
 * Created by jehyeok on 2/11/15.
 */
public class Comment {
    private Integer id;

    private Integer depth;

    private String updatedAt;

    private String content;

    private String nickName;

    private ArrayList<String> imageNames = new ArrayList<String>();

    public Comment(Integer id, Integer depth, String updatedAt, String content, String nickName, ArrayList<String> imageNames) {
        this.id = id;
        this.depth = depth;
        this.updatedAt = updatedAt;
        this.content = content;
        this.nickName = nickName;
        this.imageNames = imageNames;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDepth() {
        return depth;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public String getNickName() {
        return nickName;
    }

    public ArrayList<String> getImageNames() {
        return imageNames;
    }
}
