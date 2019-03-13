package com.cx.finance.common.enums;

/**
 * 财务管理清算公司枚举
 *
 * **/
public enum liquidationCompany {

    ISHANGJIE("ISHANGJIE","爱上街"),
    LVYOU("LVYOU","绿游"),
    YIGANG("YIGANG","易港");

    private String code;
    private String name;

    liquidationCompany(String code, String name) {
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
