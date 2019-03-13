package com.cx.finance.biz.bo;

public class CrawlerAlipayBo {
	public String benefitAccount;
	public String accountName;
	public String mobile;
	public String orderNo;
	public String createTime;
	public String realName;
	public String money;
	public String remark;
	public String remarkAccount;
	
	public static CrawlerAlipayBo gen(String benefitAccount, String mobile, String orderNo, String createTime, String realName, 
				String money, String remark, String remarkAccount,String accountName) {
		CrawlerAlipayBo alipayBo = new CrawlerAlipayBo();
		alipayBo.benefitAccount = benefitAccount;
		alipayBo.mobile = mobile;
		alipayBo.orderNo = orderNo;
		alipayBo.createTime = createTime;
		alipayBo.realName = realName;
		alipayBo.money = money;
		alipayBo.remark = remark;
		alipayBo.remarkAccount = remarkAccount;
		alipayBo.accountName = accountName;
		return alipayBo;
	}
}
