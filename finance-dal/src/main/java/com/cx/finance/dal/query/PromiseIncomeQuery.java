package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinPromiseIncomeDo;

import java.util.Date;

public class PromiseIncomeQuery extends Page<FinPromiseIncomeDo>{

    public String borrowNo;
    public String liquidationCompany;

    public Date gmtStart;
    public Date gmtEnd;
    public Integer page;
    public Integer limit;

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getLiquidationCompany() {
        return liquidationCompany;
    }

    public void setLiquidationCompany(String liquidationCompany) {
        this.liquidationCompany = liquidationCompany;
    }

    public Date getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(Date gmtStart) {
        this.gmtStart = gmtStart;
    }

    public Date getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(Date gmtEnd) {
        this.gmtEnd = gmtEnd;
    }
}
