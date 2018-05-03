package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

public class InsuranceProduct1B implements IMouldType {
	private String checkStatus;
	private String flag;
	private String message;
	private MouldList<InsuranceProduct2B> list;

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MouldList<InsuranceProduct2B> getList() {
		return list;
	}

	public void setList(MouldList<InsuranceProduct2B> list) {
		this.list = list;
	}
}
