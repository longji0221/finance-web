package com.cx.finance.biz.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cx.finance.biz.service.MgrOperatorService;
import com.cx.finance.dal.dao.BaseDao;
import com.cx.finance.dal.dao.MgrOperatorDao;
import com.cx.finance.dal.domain.MgrOperatorDo;



/**
 * ServiceImpl
 * 
 * @author CodeGenerate
 * @version 1.0.0 初始化
 * @date 2018-09-18 10:34:45
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
 
@Service("mgrOperatorService")
public class MgrOperatorServiceImpl extends ParentServiceImpl<MgrOperatorDo, Long> implements MgrOperatorService {
	
    @Resource
    private MgrOperatorDao mgrOperatorDao;

		@Override
	public BaseDao<MgrOperatorDo, Long> getDao() {
		return mgrOperatorDao;
	}

	@Override
	public MgrOperatorDo getByUsername(String username) {
		return mgrOperatorDao.getByUsername(username);
	}

	public String getDivMenusIdByOperatorId(Long operatorId,String menuUrl){
			return mgrOperatorDao.getDivMenusIdByOperatorId(operatorId,menuUrl);
	}

	@Override
	public void clearMenus(Long roleId) {
		 mgrOperatorDao.clearMenus(roleId);
	}


}