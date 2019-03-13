package com.cx.finance.common.enums;

public enum FinOfflineRepayType {

    ALIPAY("ALIPAY", "支付宝"),
    WECHAT("WECHAT", "微信"),
    BANKCARD("BANKCARD", "银行卡");

    public String code;
    public String name;
    FinOfflineRepayType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FinOfflineRepayType findRoleTypeByCode(String code) {
        for (FinOfflineRepayType type : FinOfflineRepayType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}
