package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 16:36:24
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinActualIncomeDo extends AbstractSerial {

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
     * 收款流水号
     */
    private String repayNo;

    /**
     * 实收金额
     */
    private BigDecimal actualAmount;

    /**
     * 实际收款时间
     */
    private Date actualTime;

    /**
     * 第三方简称
     */
    private String tppNid;

    /**
     * 第三方流水号
     */
    private String payTradeNo;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 清算公司
     */
    private String liquidationCompany;

    /**
     * 备注
     */
    private String remark;


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
     * 获取收款流水号
     *
     * @return 收款流水号
     */
    public String getRepayNo(){
      return repayNo;
    }

    /**
     * 设置收款流水号
     * 
     * @param repayNo 要设置的收款流水号
     */
    public void setRepayNo(String repayNo){
      this.repayNo = repayNo;
    }

    /**
     * 获取实收金额
     *
     * @return 实收金额
     */
    public BigDecimal getActualAmount(){
      return actualAmount;
    }

    /**
     * 设置实收金额
     * 
     * @param actualAmount 要设置的实收金额
     */
    public void setActualAmount(BigDecimal actualAmount){
      this.actualAmount = actualAmount;
    }

    /**
     * 获取实际收款时间
     *
     * @return 实际收款时间
     */
    public Date getActualTime(){
      return actualTime;
    }

    /**
     * 设置实际收款时间
     * 
     * @param actualTime 要设置的实际收款时间
     */
    public void setActualTime(Date actualTime){
      this.actualTime = actualTime;
    }

    /**
     * 获取第三方简称
     *
     * @return 第三方简称
     */
    public String getTppNid(){
      return tppNid;
    }

    /**
     * 设置第三方简称
     * 
     * @param tppNid 要设置的第三方简称
     */
    public void setTppNid(String tppNid){
      this.tppNid = tppNid;
    }

    /**
     * 获取第三方流水号
     *
     * @return 第三方流水号
     */
    public String getPayTradeNo(){
      return payTradeNo;
    }

    /**
     * 设置第三方流水号
     * 
     * @param payTradeNo 要设置的第三方流水号
     */
    public void setPayTradeNo(String payTradeNo){
      this.payTradeNo = payTradeNo;
    }

    /**
     * 获取支付方式
     *
     * @return 支付方式
     */
    public String getPayType(){
      return payType;
    }

    /**
     * 设置支付方式
     * 
     * @param payType 要设置的支付方式
     */
    public void setPayType(String payType){
      this.payType = payType;
    }

    /**
     * 获取产品名称
     *
     * @return 产品名称
     */
    public String getProductName(){
      return productName;
    }

    /**
     * 设置产品名称
     * 
     * @param productName 要设置的产品名称
     */
    public void setProductName(String productName){
      this.productName = productName;
    }

    /**
     * 获取产品类型
     *
     * @return 产品类型
     */
    public String getProductType(){
      return productType;
    }

    /**
     * 设置产品类型
     * 
     * @param productType 要设置的产品类型
     */
    public void setProductType(String productType){
      this.productType = productType;
    }

    /**
     * 获取清算公司
     *
     * @return 清算公司
     */
    public String getLiquidationCompany(){
      return liquidationCompany;
    }

    /**
     * 设置清算公司
     * 
     * @param liquidationCompany 要设置的清算公司
     */
    public void setLiquidationCompany(String liquidationCompany){
      this.liquidationCompany = liquidationCompany;
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

}