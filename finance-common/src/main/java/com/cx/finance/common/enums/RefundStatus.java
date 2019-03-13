package com.cx.finance.common.enums;

public enum RefundStatus {

    FAIL("FAIL", "退款失败"),SUCCESS("SUCCESS", "退款成功"),PROCEESING("PROCEESING", "退款审核中");

    private String  code;

    private String name;




    RefundStatus(String code, String name) {
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
