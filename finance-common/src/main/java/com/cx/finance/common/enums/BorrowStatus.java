package com.cx.finance.common.enums;

public enum BorrowStatus {
	APPLY("申请/未审核"),
	WAITTRANSED("待打款"),
	TRANSFERING("打款中"),
	TRANSFERRED("已经打款/待还款"),
    FINISHED("已结清");

    public String desz;

    BorrowStatus(String desz) {
        this.desz = desz;
    }

    /**
     * @return the desz
     */
    public String getDesz() {
        return desz;
    }

    /**
     * @param desz the desz to set
     */
    public void setDesz(String desz) {
        this.desz = desz;
    }
}
