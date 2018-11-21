package com.rulaibao.bean;

import com.rulaibao.network.types.IMouldType;

/**
 *  银行卡列表 实体类
 * Created by hong on 2018/11/21.
 */

public class BankCardList2B implements IMouldType {

    private String id; // ID
    private String bank; // 所属银行
    private String bankName; // 开户行名称
    private String bankAddress; // 开户行地址
    private String bankcardNo; // 银行卡号
    private String isWageCard; // 是否是工资卡（yes-是，no不是）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public String getIsWageCard() {
        return isWageCard;
    }

    public void setIsWageCard(String isWageCard) {
        this.isWageCard = isWageCard;
    }
}
