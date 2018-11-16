package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *    我的银行卡 实体类
 * Created by hong on 2018/11/14.
 */

public class MyBankCardsList2B implements IMouldType {

    private String bankCard; // 银行卡号
    private String cardType; // 银行卡类型
    private String bankName; // 银行名称

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
