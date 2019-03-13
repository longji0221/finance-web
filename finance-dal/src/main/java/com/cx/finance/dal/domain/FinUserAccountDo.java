package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:07:18
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinUserAccountDo extends AbstractSerial {

    private static final long serialVersionUID = 1L;

    /**
     * 主键Rid
     */
    private Long rid;
    

    /**
     * 创建时间时间
     */
    private Date gmtCreate;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    /**
     * 用户账户支出，收入金额
     */
    private BigDecimal amount;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账户余额
     */
    private BigDecimal totalAmount;

    /**
     * 累计已还
     */
    private BigDecimal totalRepayAmount;


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
     * 获取创建时间时间
     *
     * @return 创建时间时间
     */
    public Date getGmtCreate(){
      return gmtCreate;
    }

    /**
     * 设置创建时间时间
     * 
     * @param gmtCreate 要设置的创建时间时间
     */
    public void setGmtCreate(Date gmtCreate){
      this.gmtCreate = gmtCreate;
    }

    /**
     * 获取最后修改时间
     *
     * @return 最后修改时间
     */
    public Date getGmtModified(){
      return gmtModified;
    }

    /**
     * 设置最后修改时间
     * 
     * @param gmtModified 要设置的最后修改时间
     */
    public void setGmtModified(Date gmtModified){
      this.gmtModified = gmtModified;
    }

    /**
     * 获取用户账户支出，收入金额
     *
     * @return 用户账户支出，收入金额
     */
    public BigDecimal getAmount(){
      return amount;
    }

    /**
     * 设置用户账户支出，收入金额
     * 
     * @param amount 要设置的用户账户支出，收入金额
     */
    public void setAmount(BigDecimal amount){
      this.amount = amount;
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public Long getUserId(){
      return userId;
    }

    /**
     * 设置用户id
     * 
     * @param userId 要设置的用户id
     */
    public void setUserId(Long userId){
      this.userId = userId;
    }

    /**
     * 获取账户余额
     *
     * @return 账户余额
     */
    public BigDecimal getTotalAmount(){
      return totalAmount;
    }

    /**
     * 设置账户余额
     * 
     * @param totalAmount 要设置的账户余额
     */
    public void setTotalAmount(BigDecimal totalAmount){
      this.totalAmount = totalAmount;
    }

    /**
     * 获取累计已还
     *
     * @return 累计已还
     */
    public BigDecimal getTotalRepayamount(){
      return totalRepayAmount;
    }

    /**
     * 设置累计已还
     * 
     * @param totalRepayamount 要设置的累计已还
     */
    public void setTotalRepayamount(BigDecimal totalRepayamount){
      this.totalRepayAmount = totalRepayamount;
    }

}