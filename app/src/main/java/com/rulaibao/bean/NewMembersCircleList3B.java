package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *  圈子新成员 实体类
 * Created by junde on 2018/4/24.
 */

public class NewMembersCircleList3B implements IMouldType {

    private String circlePhotoUrl;       //  圈子头像
    private String circleName;       //  圈子名
    private String status;     //  状态（是否加入）
    private String applicantName;     //  申请人的姓名

    public String getCirclePhotoUrl() {
        return circlePhotoUrl;
    }

    public void setCirclePhotoUrl(String circlePhotoUrl) {
        this.circlePhotoUrl = circlePhotoUrl;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
}
