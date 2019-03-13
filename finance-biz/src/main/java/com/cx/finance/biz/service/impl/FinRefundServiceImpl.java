package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.alibaba.druid.support.json.JSONUtils;
import com.cx.finance.common.enums.ReFundType;
import com.cx.finance.common.exception.BizException;
import com.cx.finance.common.util.FinanceThirdUtil;
import com.cx.finance.dal.domain.FinRefundDto;
import com.cx.finance.dal.query.FinRefundQuery;
import com.cx.finance.biz.bo.FinRefundBo;
import com.cx.finance.biz.service.FinUserAccountRecordService;
import com.cx.finance.biz.service.FinUserAccountService;
import com.cx.finance.common.enums.AccountRecordType;
import com.cx.finance.common.enums.RefundChannelType;
import com.cx.finance.common.enums.RefundStatus;
import com.cx.finance.common.util.StringUtil;
import com.cx.finance.dal.domain.FinUserAccountDo;
import com.cx.finance.dal.domain.FinUserAccountRecordDo;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinRefundDao;
import com.cx.finance.dal.domain.FinRefundDo;
import com.cx.finance.biz.service.FinRefundService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.util.Map;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-09 16:07:21
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finRefundService")
public class FinRefundServiceImpl extends ParentServiceImpl<FinRefundDo, Long> implements FinRefundService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinRefundServiceImpl.class);
   
    @Resource
    private FinRefundDao finRefundDao;

	@Resource
	private FinUserAccountService finUserAccountService;

	@Resource
	private FinUserAccountRecordService finUserAccountRecordService;

	@Resource
	private FinRefundService finRefundService;

		@Override
	public BaseDao<FinRefundDo, Long> getDao() {
		return finRefundDao;
	}

	@Override
	public List<FinRefundDto> getRefundByUserId(FinRefundQuery query) {
		return finRefundDao.getRefundByUserId(query);
	}
	@Override
	public List<FinRefundDo>getRefundListByBorrowNo(String borrowNo){
		return finRefundDao.getRefundListByBorrowNo(borrowNo);

	}

	@Override
	public List<FinRefundDo> getRefundListNoFailByBorrowNo(String borrowNo) {
		return finRefundDao.getRefundListNoFailByBorrowNo(borrowNo);
	}

	@Override
	public void executeRefund(FinRefundBo bo) {
		FinUserAccountDo userDo=finUserAccountService.getByMobile(bo.mobile);
		//增加退款
		FinRefundDo refundDo=new FinRefundDo();
		refundDo.setAmount(new BigDecimal(bo.reFundAmount));
		refundDo.setChannel(RefundChannelType.OFFLINE.getCode());
		refundDo.setOperater(bo.realName);
		refundDo.setRemark(bo.remark);
		refundDo.setStatus(RefundStatus.PROCEESING.getCode());
		refundDo.setType(bo.type);
		refundDo.setUserId(userDo.getUserId());
		refundDo.setBorrowNo(bo.borrowNo);
		refundDo.setTradeNo("TK"+System.currentTimeMillis());
		if(ReFundType.ISJ_REBATE.getCode().equals(bo.type)){
			refundDo.setUserAccount(bo.mobile);
		}else {
			refundDo.setUserAccount(bo.userAccount);
		}
		finRefundService.saveRecord(refundDo);
	}

	@Override
	public void refuseRefund(FinRefundBo bo) {
		FinUserAccountDo userDo=finUserAccountService.getByMobile(bo.mobile);
		//修改退款为失败
		updateRefund(bo.finance,RefundStatus.FAIL.getCode(),bo.id);

	}

	@Override
	public void passRefund(FinRefundBo bo) {
		if(ReFundType.ISJ_REBATE.getCode().equals(bo.type)){
			Map data=new HashMap();
			data.put("mobile",bo.mobile);
			data.put("amount",bo.reFundAmount);
			data.put("remark",bo.remark);
			data.put("operator",bo.finance);
			String resultStr=FinanceThirdUtil.offlineRefundForApi(data, (String) bo.dataUrl.get("ISJ"));
			Map respBo= (Map) JSONUtils.parse(resultStr);
			if(respBo.get("code").equals("200")){
				updateRefund(bo.finance, RefundStatus.SUCCESS.getCode(), bo.id);
			}else {
				throw new BizException("第三方请求异常:"+respBo.get("msg"));
			}
		}else {
			updateRefund(bo.finance, RefundStatus.SUCCESS.getCode(), bo.id);
		}

	}
	void updateRefund(String finance,String status,Long id){
		FinRefundDo refundDo=new FinRefundDo();
		refundDo.setFinance(finance);
		refundDo.setStatus(status);
		refundDo.setRid(id);
		refundDo.setGmtModified(new Date());
		refundDo.setRefundTime(new Date());
		finRefundService.updateById(refundDo);
	}


}