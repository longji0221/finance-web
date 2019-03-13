package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import com.cx.finance.dal.query.UserQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinUserAccountRecordDao;
import com.cx.finance.dal.domain.FinUserAccountRecordDo;
import com.cx.finance.biz.service.FinUserAccountRecordService;

import java.util.List;


/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:06:53
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finUserAccountRecordService")
public class FinUserAccountRecordServiceImpl extends ParentServiceImpl<FinUserAccountRecordDo, Long> implements FinUserAccountRecordService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinUserAccountRecordServiceImpl.class);
   
    @Resource
    private FinUserAccountRecordDao finUserAccountRecordDao;

		@Override
	public BaseDao<FinUserAccountRecordDo, Long> getDao() {
		return finUserAccountRecordDao;
	}

	@Override
	public List<FinUserAccountRecordDo> getUserAccountRecodListByMobile(UserQuery dalQuery) {
		List<FinUserAccountRecordDo> finUserAccountRecordDos=finUserAccountRecordDao.getUserAccountRecodListByMobile(dalQuery);
		return finUserAccountRecordDos;
	}
}