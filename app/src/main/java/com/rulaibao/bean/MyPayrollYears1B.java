package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *  我的资单 年份
 */
public class MyPayrollYears1B implements IMouldType {


	private MouldList<MyPayrollYears2B> list;

    public MouldList<MyPayrollYears2B> getList() {
        return list;
    }

    public void setList(MouldList<MyPayrollYears2B> list) {
        this.list = list;
    }
}
