package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *  互动消息 实体类
 * Created by junde on 2018/4/24.
 */

public class InteractiveNewsList3B implements IMouldType {

    private String photoUrl;       //  头像
    private String name;       //  姓名
    private String title;       //  标题
    private String time;     //  时间
    private String date;     //  日期（月、日）
    private String reply;     //回复

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
