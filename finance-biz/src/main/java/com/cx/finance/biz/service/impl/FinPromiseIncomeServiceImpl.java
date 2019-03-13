package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.PromiseIncomeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinPromiseIncomeDao;
import com.cx.finance.dal.domain.FinPromiseIncomeDo;
import com.cx.finance.biz.service.FinPromiseIncomeService;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-01 18:30:11
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finPromiseIncomeService")
public class FinPromiseIncomeServiceImpl extends ParentServiceImpl<FinPromiseIncomeDo, Long> implements FinPromiseIncomeService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinPromiseIncomeServiceImpl.class);
   
    @Resource
    private FinPromiseIncomeDao finPromiseIncomeDao;

		@Override
	public BaseDao<FinPromiseIncomeDo, Long> getDao() {
		return finPromiseIncomeDao;
	}

	@Override
	public List<FinPromiseIncomeDo> getListByQuery(PromiseIncomeQuery query) {
		return finPromiseIncomeDao.getListByQuery(query);
	}

	@Override
	public int batchinsert(List<FinPromiseIncomeDo> list) {
		return finPromiseIncomeDao.batchinsert(list);
	}
}