package com.cx.finance.admin.web.dto.req;

import java.util.Date;

import javax.validation.constraints.Past;

import com.cx.finance.dal.query.OperatorQuery;

public class OperatorQueryReq extends BaseReq{
	
	public String realName;
	public String mobile;
	public String status;
	
	@Past
	public Date dateStart;
	public Date dateEnd;
	public Integer page;
	public Integer limit;
	
	public OperatorQuery toDalQuery(){
		OperatorQuery query = new OperatorQuery();
		
		query.realName = realName;
		query.status = status;
		query.gmtStart = dateStart;
		query.gmtEnd = dateEnd;
		query.mobile = mobile;
		query.setPageIndex(page);
		query.setPageSize(limit);
		
		return query;
	}
}
