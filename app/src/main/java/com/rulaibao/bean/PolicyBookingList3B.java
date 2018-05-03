package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *    保单预约列表 实体类
 * Created by junde on 2018/4/19.
 */

public class PolicyBookingList3B implements IMouldType {

    private String insuranceName; // 保险名称
    private String status; // 保单状态
    private String insurancePremiums; // 保费

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getInsurancePremiums() {
        return insurancePremiums;
    }

    public void setInsurancePremiums(String insurancePremiums) {
        this.insurancePremiums = insurancePremiums;
    }
}
