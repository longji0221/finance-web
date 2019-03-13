package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.ActualPaidQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinActualPaidDao;
import com.cx.finance.dal.domain.FinActualPaidDo;
import com.cx.finance.biz.service.FinActualPaidService;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-10-29 14:29:27
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finActualPaidService")
public class FinActualPaidServiceImpl extends ParentServiceImpl<FinActualPaidDo, Long> implements FinActualPaidService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinActualPaidServiceImpl.class);
   
    @Resource
    private FinActualPaidDao finActualPaidDao;

		@Override
	public BaseDao<FinActualPaidDo, Long> getDao() {
		return finActualPaidDao;
	}

	@Override
	public List<FinActualPaidDo> getListByQuery(ActualPaidQuery query) {
		return finActualPaidDao.getListByQuery(query);
	}

	@Override
	public int batchinsert(List<FinActualPaidDo> list) {
		return finActualPaidDao.batchinsert(list);
	}
}