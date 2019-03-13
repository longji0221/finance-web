package com.cx.finance.admin.web.dto.req;

public class InAccountQueryReq {
    private String borrowNo;

    private String accountAmount;

    private String operator;

    private String isBalance;

    private String remark;

    private String produce;

    private String mobile;

    private String isAllFinish;

    private String userAmount;

    private String inAmountTradeNo;
    public String getIsAllFinish() {
        return isAllFinish;
    }

    public void setIsAllFinish(String isAllFinish) {
        this.isAllFinish = isAllFinish;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProduce() {
        return produce;

    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getBorrowNo() {

        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIsBalance() {
        return isBalance;
    }

    public void setIsBalance(String isBalance) {
        this.isBalance = isBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getInAmountTradeNo() {
        return inAmountTradeNo;
    }

    public void setInAmountTradeNo(String inAmountTradeNo) {
        this.inAmountTradeNo = inAmountTradeNo;
    }
}
