package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinUserDao;
import com.cx.finance.dal.domain.FinUserDo;
import com.cx.finance.biz.service.FinUserService;



/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-10-29 17:11:13
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finUserService")
public class FinUserServiceImpl extends ParentServiceImpl<FinUserDo, Long> implements FinUserService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinUserServiceImpl.class);
   
    @Resource
    private FinUserDao finUserDao;

		@Override
	public BaseDao<FinUserDo, Long> getDao() {
		return finUserDao;
	}

	@Override
	public FinUserDo getUserByMobile(String mobile) {
		return finUserDao.getUserByMobile(mobile);
	}
}