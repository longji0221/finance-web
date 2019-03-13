package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.MgrLogDo;

public class MgrLogQuery extends Page<MgrLogDo> {

    public String userName;
    public String operation;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
