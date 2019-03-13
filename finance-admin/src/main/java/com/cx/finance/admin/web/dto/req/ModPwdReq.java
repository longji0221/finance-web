package com.cx.finance.admin.web.dto.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ModPwdReq extends BaseReq{
	@NotNull
	public String oriPasswd;
	@NotNull
	public String newPasswd;
}
