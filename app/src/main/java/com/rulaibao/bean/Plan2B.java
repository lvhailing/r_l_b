package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;
import com.rulaibao.network.types.MouldList;

public class Plan2B implements IMouldType {
	private MouldList<Plan3B> list;
	private String flag;
	private String message;

	public MouldList<Plan3B> getList() {
		return list;
	}

	public void setList(MouldList<Plan3B> list) {
		this.list = list;
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
}
