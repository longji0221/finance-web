package com.cx.finance.dal.domain;

import java.util.Date;
import java.math.BigDecimal;
import com.cx.finance.common.AbstractSerial;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-10-29 14:29:27
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinActualPaidDo extends AbstractSerial {

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
     * 借款编号
     */
    private String borrowNo;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 实际支付时间
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
     * @param
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
     * 获取借款编号
     *
     * @return 借款编号
     */
    public String getBorrowNo(){
      return borrowNo;
    }

    /**
     * 设置借款编号
     * 
     * @param borrowNo 要设置的借款编号
     */
    public void setBorrowNo(String borrowNo){
      this.borrowNo = borrowNo;
    }

    /**
     * 获取实际支付金额
     *
     * @return 实际支付金额
     */
    public BigDecimal getActualAmount(){
      return actualAmount;
    }

    /**
     * 设置实际支付金额
     * 
     * @param actualAmount 要设置的实际支付金额
     */
    public void setActualAmount(BigDecimal actualAmount){
      this.actualAmount = actualAmount;
    }

    /**
     * 获取实际支付时间
     *
     * @return 实际支付时间
     */
    public Date getActualTime(){
      return actualTime;
    }

    /**
     * 设置实际支付时间
     * 
     * @param actualTime 要设置的实际支付时间
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
     * 获取产品类型
     *
     * @return 产品类型
     */
    public String getProductName(){
      return productName;
    }

    /**
     * 设置产品类型
     * 
     * @param productName 要设置的产品类型
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