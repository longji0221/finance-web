package com.cx.finance.common.enums;

public enum AccountRecordType {
    INCASH("INCASH", "现金贷入账"),
    INCASH_PERIOD("INCASH_PERIOD", "现金分期入账"),
    INCONSUM_PERIOD("INCONSUM_PERIOD", "消费分期入账"),
    APLIY_PAY("ALIPAY_PAY", "支付宝收款"),
    APP_REPAY("APP_REPAY", "app还款"),
    CARD_REPAY("CARD_REPAY", "银行卡还款"),
    REFUND("REFUND", "退款"),
    PANG("PANG", "挂账"),
    OFFLINEPANG("OFFLINEPANG", "挂账"),
    OFFLINE("OFFLINE", "线下还款"),
    INREFUND_FAIL("INREFUND_FAIL", "退款失败入账");
    private String    code;

    private String name;

    AccountRecordType(String code, String name) {
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
