package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

/**
 *  我的资单 年份
 */
public class MyPayrollYears2B implements IMouldType {

	private String wageYears; // 工资年份

	public String getWageYears() {
		return wageYears;
	}

	public void setWageYears(String wageYears) {
		this.wageYears = wageYears;
	}
}