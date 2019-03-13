package com.cx.finance.common.enums;

public enum ProduceType {
    BORROW("BORROW", "消费分期"),
    BORROWCASH("BORROWCASH", "信用贷"),
     CASHINSTALMENT("CASHINSTALMENT", "现金分期");
    private String    code;

    private String name;

    ProduceType(String code, String name) {
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
