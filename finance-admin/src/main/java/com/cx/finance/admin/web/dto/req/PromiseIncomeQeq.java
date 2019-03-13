package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.PromiseIncomeQuery;

import javax.validation.constraints.Past;
import java.util.Date;

public class PromiseIncomeQeq  extends BaseReq {
    public String borrowNo;
    public String liquidationCompany;

    @Past
    public Date gmtStart;
    public Date gmtEnd;
    public Integer page;
    public Integer limit;

    public PromiseIncomeQuery toDalQuery(){
        PromiseIncomeQuery query = new PromiseIncomeQuery();

        query.borrowNo = borrowNo;
        query.liquidationCompany = liquidationCompany;
        query.gmtEnd = gmtEnd;
        query.gmtStart = gmtStart;
        query.setPageIndex(page);
        query.setPageSize(limit);

        return query;
    }
}
