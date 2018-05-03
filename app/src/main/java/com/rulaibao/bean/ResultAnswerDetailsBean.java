package com.rulaibao.bean;


import com.rulaibao.network.types.IMouldType;

// 回答详情
public class ResultAnswerDetailsBean implements IMouldType {


	private String total;
	private String flag;
	private String message;
	private ResultAnswerDetailsItemBean appAnswer;


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public ResultAnswerDetailsItemBean getAppAnswer() {
        return appAnswer;
    }

    public void setAppAnswer(ResultAnswerDetailsItemBean appAnswer) {
        this.appAnswer = appAnswer;
    }
}
