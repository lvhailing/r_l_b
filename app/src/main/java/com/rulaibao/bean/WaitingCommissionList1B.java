package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *    待发佣金列表 实体类
 * Created by hong on 2018/11/08.
 */

public class WaitingCommissionList1B implements IMouldType {

    private MouldList<WaitingCommissionList2B> list; // 保单列表
    private String flag;
    private String msg;
    private String message;

}
