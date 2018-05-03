package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

public class Bulletin2B implements IMouldType {
    private String bulletinContent;
    private String bulletinId;

    public String getBulletinContent() {
        return bulletinContent;
    }

    public void setBulletinContent(String bulletinContent) {
        this.bulletinContent = bulletinContent;
    }

    public String getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(String bulletinId) {
        this.bulletinId = bulletinId;
    }
}