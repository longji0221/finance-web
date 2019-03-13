package com.cx.finance.dal.domain;

public class FinUserAccountDto extends FinUserAccountRecordDo{

    private String totalAmount;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
