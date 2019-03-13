package com.cx.finance.dal.query;

import java.util.Date;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.MgrOperatorDo;

public class OperatorQuery extends Page<MgrOperatorDo> {
	private static final long serialVersionUID = 1L;
	
	public String realName;
	public String status;
	
	public String mobile;
	public Date gmtStart;
	public Date gmtEnd;
}
