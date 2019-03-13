package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.MgrLogQuery;


public class MgrLogReq extends BaseReq  {
    public String userName;
    public String operation;
    public Integer page;
    public Integer limit;

    public MgrLogQuery toDalQuery(){
        MgrLogQuery query = new MgrLogQuery();

        query.userName = userName;
        query.operation = operation;
        query.setPageIndex(page);
        query.setPageSize(limit);
        return query;
    }
}
