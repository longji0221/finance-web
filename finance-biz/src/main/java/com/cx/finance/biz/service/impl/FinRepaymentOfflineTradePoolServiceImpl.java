package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.FinRepaymentOfflineTradePoolQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinRepaymentOfflineTradePoolDao;
import com.cx.finance.dal.domain.FinRepaymentOfflineTradePoolDo;
import com.cx.finance.biz.service.FinRepaymentOfflineTradePoolService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


/**
 * ServiceImpl
 * 
 * @author yinxiangyu
 * @version 1.0.0 初始化
 * @date 2018-10-30 11:43:44
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finRepaymentOfflineTradePoolService")
public class FinRepaymentOfflineTradePoolServiceImpl extends ParentServiceImpl<FinRepaymentOfflineTradePoolDo, Long> implements FinRepaymentOfflineTradePoolService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinRepaymentOfflineTradePoolServiceImpl.class);
   
    @Resource
    private FinRepaymentOfflineTradePoolDao finRepaymentOfflineTradePoolDao;

	@Override
	public BaseDao<FinRepaymentOfflineTradePoolDo, Long> getDao() {
		return finRepaymentOfflineTradePoolDao;
	}

	@Override
	public List<FinRepaymentOfflineTradePoolQuery> getListRepaymentOfflinePool(FinRepaymentOfflineTradePoolQuery query) {
		return finRepaymentOfflineTradePoolDao.getListRepaymentOfflinePool(query);
	}

	@Override
	public List<FinRepaymentOfflineTradePoolQuery> offlineRepaymentDataQuery(FinRepaymentOfflineTradePoolQuery query) {
		return finRepaymentOfflineTradePoolDao.offlineRepaymentDataQuery(query);
	}

	@Override
	public FinRepaymentOfflineTradePoolDo getRepaymentOfflinePoolDataByTradeNo(String tradeNo) {
		return finRepaymentOfflineTradePoolDao.getRepaymentOfflinePoolDataByTradeNo(tradeNo);
	}
	/*
	*清结算系统
	* 线下还款（支付宝）总入账
	*/
	@Override
	public List<HashMap> getDayAmountByAlipay(FinRepaymentOfflineTradePoolQuery query) {
		return finRepaymentOfflineTradePoolDao.getDayAmountByAlipay(query);
	}
	/*
	*清结算系统
	* 线下还款总入账
	*/
	@Override
	public List<HashMap> getDayAmount(FinRepaymentOfflineTradePoolQuery query) {
		return finRepaymentOfflineTradePoolDao.getDayAmount(query);
	}

	@Override
	public List<HashMap> getNoUseTradesByMobile(String mobile) {
		return finRepaymentOfflineTradePoolDao.getNoUseTradesByMobile(mobile);
	}

	@Override
	public int saveRecord(FinRepaymentOfflineTradePoolDo finRepaymentOfflineTradePoolDo) {
		return finRepaymentOfflineTradePoolDao.saveRecord(finRepaymentOfflineTradePoolDo);
	}
}