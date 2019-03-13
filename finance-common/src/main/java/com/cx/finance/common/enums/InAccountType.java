package com.cx.finance.common.enums;

public enum InAccountType {
    PANG("PANG", "挂账到余额"),
    COLLECTION_REPAY("COLLECT", "催收还款"),
    OFFLINE_REPAY("OFFLINE", "线下还款");
    private String code;

    private String name;

    InAccountType(String code, String name) {
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