package com.yjkim.comment;

/**
 * Created by jehyeok on 2/11/15.
 */
public class Comment {
    private Integer id;

    private Integer depth;

    private String updatedAt;

    private String content;

    private String nickName;

    public Comment(Integer id, Integer depth, String updatedAt, String content, String nickName) {
        this.id = id;
        this.depth = depth;
        this.updatedAt = updatedAt;
        this.content = content;
        this.nickName = nickName;
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
}
