package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;

import java.math.BigDecimal;
import java.util.Date;

public class OfflineBillDataQuery extends Page<FinPangedTradePoolDo> {

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
