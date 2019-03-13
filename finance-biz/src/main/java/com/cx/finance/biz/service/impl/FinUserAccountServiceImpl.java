package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.FinUserAccountDao;
import com.cx.finance.dal.domain.FinUserAccountDo;
import com.cx.finance.biz.service.FinUserAccountService;



/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-11-03 15:07:18
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("finUserAccountService")
public class FinUserAccountServiceImpl extends ParentServiceImpl<FinUserAccountDo, Long> implements FinUserAccountService {
	
    private static final Logger logger = LoggerFactory.getLogger(FinUserAccountServiceImpl.class);
   
    @Resource
    private FinUserAccountDao finUserAccountDao;

		@Override
	public BaseDao<FinUserAccountDo, Long> getDao() {
		return finUserAccountDao;
	}

	@Override
	public void updateOutAccountByUserId(FinUserAccountDo userAccountDo) {
		finUserAccountDao.updateOutAccountByUserId(userAccountDo);
	}

	@Override
	public void updateInAccountByUserId(FinUserAccountDo userAccountDo) {
		finUserAccountDao.updateInAccountByUserId( userAccountDo);
	}

	@Override
	public FinUserAccountDo getByUserId(Long userId) {
	  	FinUserAccountDo finUserAccountDo=finUserAccountDao.getByUserId(userId);
		return finUserAccountDo;
	}

	@Override
	public FinUserAccountDo getByMobile(String mobile) {
		return finUserAccountDao.getByMobile(mobile);
	}
}