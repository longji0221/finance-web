package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinActualIncomeDo;

import java.util.Date;

public class ActualIncomeQuery extends Page<FinActualIncomeDo> {

    public String repayNo;
    public String productType;
    public String liquidationCompany;

    public Date gmtStart;
    public Date gmtEnd;


    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
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
