package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 * Created by junde on 2018/4/21.
 */

public class CommissionNewsList3B implements IMouldType {

    private String commissionIncome;   //  佣金收益
    private String incomeDate;     //  收益日期
    private String incomeTime;     //  收益时间


    public String getCommissionIncome() {
        return commissionIncome;
    }

    public void setCommissionIncome(String commissionIncome) {
        this.commissionIncome = commissionIncome;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(String incomeTime) {
        this.incomeTime = incomeTime;
    }
}
