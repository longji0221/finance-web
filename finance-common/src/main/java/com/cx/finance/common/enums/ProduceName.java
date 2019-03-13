package com.cx.finance.common.enums;

public enum ProduceName {

    BORROWCASH("BORROWCASH","信用贷"),
    BORROW("BORROW","消费分期"),
    LOAN("LOAN","白领贷"),
    DSED("DSED","都市E贷");

    private String    code;

    private String name;

    ProduceName(String code, String name) {
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
