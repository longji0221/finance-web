package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-09 16:07:21
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinRefundDo extends AbstractSerial {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Rid
     */
    private Long rid;
    

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款方式(ISJREBATE:爱上街余额 TRANSFER(转账)) 
     */
    private String type;

    /**
     * 退款渠道
     */
    private String channel;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 
     */
    private String tradeNo;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 退款状态（FAIL,SUCCESS,PROCEESING）
     */
    private String status;

    /**
     * 
     */
    private String operater;

    /**
     * 财务
     */
    private String finance;

    /**
     * 退款账户 ，退款到爱上街  填手机号
     */
    private String userAccount;

    /**
     * 
     */
    private Long userId;



    /**
     *
     */
    private String borrowNo;

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
     * 获取创建时间
     *
     * @return 创建时间
     */
    public Date getGmtCreate(){
      return gmtCreate;
    }

    /**
     * 设置创建时间
     * 
     * @param gmtCreate 要设置的创建时间
     */
    public void setGmtCreate(Date gmtCreate){
      this.gmtCreate = gmtCreate;
    }

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    public Date getGmtModified(){
      return gmtModified;
    }

    /**
     * 设置更新时间
     * 
     * @param gmtModified 要设置的更新时间
     */
    public void setGmtModified(Date gmtModified){
      this.gmtModified = gmtModified;
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
     * 获取退款方式(ISJREBATE:爱上街余额 TRANSFER(转账)) 
     *
     * @return 退款方式(ISJREBATE:爱上街余额 TRANSFER(转账)) 
     */
    public String getType(){
      return type;
    }

    /**
     * 设置退款方式(ISJREBATE:爱上街余额 TRANSFER(转账)) 
     * 
     * @param type 要设置的退款方式(ISJREBATE:爱上街余额 TRANSFER(转账)) 
     */
    public void setType(String type){
      this.type = type;
    }

    /**
     * 获取退款渠道
     *
     * @return 退款渠道
     */
    public String getChannel(){
      return channel;
    }

    /**
     * 设置退款渠道
     * 
     * @param channel 要设置的退款渠道
     */
    public void setChannel(String channel){
      this.channel = channel;
    }

    /**
     * 获取退款金额
     *
     * @return 退款金额
     */
    public BigDecimal getAmount(){
      return amount;
    }

    /**
     * 设置退款金额
     * 
     * @param amount 要设置的退款金额
     */
    public void setAmount(BigDecimal amount){
      this.amount = amount;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    /**
     * 获取
     *
     * @return 
     */
    public String getTradeNo(){
      return tradeNo;
    }

    /**
     * 设置
     * 
     * @param tradeNo 要设置的
     */
    public void setTradeNo(String tradeNo){
      this.tradeNo = tradeNo;
    }

    /**
     * 获取退款时间
     *
     * @return 退款时间
     */
    public Date getRefundTime(){
      return refundTime;
    }

    /**
     * 设置退款时间
     * 
     * @param refundTime 要设置的退款时间
     */
    public void setRefundTime(Date refundTime){
      this.refundTime = refundTime;
    }

    /**
     * 获取退款状态（FAIL,SUCCESS,PROCEESING）
     *
     * @return 退款状态（FAIL,SUCCESS,PROCEESING）
     */
    public String getStatus(){
      return status;
    }

    /**
     * 设置退款状态（FAIL,SUCCESS,PROCEESING）
     * 
     * @param status 要设置的退款状态（FAIL,SUCCESS,PROCEESING）
     */
    public void setStatus(String status){
      this.status = status;
    }

    /**
     * 获取
     *
     * @return 
     */
    public String getOperater(){
      return operater;
    }

    /**
     * 设置
     * 
     * @param operater 要设置的
     */
    public void setOperater(String operater){
      this.operater = operater;
    }

    /**
     * 获取财务
     *
     * @return 财务
     */
    public String getFinance(){
      return finance;
    }

    /**
     * 设置财务
     * 
     * @param finance 要设置的财务
     */
    public void setFinance(String finance){
      this.finance = finance;
    }

    /**
     * 获取退款账户 ，退款到爱上街  填手机号
     *
     * @return 退款账户 ，退款到爱上街  填手机号
     */
    public String getUserAccount(){
      return userAccount;
    }

    /**
     * 设置退款账户 ，退款到爱上街  填手机号
     * 
     * @param userAccount 要设置的退款账户 ，退款到爱上街  填手机号
     */
    public void setUserAccount(String userAccount){
      this.userAccount = userAccount;
    }

    /**
     * 获取
     *
     * @return 
     */
    public Long getUserId(){
      return userId;
    }

    /**
     * 设置
     * 
     * @param userId 要设置的
     */
    public void setUserId(Long userId){
      this.userId = userId;
    }

}