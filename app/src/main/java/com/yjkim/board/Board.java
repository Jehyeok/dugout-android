package com.yjkim.board;

/**
 * Created by jehyeok on 2/9/15.
 */
public class Board {
    private String nickName;

    private Integer id;

    private Integer like;

    private Integer dislike;

    private Integer count;

    private Integer level;

    private Integer groupNumber;

    private String title;

    private String content;

    private String updatedAt;

    private Integer commentSize;

    public Board(Integer id, Integer like, Integer dislike, Integer count, Integer level, Integer groupNumber, String title, String content, Integer commentSize, String nickName, String updatedAt) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
        this.count = count;
        this.level = level;
        this.groupNumber = groupNumber;
        this.title = title;
        this.content = content;
        this.commentSize = commentSize;
        this.nickName = nickName;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLike() {
        return like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getGroupNumber() {
        return groupNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isNotice() {
        return level == 1;
    }

    public Integer getCommentSize() {
        return commentSize;
    }

    public String getNickName() {
        return nickName;
    }
}
