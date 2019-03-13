package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import com.cx.finance.dal.query.OfflineBillDataQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinPangedTradePoolDao;
import com.cx.finance.dal.domain.FinPangedTradePoolDo;
import com.cx.finance.biz.service.FinPangedTradePoolService;

import java.util.HashMap;
import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-05 19:50:36
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finPangedTradePoolService")
public class FinPangedTradePoolServiceImpl extends ParentServiceImpl<FinPangedTradePoolDo, Long> implements FinPangedTradePoolService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinPangedTradePoolServiceImpl.class);
   
    @Resource
    private FinPangedTradePoolDao finPangedTradePoolDao;

		@Override
	public BaseDao<FinPangedTradePoolDo, Long> getDao() {
		return finPangedTradePoolDao;
	}

	@Override
	public FinPangedTradePoolDo getPangedTradePoolDataByTradeNo(String tradeNo) {
		return finPangedTradePoolDao.getPangedTradePoolDataByTradeNo(tradeNo);
	}
	/*
	*清结算系统
	* 线下还款（支付宝）已领取
	*/
	@Override
	public List<HashMap> getDayReceiveByAlipay(FinRepaymentOfflineTradePoolQuery query) {
		return finPangedTradePoolDao.getDayReceiveByAlipay(query);
	}
	/*
	*清结算系统
	* 线下还款总领取
	*/
	@Override
	public List<HashMap> getDayReceive(FinRepaymentOfflineTradePoolQuery query) {
		return finPangedTradePoolDao.getDayReceive(query);
	}
}