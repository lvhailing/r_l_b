package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

import java.io.Serializable;

/**
 *  我的资单 年份
 */
public class MyPayrollYears2B implements IMouldType,Serializable {

	private String wageYears; // 工资年份

	public String getWageYears() {
		return wageYears;
	}

	public void setWageYears(String wageYears) {
		this.wageYears = wageYears;
	}
}
