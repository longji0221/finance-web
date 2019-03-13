package com.cx.finance.dal.query;

import java.util.Date;

import com.cx.finance.common.page.Page;

public class CommonQuery<T> extends Page<T>{
	private static final long serialVersionUID = 1L;
	
	public String mobile;
	
	public Date gmtStart;
	public Date gmtEnd;
}
