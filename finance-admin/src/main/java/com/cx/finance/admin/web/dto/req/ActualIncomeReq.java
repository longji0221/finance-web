package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.ActualIncomeQuery;

import javax.validation.constraints.Past;
import java.util.Date;

public class ActualIncomeReq  extends BaseReq {
    public String repayNo;
    public String productType;
    public String liquidationCompany;

    @Past
    public Date gmtStart;
    public Date gmtEnd;
    public Integer page;
    public Integer limit;

    public ActualIncomeQuery toDalQuery(){
        ActualIncomeQuery query = new ActualIncomeQuery();

        query.repayNo = repayNo;
        query.productType = productType;
        query.liquidationCompany = liquidationCompany;
        query.gmtEnd = gmtEnd;
        query.gmtStart = gmtStart;
        query.setPageIndex(page);
        query.setPageSize(limit);

        return query;
    }
}
