package com.cx.finance.common.enums;

public enum CompanyType {

    ISJ("ISJ", "爱上街"),
    JSD("JSD", "极速贷"),
    DSED("DSED", "都市e贷");

    private String  code;

    private String name;

    CompanyType(String code, String name) {
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
