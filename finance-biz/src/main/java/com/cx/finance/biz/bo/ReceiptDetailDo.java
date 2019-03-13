package com.cx.finance.biz.bo;

import java.util.Date;

/**
 * @类描述：爱上街收款明细实体类
 * @author fanmanfu 2018年6月11日下午7:55:08 
 * @注意：本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 * 
 * **/

public class ReceiptDetailDo {
	
	//  支付日期，业务流水号、UPS流水号、现金支付金额、订单号，业务类型（现金贷，消费分期，白领贷）
	private Date payDate;
	private String repayNo;
	private String upsNo;
	private String amount;	//借款金额
	private String orderNo;
	private String type;
	private String upsType;//渠道名称
	private String otherAmount;//其他金额
	private String beheadInterest;	//砍头息
	private String actualAmount; // 借款金额
	private String rateAmount;	//利息
	private String poundage;	//手续费
	private String overdueAmount;  //逾期费
	private String repayAmount;	//应还款金额
	private String actualRepayAmount;  //实际还款本金
	private String gmtPlanRepayment;	//计划还款时间
	private String arrivalAmount;		//到账金额
	private String paymentAmount;		//已还款金额
	private String days;		//借款天数
	private String renewalNum;		//续借次数
	private String remark;		//备注
	private String status;		//状态
	private String goodsAmount;		//	商品价格
	private String renewalAmount;	//续借金额


	public String getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(String goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public String getRenewalAmount() {
		return renewalAmount;
	}

	public void setRenewalAmount(String renewalAmount) {
		this.renewalAmount = renewalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getRenewalNum() {
		return renewalNum;
	}

	public void setRenewalNum(String renewalNum) {
		this.renewalNum = renewalNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getGmtPlanRepayment() {
		return gmtPlanRepayment;
	}

	public void setGmtPlanRepayment(String gmtPlanRepayment) {
		this.gmtPlanRepayment = gmtPlanRepayment;
	}

	public String getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(String arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public String getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(String repayAmount) {
		this.repayAmount = repayAmount;
	}

	public String getActualRepayAmount() {
		return actualRepayAmount;
	}

	public void setActualRepayAmount(String actualRepayAmount) {
		this.actualRepayAmount = actualRepayAmount;
	}

	public String getBeheadInterest() {
		return beheadInterest;
	}

	public void setBeheadInterest(String beheadInterest) {
		this.beheadInterest = beheadInterest;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(String rateAmount) {
		this.rateAmount = rateAmount;
	}

	public String getPoundage() {
		return poundage;
	}

	public void setPoundage(String poundage) {
		this.poundage = poundage;
	}

	public String getOverdueAmount() {
		return overdueAmount;
	}

	public void setOverdueAmount(String overdueAmount) {
		this.overdueAmount = overdueAmount;
	}

	public String getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(String otherAmount) {
		this.otherAmount = otherAmount;
	}
	public String getUpsType() {
		return upsType;
	}
	public void setUpsType(String upsType) {
		this.upsType = upsType;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public String getRepayNo() {
		return repayNo;
	}
	public void setRepayNo(String repayNo) {
		this.repayNo = repayNo;
	}
	public String getUpsNo() {
		return upsNo;
	}
	public void setUpsNo(String upsNo) {
		this.upsNo = upsNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
