package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 * Created by junde on 2018/4/21.
 */

public class CommissionNewsList2B implements IMouldType {

    private String status;     //  状态：read:已读     undread：未读，
    private String createTime;     //  创建时间
    private String content;   //  消息内容
    private String busiType;     //  消息业务类型
    private String busiPriv;     //  存放佣金保单产品id

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBusiPriv() {
        return busiPriv;
    }

    public void setBusiPriv(String busiPriv) {
        this.busiPriv = busiPriv;
    }
}
