package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 * Created by junde on 2018/4/16.
 */

public class PlatformBulletinList3B implements IMouldType {

    private String title;       //  公告标题
    private String description;     //  内容
    private String sendTime;        //  发送时间
    private String bulletinId;          //  公告编号
    private String readState;          //  是否阅读（yes:已阅读；no:未阅读）

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }
}
