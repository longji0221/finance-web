package com.cx.finance.admin.web.dto.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OperatorEditReq extends BaseReq{
	public Long rid;
	
	@NotNull
	@Size(min=11)
	public String mobile;
	
	public String email;
	
	public Long roleId;
	
	public String password;
	
	@NotNull
	@Size(min=2)
	public String realName;

	public Integer status;
	
}
