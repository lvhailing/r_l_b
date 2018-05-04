package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *    保单预约列表 实体类
 * Created by junde on 2018/4/19.
 */

public class PolicyBookingList1B implements IMouldType {

    private MouldList<PolicyBookingList2B> list; // 预约列表
    private String flag;
    private String msg;

    public MouldList<PolicyBookingList2B> getList() {
        return list;
    }

    public void setList(MouldList<PolicyBookingList2B> list) {
        this.list = list;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
