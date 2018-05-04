package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *    保单预约列表 实体类
 * Created by junde on 2018/4/19.
 */

public class PolicyBookingList2B implements IMouldType {

    private int count; // 产品预约总数  confirmed：表示已确认；confirming：待确认 refuse：已驳回；canceled：已取消
    private String productName; // 预约产品名称
    private String auditStatus; //审核状态： confirmed：表示已审核；confirming：待审核  refuse：已拒绝；canceled：已取消
    private String insuranceAmount; // 保险金额

}
