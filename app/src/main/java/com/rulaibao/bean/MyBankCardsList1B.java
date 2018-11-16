package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *    我的银行卡 实体类
 * Created by hong on 2018/11/14.
 */

public class MyBankCardsList1B implements IMouldType {

    private MouldList<MyBankCardsList2B> list; // 银行卡列表
    private String flag;
    private String msg;
    private String message; // 提示信息

    public MouldList<MyBankCardsList2B> getList() {
        return list;
    }

    public void setList(MouldList<MyBankCardsList2B> list) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
