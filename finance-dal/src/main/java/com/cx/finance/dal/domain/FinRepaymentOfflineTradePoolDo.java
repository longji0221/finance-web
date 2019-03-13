package com.cx.finance.dal.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.cx.finance.common.AbstractSerial;
import com.cx.finance.common.enums.FinOfflineRepayType;

/**
 * 实体
 * 
 * @author yinxiangyu
 * @version 1.0.0 初始化
 * @date 2018-10-30 11:43:44
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinRepaymentOfflineTradePoolDo extends AbstractSerial {

	public static FinRepaymentOfflineTradePoolDo build(String benefitAccount, String payerAccount, FinOfflineRepayType repayType,
				String tradeNo, BigDecimal amount, String remark, String realName, Date payTime,String operator) {
		FinRepaymentOfflineTradePoolDo tradePoolDo = new FinRepaymentOfflineTradePoolDo();
		tradePoolDo.setBenefitAccount(benefitAccount);
		tradePoolDo.setPayerAccount(payerAccount);
    	tradePoolDo.setRepayType(repayType.code);
        tradePoolDo.setTradeNo(tradeNo);
        tradePoolDo.setAmount(amount);
        tradePoolDo.setRemark(remark);
        tradePoolDo.setRealName(realName);
        tradePoolDo.setPayTime(payTime);
        tradePoolDo.setOperator(operator);
        return tradePoolDo;
	}
	 
    private static final long serialVersionUID = 1L;

    /**
     * 主键Rid
     */
    private Long rid;
    
    /**
     * 交易流水
     */
    private String tradeNo;

    /**
     * 支付者账号
     */
    private String payerAccount;

    /**
     * alipay-支付宝, wx-微信, bank-银行卡支付
     */
    private String repayType;

    /**
     * 
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 
     */
    private Date gmtCreate;

    /**
     * 
     */
    private String realName;


    /**
     * 收款账户
     */
    private String benefitAccount;

    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 获取主键Id
     *
     * @return rid
     */
    public Long getRid(){
      return rid;
    }

    /**
     * 设置主键Id
     * 
     * @param 要设置的主键Id
     */
    public void setRid(Long rid){
      this.rid = rid;
    }
    
    /**
     * 获取交易流水
     *
     * @return 交易流水
     */
    public String getTradeNo(){
      return tradeNo;
    }

    /**
     * 设置交易流水
     * 
     * @param tradeNo 要设置的交易流水
     */
    public void setTradeNo(String tradeNo){
      this.tradeNo = tradeNo;
    }

    /**
     * 获取支付者账号
     *
     * @return 支付者账号
     */
    public String getPayerAccount(){
      return payerAccount;
    }

    /**
     * 设置支付者账号
     * 
     * @param payerAccount 要设置的支付者账号
     */
    public void setPayerAccount(String payerAccount){
      this.payerAccount = payerAccount;
    }

    /**
     * 获取alipay-支付宝, wx-微信, bank-银行卡支付
     *
     * @return alipay-支付宝, wx-微信, bank-银行卡支付
     */
    public String getRepayType(){
      return repayType;
    }

    /**
     * 设置alipay-支付宝, wx-微信, bank-银行卡支付
     * 
     * @param repayType 要设置的alipay-支付宝, wx-微信, bank-银行卡支付
     */
    public void setRepayType(String repayType){
      this.repayType = repayType;
    }

    /**
     * 获取
     *
     * @return 
     */
    public BigDecimal getAmount(){
      return amount;
    }

    /**
     * 设置
     * 
     * @param amount 要设置的
     */
    public void setAmount(BigDecimal amount){
      this.amount = amount;
    }

    /**
     * 获取备注
     *
     * @return 备注
     */
    public String getRemark(){
      return remark;
    }

    /**
     * 设置备注
     * 
     * @param remark 要设置的备注
     */
    public void setRemark(String remark){
      this.remark = remark;
    }

    /**
     * 获取付款时间
     *
     * @return 付款时间
     */
    public Date getPayTime(){
      return payTime;
    }

    /**
     * 设置付款时间
     * 
     * @param payTime 要设置的付款时间
     */
    public void setPayTime(Date payTime){
      this.payTime = payTime;
    }

    /**
     * 获取
     *
     * @return 
     */
    public Date getGmtCreate(){
      return gmtCreate;
    }

    /**
     * 设置
     * 
     * @param gmtCreate 要设置的
     */
    public void setGmtCreate(Date gmtCreate){
      this.gmtCreate = gmtCreate;
    }

    /**
     * 获取
     *
     * @return 
     */
    public String getRealName(){
      return realName;
    }

    /**
     * 设置
     * 
     * @param realName 要设置的
     */
    public void setRealName(String realName){
      this.realName = realName;
    }


    /**
     * 获取收款账户
     *
     * @return 收款账户
     */
    public String getBenefitAccount(){
      return benefitAccount;
    }

    /**
     * 设置收款账户
     * 
     * @param benefitAccount 要设置的收款账户
     */
    public void setBenefitAccount(String benefitAccount){
      this.benefitAccount = benefitAccount;
    }

}