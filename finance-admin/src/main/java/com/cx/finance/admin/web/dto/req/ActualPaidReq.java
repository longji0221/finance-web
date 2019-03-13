package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.ActualPaidQuery;

import javax.validation.constraints.Past;
import java.util.Date;

public class ActualPaidReq extends BaseReq{

    public String borrowNo;
    public String productType;
    public String liquidationCompany;

    @Past
    public Date gmtStart;
    public Date gmtEnd;
    public Integer page;
    public Integer limit;

    public ActualPaidQuery toDalQuery(){
        ActualPaidQuery query = new ActualPaidQuery();

        query.borrowNo = borrowNo;
        query.productType = productType;
        query.liquidationCompany = liquidationCompany;
        query.gmtEnd = gmtEnd;
        query.gmtStart = gmtStart;
        query.setPageIndex(page);
        query.setPageSize(limit);

        return query;
    }
}
