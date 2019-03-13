package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 18:30:11
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinPromiseIncomeDo extends AbstractSerial {

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
     * 期数
     */
    private String periods;

    /**
     * 应还时间
     */
    private Date gmtPlanRepay;

    /**
     * 应收总额
     */
    private BigDecimal predictAmount;

    /**
     * 累计已还金额
     */
    private BigDecimal repayAmount;

    /**
     * 剩余应还金额
     */
    private BigDecimal noneAmount;

    /**
     * 应收借款本金
     */
    private BigDecimal amount;

    /**
     * 应收利息
     */
    private BigDecimal retaAmount;

    /**
     * 应收手续费
     */
    private BigDecimal poundageAmount;

    /**
     * 应收逾期费
     */
    private BigDecimal overdueAmount;

    /**
     * 应收搭售商品费用
     */
    private BigDecimal shopAmount;

    /**
     * 状态
     */
    private String status;

    /**
     * 清算公司
     */
    private String liquidationCompany;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private String productType;


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
     * 获取期数
     *
     * @return 期数
     */
    public String getPeriods(){
      return periods;
    }

    /**
     * 设置期数
     * 
     * @param periods 要设置的期数
     */
    public void setPeriods(String periods){
      this.periods = periods;
    }

    /**
     * 获取应还时间
     *
     * @return 应还时间
     */
    public Date getGmtPlanRepay(){
      return gmtPlanRepay;
    }

    /**
     * 设置应还时间
     * 
     * @param gmtPlanRepay 要设置的应还时间
     */
    public void setGmtPlanRepay(Date gmtPlanRepay){
      this.gmtPlanRepay = gmtPlanRepay;
    }

    /**
     * 获取应收总额
     *
     * @return 应收总额
     */
    public BigDecimal getPredictAmount(){
      return predictAmount;
    }

    /**
     * 设置应收总额
     * 
     * @param predictAmount 要设置的应收总额
     */
    public void setPredictAmount(BigDecimal predictAmount){
      this.predictAmount = predictAmount;
    }

    /**
     * 获取累计已还金额
     *
     * @return 累计已还金额
     */
    public BigDecimal getRepayAmount(){
      return repayAmount;
    }

    /**
     * 设置累计已还金额
     * 
     * @param repayAmount 要设置的累计已还金额
     */
    public void setRepayAmount(BigDecimal repayAmount){
      this.repayAmount = repayAmount;
    }

    /**
     * 获取剩余应还金额
     *
     * @return 剩余应还金额
     */
    public BigDecimal getNoneAmount(){
      return noneAmount;
    }

    /**
     * 设置剩余应还金额
     * 
     * @param noneAmount 要设置的剩余应还金额
     */
    public void setNoneAmount(BigDecimal noneAmount){
      this.noneAmount = noneAmount;
    }

    /**
     * 获取应收借款本金
     *
     * @return 应收借款本金
     */
    public BigDecimal getAmount(){
      return amount;
    }

    /**
     * 设置应收借款本金
     * 
     * @param amount 要设置的应收借款本金
     */
    public void setAmount(BigDecimal amount){
      this.amount = amount;
    }

    /**
     * 获取应收利息
     *
     * @return 应收利息
     */
    public BigDecimal getRetaAmount(){
      return retaAmount;
    }

    /**
     * 设置应收利息
     * 
     * @param retaAmount 要设置的应收利息
     */
    public void setRetaAmount(BigDecimal retaAmount){
      this.retaAmount = retaAmount;
    }

    /**
     * 获取应收手续费
     *
     * @return 应收手续费
     */
    public BigDecimal getPoundageAmount(){
      return poundageAmount;
    }

    /**
     * 设置应收手续费
     * 
     * @param poundageAmount 要设置的应收手续费
     */
    public void setPoundageAmount(BigDecimal poundageAmount){
      this.poundageAmount = poundageAmount;
    }

    /**
     * 获取应收逾期费
     *
     * @return 应收逾期费
     */
    public BigDecimal getOverdueAmount(){
      return overdueAmount;
    }

    /**
     * 设置应收逾期费
     * 
     * @param overdueAmount 要设置的应收逾期费
     */
    public void setOverdueAmount(BigDecimal overdueAmount){
      this.overdueAmount = overdueAmount;
    }

    /**
     * 获取应收搭售商品费用
     *
     * @return 应收搭售商品费用
     */
    public BigDecimal getShopAmount(){
      return shopAmount;
    }

    /**
     * 设置应收搭售商品费用
     * 
     * @param shopAmount 要设置的应收搭售商品费用
     */
    public void setShopAmount(BigDecimal shopAmount){
      this.shopAmount = shopAmount;
    }

    /**
     * 获取状态
     *
     * @return 状态
     */
    public String getStatus(){
      return status;
    }

    /**
     * 设置状态
     * 
     * @param status 要设置的状态
     */
    public void setStatus(String status){
      this.status = status;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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