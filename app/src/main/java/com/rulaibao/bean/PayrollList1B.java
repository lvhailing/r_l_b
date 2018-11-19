package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *  工资单列表 实体类
 */
public class PayrollList1B implements IMouldType {


	private MouldList<PayrollList2B> list;
	private String total;
	private String flag;
	private String message;

    public MouldList<PayrollList2B> getList() {
        return list;
    }

    public void setList(MouldList<PayrollList2B> list) {
        this.list = list;
    }
}
