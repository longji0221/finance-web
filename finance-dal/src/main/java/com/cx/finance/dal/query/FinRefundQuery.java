package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinRefundDo;


public class FinRefundQuery extends Page<FinRefundDo> {
    private String mobile;

    private String status; //退款状态

    private String type; //退款方式(ISJREBATE:爱上街余额 TRANSFER(转账))

    public Integer page;

    public Integer limit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
