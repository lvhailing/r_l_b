package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 * Created by junde on 2018/4/23.
 */

public class MyTopicList3B implements IMouldType {

    private String circleName;       //  圈子名
    private String groupName;     //  小组名
    private String topicTitle;       //  话题标题
    private String zanNumber;     //  点赞数
    private String commentNumber;     //  评论数

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getZanNumber() {
        return zanNumber;
    }

    public void setZanNumber(String zanNumber) {
        this.zanNumber = zanNumber;
    }

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }
}
