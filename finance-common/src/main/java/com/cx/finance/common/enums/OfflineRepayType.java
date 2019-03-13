package com.cx.finance.common.enums;

public enum OfflineRepayType {

    ALIPAY("ALIPAY", "支付宝"),
    WECHAT("WECHAT", "微信"),
    BANKCARD("BANKCARD", "银行卡");

    public String code;
    public String name;
    OfflineRepayType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OfflineRepayType findRoleTypeByCode(String code) {
        for (OfflineRepayType type : OfflineRepayType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

}
