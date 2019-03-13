package com.cx.finance.admin.web.dto.req;

import javax.validation.constraints.NotNull;

public class ModPwdByMgrReq {
    @NotNull
    public String newPasswd;
    @NotNull
    public Long rid;

    public String realName;

    public String roleId;


}
