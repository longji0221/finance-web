package com.cx.finance.dal.domain;

import com.cx.finance.common.AbstractSerial;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 实体
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:06:53
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 public class FinUserAccountRecordDo extends AbstractSerial {

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
     * 
     */
    private Long userId;

    /**
     * 变更金额
     */
    private String amount;

    /**
     * 关联id
     */
    private String refId;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private String operator;

    /**
     * 操作类型
INCASH:现金贷入账INCASH_PERIOD:现金分期入账INCONSUM_PERIOD消费分期入账APLIY_PAY:支付宝收款APP_REPAY:app还款6、CARD_REPAY:银行卡还款REFUND:退款  (发起退款申请时，即生成一条余额记录，扣除退款金额)INREFUND_FAIL退款失败入账  (退款失败，资金回到账户余额中)
     */
    private String type;



    private String accountAmount;


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

    /**
     * 获取变更金额
     *
     * @return 变更金额
     */
    public String getAmount(){
      return amount;
    }

    /**
     * 设置变更金额
     * 
     * @param amount 要设置的变更金额
     */
    public void setAmount(String amount){
      this.amount = amount;
    }

    /**
     * 获取关联id
     *
     * @return 关联id
     */
    public String getRefId(){
      return refId;
    }

    /**
     * 设置关联id
     * 
     * @param refId 要设置的关联id
     */
    public void setRefId(String refId){
      this.refId = refId;
    }

    /**
     * 获取
     *
     * @return 
     */
    public String getRemark(){
      return remark;
    }

    /**
     * 设置
     * 
     * @param remark 要设置的
     */
    public void setRemark(String remark){
      this.remark = remark;
    }

    /**
     * 获取
     *
     * @return 
     */
    public String getOperator(){
      return operator;
    }

    /**
     * 设置
     * 
     * @param operator 要设置的
     */
    public void setOperator(String operator){
      this.operator = operator;
    }

    /**
     * 获取操作类型
INCASH:现金贷入账INCASH_PERIOD:现金分期入账INCONSUM_PERIOD消费分期入账APLIY_PAY:支付宝收款APP_REPAY:app还款6、CARD_REPAY:银行卡还款REFUND:退款  (发起退款申请时，即生成一条余额记录，扣除退款金额)INREFUND_FAIL退款失败入账  (退款失败，资金回到账户余额中)
     *
     * @return 操作类型
INCASH:现金贷入账INCASH_PERIOD:现金分期入账INCONSUM_PERIOD消费分期入账APLIY_PAY:支付宝收款APP_REPAY:app还款6、CARD_REPAY:银行卡还款REFUND:退款  (发起退款申请时，即生成一条余额记录，扣除退款金额)INREFUND_FAIL退款失败入账  (退款失败，资金回到账户余额中)
     */
    public String getType(){
      return type;
    }

    /**
     * 设置操作类型
INCASH:现金贷入账INCASH_PERIOD:现金分期入账INCONSUM_PERIOD消费分期入账APLIY_PAY:支付宝收款APP_REPAY:app还款6、CARD_REPAY:银行卡还款REFUND:退款  (发起退款申请时，即生成一条余额记录，扣除退款金额)INREFUND_FAIL退款失败入账  (退款失败，资金回到账户余额中)
     * 
     * @param type 要设置的操作类型
INCASH:现金贷入账INCASH_PERIOD:现金分期入账INCONSUM_PERIOD消费分期入账APLIY_PAY:支付宝收款APP_REPAY:app还款6、CARD_REPAY:银行卡还款REFUND:退款  (发起退款申请时，即生成一条余额记录，扣除退款金额)INREFUND_FAIL退款失败入账  (退款失败，资金回到账户余额中)
     */
    public void setType(String type){
      this.type = type;
    }

    public String getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(String accountAmount) {
        this.accountAmount = accountAmount;
    }
}