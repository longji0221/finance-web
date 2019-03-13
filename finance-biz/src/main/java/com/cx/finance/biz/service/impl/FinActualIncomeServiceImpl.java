package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.dal.query.ActualIncomeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinActualIncomeDao;
import com.cx.finance.dal.domain.FinActualIncomeDo;
import com.cx.finance.biz.service.FinActualIncomeService;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 16:36:24
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finActualIncomeService")
public class FinActualIncomeServiceImpl extends ParentServiceImpl<FinActualIncomeDo, Long> implements FinActualIncomeService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinActualIncomeServiceImpl.class);
   
    @Resource
    private FinActualIncomeDao finActualIncomeDao;

		@Override
	public BaseDao<FinActualIncomeDo, Long> getDao() {
		return finActualIncomeDao;
	}

	@Override
	public List<FinActualIncomeDo> getListByQuery(ActualIncomeQuery query) {
		return finActualIncomeDao.getListByQuery(query);
	}

	@Override
	public int batchinsert(List<FinActualIncomeDo> list) {
		return finActualIncomeDao.batchinsert(list);
	}
}