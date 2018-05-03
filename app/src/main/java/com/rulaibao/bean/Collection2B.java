package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

public class Collection2B implements IMouldType {
	private String flag;
	private String message;
	private String dataStatus;

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

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
}
