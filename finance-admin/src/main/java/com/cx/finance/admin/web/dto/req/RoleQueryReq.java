package com.cx.finance.admin.web.dto.req;

import com.cx.finance.dal.query.OperatorQuery;
import com.cx.finance.dal.query.RoleQuery;

import javax.validation.constraints.Past;
import java.util.Date;

public class RoleQueryReq {

    public String roleName;
    public String mobile;
    public String status;

    @Past
    public Date dateStart;
    public Date dateEnd;
    public Integer page;
    public Integer limit;

    public RoleQuery toDalQuery(){
        RoleQuery query = new RoleQuery();
        query.roleName=roleName;
        query.status = status;
        query.gmtStart = dateStart;
        query.gmtEnd = dateEnd;
        query.setPageIndex(page);
        query.setPageSize(limit);

        return query;
    }
}
