package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;


// 未读消息
public class UnreadNewsCount2B implements IMouldType {
	private String insurance;// 保单消息
	private String commission; // 佣金消息 互动消息（待定）；圈子新成员（待定）

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
