package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.UserQuery;

public class UserQueryReq extends BaseReq{

    private String mobile;

    private String borrowNo;

    private String produce;

    private String pangAmount;

    private String pangTradeNo;

    private String reFundAmount;

    private String userAmount;

    private String userAccount;

    private String type;

    public Integer page;

    public Integer limit;

    private String remark;



    //借款产品区分（self自营，third第三方）
    private String ascription;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPangAmount() {
        return pangAmount;
    }

    public void setPangAmount(String pangAmount) {
        this.pangAmount = pangAmount;
    }

    public String getPangTradeNo() {
        return pangTradeNo;
    }

    public Integer getPage() {
        return page;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setPangTradeNo(String pangTradeNo) {
        this.pangTradeNo = pangTradeNo;
    }

    public String getReFundAmount() {
        return reFundAmount;
    }

    public void setReFundAmount(String reFundAmount) {
        this.reFundAmount = reFundAmount;
    }

    public String getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(String userAmount) {
        this.userAmount = userAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    public UserQuery toDalQuery(){
        UserQuery query = new UserQuery();

        query.mobile = mobile;

        query.setPageIndex(page);
        query.setPageSize(limit);

        return query;
    }
}
