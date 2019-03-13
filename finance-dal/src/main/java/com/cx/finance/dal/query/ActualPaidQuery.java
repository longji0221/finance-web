package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinActualPaidDo;

import javax.validation.constraints.Past;
import java.util.Date;

public class ActualPaidQuery extends Page<FinActualPaidDo> {


    public String borrowNo;
    public String productType;
    public String liquidationCompany;

    public Date gmtStart;
    public Date gmtEnd;


    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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
