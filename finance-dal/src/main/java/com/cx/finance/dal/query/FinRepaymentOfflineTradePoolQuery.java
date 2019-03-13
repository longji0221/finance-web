package com.cx.finance.dal.query;

import com.cx.finance.common.page.Page;
import com.cx.finance.dal.domain.FinRepaymentOfflineTradePoolDo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FinRepaymentOfflineTradePoolQuery extends Page<FinRepaymentOfflineTradePoolDo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Id
     */
    private Long id;
    /**
     * 交易流水
     */
    private String tradeNo;
    /**
     * 支付者的账号
     */
    private String payerAccount;
    /**
     * 还款方式
     */
    private String repayType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 真实姓名
     */
    private String realName;

    private Date dateStart;

    private Date dateEnd;

    private BigDecimal amount;

    private Date payTime;
    /**
     * 查询项的内容/值
     */
    private String valueContent;
    /**
     * 查询项
     */
    private String condition;

    private List list;

    private String benefitAccount;

    public Integer page;

    public Integer limit;

    private String isUse;

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValueContent() {
        return valueContent;
    }

    public void setValueContent(String valueContent) {
        this.valueContent = valueContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getBenefitAccount() {
        return benefitAccount;
    }

    public void setBenefitAccount(String benefitAccount) {
        this.benefitAccount = benefitAccount;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}