package com.cx.finance.common.enums;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum ReFundType {
    ISJ_REBATE("ISJREBATE", "爱上街余额"), TRANSFER("TRANSFER", "转账");

    private String    code;

    private String name;




    ReFundType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
